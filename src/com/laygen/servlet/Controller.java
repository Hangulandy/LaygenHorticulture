package com.laygen.servlet;

import java.io.ByteArrayInputStream;
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
import com.laygen.beans.Sensor;
import com.laygen.beans.User;
import com.laygen.database.Dictionary;
import com.laygen.database.UserDB;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.compress.utils.IOUtils;

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

				if (!(action.equalsIgnoreCase("viewCameraPage") || action.equalsIgnoreCase("takePicture")
						|| action.equalsIgnoreCase("deleteImage"))) {
					clearImages(session);
				}

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

				if (action.equalsIgnoreCase("refreshMachineInfo")) {
					refreshMachineInfo(request, response, session);
				}

				if (action.equalsIgnoreCase("updateGrowSettings")) {
					updateGrowSettings(request, response, session);
				}

				if (action.equalsIgnoreCase("updateWaterSettings")) {
					updateWaterSettings(request, response, session);
				}

				if (action.equalsIgnoreCase("updateLightSettings")) {
					updateLightSettings(request, response, session);
				}

				if (action.equalsIgnoreCase("updateCustomColor")) {
					updateCustomColor(request, response, session);
				}

				if (action.equalsIgnoreCase("updateAirSettings")) {
					updateAirSettings(request, response, session);
				}

				if (action.equalsIgnoreCase("updateCameraSettings")) {
					updateCameraSettings(request, response, session);
				}

				if (action.equalsIgnoreCase("viewMachineData")) {
					viewMachineData(request, response, session);
				}

				if (action.equalsIgnoreCase("viewCameraPage")) {
					viewCameraPage(request, response, session);
				}

				if (action.equalsIgnoreCase("selectImage")) {
					selectImage(request, response, session);
				}

				if (action.equalsIgnoreCase("captureImage")) {
					captureImage(response, session);
				}

				if (action.equalsIgnoreCase("deleteImage")) {
					deleteImageFromMachine(request, response, session);
				}

			}

		} catch (IllegalStateException e) {
			sendSessionExpireMessage(response);
		}

	}

	private void clearImages(HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		if (machine != null) {
			machine.clearImages();
			session.setAttribute("machine", machine);
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

		session.setAttribute("user", user.isLoggedIn() ? user : null);
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
		
		System.out.println(serialNumber);
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
					machine = null;
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
		User user = (User) session.getAttribute("user");

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
		User user = (User) session.getAttribute("user");

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

	private void updateWaterSettings(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "invalidUserMessage";
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {

					String[] messages = machine.setWaterInValve(request.getParameter("water_in_valve_on"));
					message = messages[1];

					if (message.equalsIgnoreCase("null")) {
						// everything is good so far
						TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

						int period = getIntFromString(request.getParameter("water_cycle_period"));
						int duration = getIntFromString(request.getParameter("water_cycle_duration"));
						int max = 60 * 60 * 24 * 50; // limit to 50 days * 24 hr * 60 min * 60 sec
						duration = duration > max ? max : duration;

						period = period > duration ? period : duration;

						newSettings.put("water_cycle_on", request.getParameter("water_cycle_on"));
						newSettings.put("water_cycle_duration", String.valueOf(duration));
						newSettings.put("water_cycle_period", String.valueOf(period));
						newSettings.put("water_in_valve_on", messages[0]);

						message = machine.sendMachineSettngs(newSettings);
					}
				} else {
					message = "unableToUpdate";
				}
			} else {
				message = "userNotAuthorized";
			}
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void updateLightSettings(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "invalidUserMessage";
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					// everything is good so far
					TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

					String lightColorString = request.getParameter("light_color");
					if (lightColorString == null) {
						lightColorString = machine.getLightColors().firstKey();
					}
					String lightColorName = lightColorString.split("-")[0];

					newSettings.put("brightness", request.getParameter("brightness"));
					newSettings.put("light_on", request.getParameter("light_on"));
					newSettings.put("light_color", lightColorName);

					message = machine.sendMachineSettngs(newSettings);
				}
			} else {
				message = "unableToUpdate";
			}
		} else {
			message = "userNotAuthorized";
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void updateCustomColor(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "invalidUserMessage";
		User user = (User) session.getAttribute("user");

		String lightColor = request.getParameter("light_color");
		int redValue = getIntFromString(request.getParameter("redValue"));
		int greenValue = getIntFromString(request.getParameter("greenValue"));
		int blueValue = getIntFromString(request.getParameter("blueValue"));

		if (user != null) {
			if (userIsAuth(session)) {

				// TODO - check that the light color parameter is actually a light color for
				// that machine
				if (machine != null && userIsAuth(session) && machine.getLightColors() != null
						&& machine.getLightColors().get(lightColor) != null) {
					// everything is good so far

					int value = redValue * 1000000 + greenValue * 1000 + blueValue;
					String messageToMachine = String.format("%d#light_%s", value, lightColor);
					message = machine.sendCommandToMachine(messageToMachine);
				} else {
					message = "unableToUpdate";
				}
			} else {
				message = "userNotAuthorized";
			}
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void updateAirSettings(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "invalidUserMessage";
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					// everything is good so far
					TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

					// int heaterOn = getIntFromString(request.getParameter("heater_on"));
					int fanOn = getIntFromString(request.getParameter("fan_on"));
					int fanAuto = getIntFromString(request.getParameter("fan_auto"));
					int fanHumidity = getIntFromString(request.getParameter("fan_humidity"));

					// newSettings.put("heater_on", String.valueOf(heaterOn));
					newSettings.put("fan_on", String.valueOf(fanOn));
					newSettings.put("fan_auto", String.valueOf(fanAuto));
					newSettings.put("fan_humidity", String.valueOf(fanHumidity));

					message = machine.sendMachineSettngs(newSettings);
				}
			} else {
				message = "unableToUpdate";
			}
		} else {
			message = "userNotAuthorized";
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void updateCameraSettings(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "null";
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {

					if (message.equalsIgnoreCase("null")) {
						// everything is good so far
						TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

						int period = getIntFromString(request.getParameter("camera_cycle_period"));
						int onOrOff = getIntFromString(request.getParameter("camera_cycle_on"));

						newSettings.put("camera_cycle_on", String.valueOf(onOrOff));
						newSettings.put("camera_cycle_period", String.valueOf(period));

						message = machine.sendMachineSettngs(newSettings);
					}
				} else {
					message = "unableToUpdate";
				}
			} else {
				message = "userNotAuthorized";
			}
		} else {
			message = "invalidUserMessage";
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void viewMachineData(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String startDate = (String) request.getParameter("startDate");
		String startTime = (String) request.getParameter("startTime");
		String endDate = (String) request.getParameter("endDate");
		String endTime = (String) request.getParameter("endTime");
		String message = "invalidUserMessage";
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					machine.fetchCurrentReadingsFromDB();

					if (machine.getSensors() != null) {
						message = "null";
						Sensor sensor = null;
						for (String key : machine.getSensors().keySet()) {
							sensor = machine.getSensors().get(key);
							machine.fetchSensorReadingsByDate(startDate, startTime, endDate, endTime, sensor);
						}
						machine.reduceReadings(1000);
					} else {
						message = "noSensors";
					}
				} else {
					message = "invalidMachineInformationMessage";
				}
			} else {
				message = "userNotAuthorized";
			}
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void viewCameraPage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "null";
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					// everything is good so far
					machine.fetchImageList();

				} else {
					message = "invalidMachineInformationMessage";
				}
			} else {
				message = "userNotAuthorized";
			}
		} else {
			message = "invalidUserMessage";
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void selectImage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "null";
		User user = (User) session.getAttribute("user");
		String imageId = request.getParameter("imageId");
		// byte[] imageBytes = null;
		String imageString = null;

		if (imageId != null && !imageId.equalsIgnoreCase("")) {

			if (user != null) {
				if (userIsAuth(session)) {
					if (machine != null && machine.getSerialNumber() != null) {
						// everything is good so far
						imageString = machine.fetchImage(imageId);
						if (imageString.length() < 10) {
							message = "cannotFindImageMessage";
						} else {
							machine.clearReadings();
							session.setAttribute("machine", machine);
						}
					} else {
						message = "invalidMachineInformationMessage";
					}
				} else {
					message = "userNotAuthorized";
				}
			} else {
				message = "invalidUserMessage";
			}
		} else {
			message = "cannotFindImageMessage";
		}
		sendObjectWithResponse(machine, message, session, response, imageString);
	}

	private void captureImage(HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "null";
		User user = (User) session.getAttribute("user");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					// everything is good so far
					message = machine.takePicture();
				} else {
					message = "invalidMachineInformationMessage";
				}
			} else {
				message = "userNotAuthorized";
			}
		} else {
			message = "invalidUserMessage";
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private void deleteImageFromMachine(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = "null";
		User user = (User) session.getAttribute("user");
		String imageId = (String) request.getParameter("imageId");

		if (user != null) {
			if (userIsAuth(session)) {
				if (machine != null && machine.getSerialNumber() != null) {
					// everything is good so far
					machine.deleteImage(imageId);
					message = "success";
				} else {
					message = "invalidMachineInformationMessage";
				}
			} else {
				message = "userNotAuthorized";
			}
		} else {
			message = "invalidUserMessage";
		}

		sendObjectWithResponse(machine, message, session, response);
	}

	private boolean userIsAuth(HttpSession session) {
		boolean outcome = false;
		Machine machine = (Machine) session.getAttribute("machine");
		User user = (User) session.getAttribute("user");
		if (machine != null) {
			outcome = machine.userIsAuthorized(user);
		}
		return outcome;
	}

	private void sendObjectWithResponse(Object obj, String message, HttpSession session, HttpServletResponse response,
			String string) {

		User user = (User) session.getAttribute("user");

		if (user == null) {
			message = "invalidUserMessage";
			obj = null;
		}

		MyResponse resp = new MyResponse();
		resp.setUser(user);
		resp.setObject(user == null ? null : obj);
		resp.setMessage(message);
		resp.setString(string);
		// resp.printIt();

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

	private void sendObjectWithResponse(Object obj, String message, HttpSession session, HttpServletResponse response) {
		sendObjectWithResponse(obj, message, session, response, null);
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

	private void sendImageAsResponse(byte[] imageBytes, HttpServletResponse response) {
		ByteArrayInputStream in = null;
		try {
			in = new ByteArrayInputStream(imageBytes);
			Base64InputStream stream = new Base64InputStream(in);
			IOUtils.copy(stream, response.getOutputStream());
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getIntFromString(String string) {
		int output = 0;
		try {
			output = Integer.parseInt(string);
		} catch (Exception e) {
			// do nothing
		}
		return output;
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
