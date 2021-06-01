package com.laygen.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.laygen.beans.Machine;
import com.laygen.beans.MyResponse;
import com.laygen.beans.User;
import com.laygen.database.Dictionary;
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

		try {
			HttpSession session = request.getSession();

			final Object lock = session.getId().intern();

			synchronized (lock) {

				Dictionary dict = (Dictionary) session.getAttribute("dict");

				if (dict == null || dict.getEntries() == null) {
					dict = Dictionary.getInstance();
					session.setAttribute("dict", dict);
				}

				if (action.equalsIgnoreCase("getDictionary")) {
					getDictionary(request, response, session);
				} else {
					initializeSessionAttributes(session);
				}

				if (action.equalsIgnoreCase("login")) {
					login(request, response, session);
				}

				if (action.equalsIgnoreCase("logout")) {
					logout(session);
				}

				if (action.equalsIgnoreCase("join")) {
					join(request, response, session);
				}

				if (action.equalsIgnoreCase("getAuthorizations")) {
					getAuthorizations(request, response, session);
				}

				if (action.equalsIgnoreCase("selectMachine")) {
					selectMachine(request, response, session);
				}

				if (action.equalsIgnoreCase("searchForUser")) {
					searchForUser(request, response, session);
				}

				if (action.equalsIgnoreCase("addUser")) {
					addUser(request, response, session);
				}

				if (action.equalsIgnoreCase("removeUser")) {
					removeUser(request, response, session);
				}

				if (action.equalsIgnoreCase("transferOwnership")) {
					transferOwnership(request, response, session);
				}
				
				if (action.equalsIgnoreCase("refreshMachineInfo")){
					refreshMachineInfo(request, response, session);
				}

				if (action.equalsIgnoreCase("updateGrowSettings")) {
					updateGrowSettings(request, response, session);
				}
			}

		} catch (IllegalStateException e) {
			sendSessionExpireMessage(response);
		}

	}

	private void sendSessionExpireMessage(HttpServletResponse response) {

		MyResponse resp = new MyResponse();
		resp.setUser(null);
		resp.setObject(null);
		resp.setMessage("sessionExpiredMessage");

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			Gson gson = new Gson();
			String jsonData = gson.toJson(resp);
			out.print(jsonData);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = new User();

		String message = user.login(email, password);

		session.setAttribute("user", user);
		sendObjectWithResponse(user, message, session, response);
	}

	private void logout(HttpSession session) {
		session.invalidate();
	}

	private void join(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		User user = User.buildUserFromRequest(request);
		String message = user.getErrorMsg() == null ? UserDB.insert(user) : "null";

		sendObjectWithResponse(user, message, session, response);
	}

	private void getAuthorizations(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		User user = (User) session.getAttribute("user");
		String message = null;

		if (user != null && user.getId() != null) {
			user.fetchAuthorizations();
			message = "selectMachinePrompt";
		} else {
			user = null;
			logout(session);
		}
		session.setAttribute("user", user);
		sendObjectWithResponse(user, message, session, response);
	}

	private void selectMachine(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String serialNumber = request.getParameter("selectedMachineId");
		Machine machine = null;
		String message = "invalidValueMessage";

		// First, validate serial number; get all machine info if it exists
		if (serialNumber != null) {
			machine = new Machine();
			machine.setSerialNumber(serialNumber);
			machine.fetchAllFromDB();
			// Next, check that there is info returned from DB; if yes, check user auth; if
			// good, send machine info, otherwise send null response
			if (machine != null && machine.getInfo() != null) {
				session.setAttribute("machine", machine);
				if (!userIsAuth(session)) {
					message = "userNotAuthorized";
				} else {
					message = "null";
				}
			}
		}
		sendObjectWithResponse(machine, message, session, response);
	}

	private void searchForUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// First, get machine and user variables
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "null";
		User searchedUser = null;

		// Make sure user is authorized
		if (userIsAuth(session)) {
			if (machine != null) {

				// Get searched user by email
				String email = (String) request.getParameter("email");
				String uuid = UserDB.fetchUUIDByEmail(email.trim());
				if (uuid != null) {
					searchedUser = UserDB.fetchUserByUUID(uuid);
				} else {
					message = "cannotFindUserMessage";
				}
			} else {
				message = "invalidValueMessage";
			}
		} else {
			message = "mustBeOwnerMessage";
		}

		sendObjectWithResponse(searchedUser, message, session, response);
	}

	private void addUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = null;

		if (machine != null && userIsOwner(session)) {
			String userIdToAdd = request.getParameter("userToAdd");
			User userToAdd = UserDB.fetchUserByUUID(userIdToAdd);

			if (userToAdd != null) {
				message = machine.addAuthorizationByUUID(userIdToAdd);
			}
		} else {
			message = "mustBeOwnerMessage";
		}
		machine.fetchAuthorizedUsersFromDB();

		sendObjectWithResponse(machine, message, session, response);
	}

	private void removeUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = null;
		String userIdToRemove = request.getParameter("userToRemove");
		User owner = (User) session.getAttribute("user");

		if (machine != null && userIsOwner(session) && machine.userIsAuthorized(userIdToRemove)) {
			if (!owner.getId().equalsIgnoreCase(userIdToRemove)) {
				message = machine.removeAuthorizationByUUID(userIdToRemove);
			} else {
				message = "cannotRemoveOwnerMessage";
			}
		} else {
			message = "mustBeOwnerMessage";
		}
		machine.fetchAuthorizedUsersFromDB();

		sendObjectWithResponse(machine, message, session, response);
	}

	private void transferOwnership(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String newOwnerId = request.getParameter("newOwnerId");
		User user = (User) session.getAttribute("user");
		String message = "null";

		// Check that session user is owner
		if (user.getEmail().equalsIgnoreCase(machine.getOwnerEmail())) {
			// if so, he can do the operation
			message = machine.transferOwnership(newOwnerId);
			user.refreshAuthorizations();
		} else {
			// otherwise, he cannot
			message = "mustBeOwnerMessage";
		}
		
		sendObjectWithResponse(machine, message, session, response);
	}

	private void refreshMachineInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "null";
		User user = getLoggedInUser(session);
		
		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					machine.fetchAllFromDB();
				}
				// TODO - decide which error message to send in this case
			} else {
				message = "userNotAuthorized";
			}
		}
		
		sendObjectWithResponse(machine, message, session, response);
	}

	private void updateGrowSettings(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "invalidUserMessage";
		User user = getLoggedInUser(session);

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());
					newSettings.put("plant_date", request.getParameter("plant_date"));
					message = machine.sendMachineSettngs(newSettings);
				} else {
					message = "unableToUpdate";
				}
			} else {
				message = "userNotAuthorized";
			}
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private User getLoggedInUser(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user != null && user.isLoggedIn()) {
			return user;
		}
		return null;
	}

	private boolean userIsAuth(HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		User user = (User) session.getAttribute("user");
		return machine.userIsAuthorized(user);
	}

	private void sendObjectWithResponse(Object obj, String message, HttpSession session, HttpServletResponse response) {

		User user = (User) session.getAttribute("user");

		if (!user.isLoggedIn()) {
			user = null;
			obj = null;
		}

		MyResponse resp = new MyResponse();
		resp.setUser(user);
		resp.setObject(user == null ? null : obj);
		resp.setMessage(message);
		
		resp.printIt();

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			Gson gson = new Gson();
			String jsonData = gson.toJson(resp);
			out.print(jsonData);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMapAsResponse(Map map, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");

			PrintWriter out = response.getWriter();
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			Type gsonType = new TypeToken<Map>() {
			}.getType();
			String jsonData = gson.toJson(map, gsonType);
			out.print(jsonData);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getDictionary(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Dictionary dict = (Dictionary) session.getAttribute("dict");
		sendMapAsResponse(dict.getEntries(), response);
	}

	private void initializeSessionAttributes(HttpSession session) {
		session.setAttribute("message", "null");
		session.setAttribute("popupMessage", "null");
		session.setAttribute("viewComponent", null);
		session.setAttribute("selectedImage", null);
		session.setAttribute("searchedUser", null);
	}

	private boolean userIsOwner(HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		User user = (User) session.getAttribute("user");
		return user.getEmail().equalsIgnoreCase(machine.getInfo().get("owner_email"));
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
