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
import com.laygen.database.MessageDB;
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
		String url = "/index.jsp";
		HttpSession session = request.getSession();

		final Object lock = session.getId().intern();

		synchronized (lock) {

			session.setAttribute("message", null);
			session.setAttribute("loginMessage", null);
			session.setAttribute("joinMessage", null);
			session.setAttribute("machineInfoViewMessage", null);
			session.setAttribute("machineSettingsViewMessage", null);
			session.setAttribute("machineDataViewMessage", null);
			session.setAttribute("viewComponent", null);

			Machine machine = (Machine) session.getAttribute("machine");
			if (machine == null) {
				machine = new Machine();
				machine.setSerialNumber("0123456789");
			}

			User user = (User) session.getAttribute("user");

			// ACTION branches begin here

			// ACTION: home
			if (action.equalsIgnoreCase("home")) {
				// will just execute defaults which lead to index
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

				String loginMessage = null;
				user = UserDB.login(email, password);

				if (user == null) {
					loginMessage = "Could not find that user in the database. Check your credentials and try again, or join our community.";
				} else {
					// At this point, the user is in the data store
					user.printUser();
					if (user.isLoggedIn()) {
						loginMessage = user.toString();
					} else {
						loginMessage = "Password is incorrect.";
					}
				}
				session.setAttribute("loginMessage", loginMessage);
				session.setAttribute("message", loginMessage);
				session.setAttribute("user", user);
				session.setAttribute("viewComponent", null);
			}

			// ACTION: redirect to join
			if (action.equalsIgnoreCase("redirectToJoin")) {
				session.setAttribute("viewComponent", "join");
			}

			// ACTION: join
			if (action.equalsIgnoreCase("join")) {
				user = User.buildUserFromRequest(request);
				session.setAttribute("joinMessage",
						user.getErrorMsg().equalsIgnoreCase("") ? UserDB.insert(user) : user.getErrorMsg());
				session.setAttribute("user", user);
				session.setAttribute("viewComponent", "join");
			}

			// ACTION: view machine info
			if (action.equalsIgnoreCase("viewMachineInfo")) {
				machine.refreshInfoFromDB();
				String machineInfoViewMessage = "";
				if (machine.getInfo() == null) {
					machineInfoViewMessage = "No info was returned for that machine.";
				} else {
					machineInfoViewMessage = "Here is the information for machine " + machine.getSerialNumber();
				}
				session.setAttribute("machineInfoViewMessage", machineInfoViewMessage);
				session.setAttribute("viewComponent", "machineInfo");
				session.setAttribute("machine", machine);
			}

			// ACTION: view settings
			if (action.equalsIgnoreCase("viewMachineSettings")) {
				machine.refreshSettingsFromDB();
				String machineInfoViewMessage = "";
				if (machine.getSettings() == null) {
					machineInfoViewMessage = "No settings were returned for that machine.";
				} else {
					machineInfoViewMessage = "Here are the settings for machine " + machine.getSerialNumber();
				}
				session.setAttribute("machineSettingsViewMessage", machineInfoViewMessage);
				session.setAttribute("viewComponent", "machineSettings");
				session.setAttribute("machine", machine);
			}

			// ACTION: view data
			if (action.equalsIgnoreCase("viewMachineData")) {
				TreeSet<Message> readings = null;
				String message = "";
				readings = MachineDB.getReadingsForMachine(machine.getSerialNumber());

				session.setAttribute("readings", readings);

				if (readings == null) {
					// Do something
					System.out.println("'readings' is null");
				} else {
					// Do something else
					System.out.println("'readings' is not null");
				}
				session.setAttribute("machineDataViewMessage", message);
				session.setAttribute("viewComponent", "machineData");
			}

			// ACTION: select machine
			if (action.equalsIgnoreCase("selectMachine")) {
				// TODO - logic to actually select machine from a list
				String serialNumber = "0123456789";
				machine = new Machine();
				machine.setSerialNumber(serialNumber);
				machine.refreshAllFromDB();
				session.setAttribute("machine", machine);
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
								
				String message = machine.updateMachineSettings(newSettings);
				
				session.setAttribute("machineSettingsViewMessage", message);
				session.setAttribute("viewComponent", "machineSettings");
				session.setAttribute("machine", machine);
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
