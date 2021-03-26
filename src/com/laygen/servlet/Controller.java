package com.laygen.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.laygen.beans.Sensor;
import com.laygen.beans.Machine;
import com.laygen.beans.User;
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
			String selectedSensor = (String) session.getAttribute("component");

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
						user.refreshAuthorizations();
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
				selectedSensor = (String) request.getParameter("selectedSensor");
				if (selectedSensor == null) {
					if (machine.getSensors() != null && machine.getSensors().size() > 0) {
						selectedSensor = machine.getSensors().firstKey();
					}
				}
				if (selectedSensor != null) {
					machine.setSelectedSensor(machine.getSensors().get(selectedSensor));
					machine.refreshSelectedSensorReadings();
				}
				viewComponent = "machineData";
			}

			// ACTION: view my authorizations
			if (action.equalsIgnoreCase("viewMyMachines")) {
				user.refreshAuthorizations();
				viewComponent = "viewMyMachines";
			}

			// ACTION: select machine
			if (action.equalsIgnoreCase("selectMachine")) {
				String serialNumber = request.getParameter("selectedMachineId");
				machine = new Machine();
				if (serialNumber != null) {
					machine.setSerialNumber(serialNumber);
					machine.refreshAllFromDB();
				} else {
					message = "No information to show about that machine.";
				}
				viewComponent = "machineInfo";
			}

			// ACTION: update settings
			if (action.equalsIgnoreCase("updateSettings")) {

				HashMap<String, String> newSettings = new HashMap<String, String>();

				String pumpCycle = request.getParameter("pump_cycle");
				String pumpDuration = request.getParameter("pump_duration");

				try {
					int wd = Integer.parseInt(pumpDuration);
					int maxWd = 60 * 60 * 24 * 50; // limit to 50 days * 24 hr * 60 min * 60 sec
					wd = wd > maxWd ? maxWd : wd;
					int wc = Integer.parseInt(pumpCycle);
					wc = wc > wd ? wd : wc;
				} catch (Exception e) {
					// do nothing
				}

				newSettings.put("brightness", request.getParameter("brightness"));
				newSettings.put("light_on", request.getParameter("light_on"));
				newSettings.put("pump_on", request.getParameter("pump_on"));
				newSettings.put("camera_on", request.getParameter("camera_on"));
				newSettings.put("camera_interval", request.getParameter("camera_interval"));
				newSettings.put("pump_duration", pumpDuration);
				newSettings.put("pump_cycle", pumpCycle);

				message = machine.updateMachineSettings(newSettings);
				viewComponent = "machineSettings";
			}

			// ACTION: view camera page; also used to refresh the image list and view image
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

			// ACTION: delete image
			if (action.equalsIgnoreCase("deleteImage")) {
				String imageId = (String) request.getParameter("image");
				image = machine.deleteImage(imageId);
				viewComponent = "cameraPage";
			}

			// finally, set session attributes to use in the SPA
			if (!action.equalsIgnoreCase("logout")) {
				session.setAttribute("message", message);
				session.setAttribute("viewComponent", viewComponent);
				session.setAttribute("machine", machine);
				session.setAttribute("user", user);
				session.setAttribute("selectedImageId", image);
				session.setAttribute("selectedSensor", selectedSensor);
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
