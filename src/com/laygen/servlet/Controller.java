package com.laygen.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.laygen.beans.Machine;
import com.laygen.beans.Message;
import com.laygen.beans.User;
import com.laygen.database.MachineDB;
import com.laygen.database.UserDB;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Controller() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		action = action == null ? "home" : action;

		String url = "/index.jsp";
		HttpSession session = request.getSession();

		final Object lock = session.getId().intern();

		synchronized (lock) {

			String message = null;
			String viewComponent = null;
			String image = null;
			User user = (User) session.getAttribute("user");
			Machine machine = (Machine) session.getAttribute("machine");

			// ACTION branches begin here

			// ACTION: home
			if (action.equalsIgnoreCase("home")) {
				// will just execute defaults which lead to home page
				viewComponent = null;
			}

			// ACTION: logout
			if (action.equalsIgnoreCase("logout")) {
				session.invalidate();
				// TODO - do we want to put a message on the home screen also?
			}

			// ACTION: login
			if (action.equalsIgnoreCase("login")) {
				String email = request.getParameter("email");
				String password = request.getParameter("password");

				user = UserDB.login(email, password);

				if (user == null) {
					message = "Could not find that user in the database. Check your credentials and try again, or join our community.";
				} else {
					// At this point, the user is in the data store
					user.printUser();
					if (user.isLoggedIn()) {
						// do nothing
					} else {
						message = "Password is incorrect.";
					}
				}
				viewComponent = null;
			}

			// ACTION: redirect to join
			if (action.equalsIgnoreCase("redirectToJoin")) {
				viewComponent = "join";
			}

			// ACTION: join
			if (action.equalsIgnoreCase("join")) {
				user = User.buildUserFromRequest(request);
				message = user.getErrorMsg().equalsIgnoreCase("") ? UserDB.insert(user) : user.getErrorMsg();
				viewComponent = "join";
			}

			// ACTION: view machine info
			if (action.equalsIgnoreCase("viewMachineInfo")) {
				machine.refreshInfoFromDB();
				if (machine.getInfo() == null) {
					message = "No info was returned for that machine.";
				} else {
					message = "Here is the information for machine " + machine.getSerialNumber();
				}
				viewComponent = "machineInfo";
			}

			// ACTION: view settings
			if (action.equalsIgnoreCase("viewMachineSettings")) {
				machine.refreshSettingsFromDB();
				if (machine.getSettings() == null) {
					message = "No settings were returned for that machine.";
				} else {
					message = "Here are the settings for machine " + machine.getSerialNumber();
				}
				viewComponent = "machineSettings";
			}

			// ACTION: view data
			if (action.equalsIgnoreCase("viewMachineData")) {
				// TODO - should this be a single sensor history view
				TreeSet<Message> readings = null;
				readings = MachineDB.getReadingsForMachine(machine.getSerialNumber());

				if (readings == null) {
					// Do something
					System.out.println("'readings' is null");
				} else {
					// Do something else
					System.out.println("'readings' is not null");
				}
				viewComponent = "machineData";
				session.setAttribute("readings", readings);
			}

			// ACTION: view my authorizations
			if (action.equalsIgnoreCase("viewMyMachines")) {

//				user.refreshAuthorizations();
//				if (user.getAuthorizations() == null || user.getAuthorizations().size() == 0) {
//					message = "You have no machines to show.";
//				} else {
//					message = "Here are the machines you can use:";
//				}
//				viewComponent = "viewMyMachines";
			}

			// ACTION: select machine
			if (action.equalsIgnoreCase("selectMachine")) {
				// TODO - logic to actually select machine from a list
				String serialNumber = "0123456789";
				machine = new Machine();
				machine.setSerialNumber(serialNumber);
				machine.refreshAllFromDB();
				viewComponent = "machineInfo";
			}

			// ACTION: update settings
			if (action.equalsIgnoreCase("updateSettings")) {

				HashMap<String, String> newSettings = new HashMap<String, String>();

				String pumpCycle = request.getParameter("pump_cycle");
				String pumpDuration = request.getParameter("pump_duration");

				try {
					int wc = Integer.parseInt(pumpCycle);
					int wd = Integer.parseInt(pumpDuration);

					if (wc < wd) {
						pumpCycle = pumpDuration;
					}
				} catch (Exception e) {
					// do nothing
				}

				// TODO - get a list of possible settings and do a loop
				// put new settings in the machine object
				newSettings.put("brightness", request.getParameter("brightness"));
				newSettings.put("light_on", request.getParameter("light_on"));
				newSettings.put("pump_on", request.getParameter("pump_on"));
				newSettings.put("pump_duration", pumpDuration);
				newSettings.put("pump_cycle", pumpCycle);

				message = machine.updateMachineSettings(newSettings);
				viewComponent = "machineSettings";
			}

			// ACTION: view camera page; also used to refresh the image list
			if (action.equalsIgnoreCase("viewCameraPage")) {
				machine.refreshImages();
				image = (String) request.getParameter("image");
				if (image == null) {
					if (machine.getImageNames() != null && machine.getImageNames().size() > 0) {
						image = machine.getImageNames().firstKey();
					}
				}
				if (image != null) {
					machine.fetchImage(image);					
				}
				viewComponent = "cameraPage";
			}

			// ACTION: take a picture
			if (action.equalsIgnoreCase("takePicture")) {
				message = machine.takePicture() + ". Be sure to refresh the list in a few seconds.";
				request.setAttribute("action", null);
				viewComponent = "cameraPage";
			}

			// ACTION: view image
			if (action.equalsIgnoreCase("viewImage")) {
				String imageId = (String) request.getParameter("image");
				System.out.println("Imageid is " + imageId);
				if (imageId != null) {
					machine.fetchImage(imageId);
					image = imageId;
				} else {
					// TODO - not sure if anything should happen here
				}
				viewComponent = "cameraPage";
			}

			// ACTION: delete image
			if (action.equalsIgnoreCase("deleteImage")) {
				String imageId = (String) request.getParameter("image");
				if (imageId != null) {
					MachineDB.deleteImage(imageId);
				}
				machine.setImage(null);
				image = null;
				viewComponent = "cameraPage";
			}

			// finally, set session attributes to use in the SPA
			if (!action.equalsIgnoreCase("logout")) {
				session.setAttribute("message", message);
				session.setAttribute("viewComponent", viewComponent);
				session.setAttribute("machine", machine);
				session.setAttribute("user", user);
				session.setAttribute("selectedImageId", image);
			}
		}

//		TreeSet<Message> messages = null;
//
//		messages = MessageDB.getAllMessages();
//		
//		session.setAttribute("messages", messages);
//
//		if (messages == null) {
//			// Do something
//			System.out.println("'messages' is null");
//		} else {
//			// Do something else
//			System.out.println("'messages' is not null");
//			url = "/display_all_messages.jsp";
//		}

		getServletContext().getRequestDispatcher(url).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
