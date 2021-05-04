package com.laygen.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.laygen.beans.Machine;
import com.laygen.beans.Sensor;
import com.laygen.beans.User;
import com.laygen.database.Dictionary;
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
		action = action == null ? "home" : action;

		String url = "/index.jsp";
		HttpSession session = request.getSession();

		Dictionary dict = Dictionary.getInstance();

		final Object lock = session.getId().intern();

		synchronized (lock) {

			session.setAttribute("dict", dict);
			session.setAttribute("popup", null);

			// this will set some of the attributes to null because we don't want them
			// persisting outside of a the view scope
			if (action.equalsIgnoreCase("selectLanguage")) {
				selectLanguage(request, session);
			} else {
				initializeSessionAttributes(session);
			}

			// ACTION branches begin here
			if (action.equalsIgnoreCase("home")) {
				redirectHome(session);
			}

			if (action.equalsIgnoreCase("backupDB")) {
				backupDB(session);
			}

			if (action.equalsIgnoreCase("logout")) {
				logout(session);
			}

			if (action.equalsIgnoreCase("login")) {
				login(request, session);
			}

			if (action.equalsIgnoreCase("redirectToJoin")) {
				redirectToJoin(session);
			}

			if (action.equalsIgnoreCase("join")) {
				join(request, session);
			}

			if (action.equalsIgnoreCase("viewMachineSettings")) {
				viewMachineSettings(session);
			}

			if (action.equalsIgnoreCase("viewMachineData")) {
				viewMachineData(request, session);
			}

			if (action.equalsIgnoreCase("viewMyMachines")) {
				viewMyMachines(session);
			}

			if (action.equalsIgnoreCase("selectMachine")) {
				selectMachine(request, session);
			}

			if (action.equalsIgnoreCase("viewMachineInfo")) {
				viewMachineInfo(session);
			}

			if (action.equalsIgnoreCase("updateMachineInfo")) {
				updateMachineInfo(request, session);
			}

			if (action.equalsIgnoreCase("updateGrowSettings")) {
				updateGrowSettings(request, session);
			}

			if (action.equalsIgnoreCase("updateWaterSettings")) {
				updateWaterSettings(request, session);
			}

			if (action.equalsIgnoreCase("updateLightSettings")) {
				updateLightSettings(request, session);
			}

			if (action.equalsIgnoreCase("updateCustomColor")) {
				updateCustomColor(request, session);
			}

			if (action.equalsIgnoreCase("updateAirSettings")) {
				updateAirSettings(request, session);
			}

			if (action.equalsIgnoreCase("updateCameraSettings")) {
				updateCameraSettings(request, session);
			}

			if (action.equalsIgnoreCase("viewCameraPage")) {
				viewCameraPage(request, session);
			}

			if (action.equalsIgnoreCase("takePicture")) {
				takePicture(session);
			}

			if (action.equalsIgnoreCase("deleteImage")) {
				deleteImageFromMachine(request, session);
			}

		}

		getServletContext().getRequestDispatcher(url).forward(request, response);
	}

	private void initializeSessionAttributes(HttpSession session) {
		session.setAttribute("message", null);
		session.setAttribute("viewComponent", null);
		session.setAttribute("selectedImage", null);
	}

	private void redirectHome(HttpSession session) {
		session.setAttribute("viewComponent", null);
	}

	private void selectLanguage(HttpServletRequest request, HttpSession session) {
		String lang = request.getParameter("selectedLanguage");

		if (lang != null) {
			session.setAttribute("lang", lang);
		} else {
			session.setAttribute("lang", "ko");
		}
	}

	private void backupDB(HttpSession session) {
		String message = MessageDB.backupDB();
		session.setAttribute("message", message);
		redirectHome(session);
	}

	private void logout(HttpSession session) {
		session.invalidate();
	}

	private void login(HttpServletRequest request, HttpSession session) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = UserDB.login(email, password);
		String message = null;
		String lang = (String) session.getAttribute("lang");

		if (user == null) {
			message = Dictionary.getInstance().get("userNotFound", lang);
		} else {
			if (user.isLoggedIn()) {
				session.setAttribute("user", user);
				viewMyMachines(session);
			} else {
				message = Dictionary.getInstance().get("wrongPassword", lang);
				session.setAttribute("message", message);
				session.setAttribute("viewComponent", null);
			}
		}
	}

	private void redirectToJoin(HttpSession session) {
		session.setAttribute("viewComponent", "join");
	}

	private void join(HttpServletRequest request, HttpSession session) {
		User user = User.buildUserFromRequest(request);
		String message = user.getErrorMsg().equalsIgnoreCase("") ? UserDB.insert(user) : user.getErrorMsg();

		session.setAttribute("user", user);
		session.setAttribute("message", message);
		session.setAttribute("viewComponent", "join");
	}

	private void viewMachineSettings(HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = null;

		if (machine != null && machine.getSerialNumber() != null) {
			machine.refreshSettingsFromDB();
			if (machine.getSettings() == null) {
				message = Dictionary.getInstance().get("noSettings", (String) session.getAttribute("lang"));
			}
		} else {
			viewMyMachines(session);
		}
		session.setAttribute("message", message);
		session.setAttribute("viewComponent", "machineSettings");
	}

	private void viewMachineData(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String selectedSensor = (String) request.getParameter("selectedSensor");
		String startDate = (String) request.getParameter("startDate");
		String startTime = (String) request.getParameter("startTime");
		String endDate = (String) request.getParameter("endDate");
		String endTime = (String) request.getParameter("endTime");

		if (machine != null) {
			machine.refreshCurrentReadingsFromDB();
			if (selectedSensor == null) {
				if (machine.getSensors() != null && machine.getSensors().size() > 0) {
					selectedSensor = machine.getSensors().firstKey();
				} else {
					session.setAttribute("message",
							Dictionary.getInstance().get("noSensors", (String) session.getAttribute("lang")));
				}
			}

			if (machine.getSensors() != null) {
				Sensor sensor = null;
				for (String key : machine.getSensors().keySet()) {
					sensor = machine.getSensors().get(key);
					if (sensor != null) {
						machine.fetchSensorReadingsByDate(startDate, startTime, endDate, endTime, sensor);
					}
				}
			}
		} else {
			viewMyMachines(session);
		}

		session.setAttribute("selectedSensor", selectedSensor);
		session.setAttribute("viewComponent", "machineData");
	}

	private void viewMyMachines(HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null && user.getId() != null) {
			user.refreshAuthorizations();

			session.setAttribute("user", user);
			session.setAttribute("message",
					Dictionary.getInstance().get("selectMachinePrompt", (String) session.getAttribute("lang")));
			session.setAttribute("machine", null);
			session.setAttribute("viewComponent", null);
		} else {
			session.setAttribute("user", null);
			redirectHome(session);
		}
	}

	private void selectMachine(HttpServletRequest request, HttpSession session) {
		String serialNumber = request.getParameter("selectedMachineId");
		Machine machine = null;
		String message = null;

		if (serialNumber != null) {
			machine = new Machine();
			machine.setSerialNumber(serialNumber);
			machine.refreshAllFromDB();
		} else {
			viewMyMachines(session);
		}

		if (machine.getInfo() == null) {
			viewMyMachines(session);
		} else {
			session.setAttribute("machine", machine);
			session.setAttribute("message", message);
			session.setAttribute("viewComponent", "machineInfo");
		}
	}

	private void viewMachineInfo(HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		if (machine != null && machine.getSerialNumber() != null && machine.getInfo() != null) {
			session.setAttribute("viewComponent", "machineInfo");
		} else {
			viewMyMachines(session);
		}
	}

	private void updateMachineInfo(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute("user");

		if (user != null && user.isLoggedIn()) {

			Machine machine = (Machine) session.getAttribute("machine");
			String nickname = request.getParameter("nickname");
			String message = null;

			if (machine != null && machine.getSerialNumber() != null && nickname != null) {
				message = machine.updateNickname(nickname);
				session.setAttribute("message", message);
				viewMachineInfo(session);
			} else {
				viewMyMachines(session);
			}
			user.refreshAuthorizations();

		} else {
			session.setAttribute("user", null);
			redirectHome(session);
		}
	}

	private void updateGrowSettings(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");

		if (machine != null && machine.getSerialNumber() != null) {
			TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

			newSettings.put("plant_date", request.getParameter("plant_date"));

			session.setAttribute("message",
					machine.updateMachineSettings(newSettings, (String) session.getAttribute("lang")));
			session.setAttribute("viewComponent", "machineSettings");
		} else {
			viewMyMachines(session);
		}
	}

	private void updateWaterSettings(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String lang = (String) session.getAttribute("lang");

		if (machine != null && machine.getSerialNumber() != null) {

			String[] messages = machine.setWaterInValve(request.getParameter("water_in_valve_on"), lang);
			String message = messages[1];

			TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

			String waterCyclePeriodHours = request.getParameter("water_cycle_period_hours");
			String waterCyclePeriodMinutes = request.getParameter("water_cycle_period_minutes");

			String waterCycleDuration = request.getParameter("water_cycle_duration");
			int duration = getIntFromString(waterCycleDuration);
			int max = 60 * 60 * 24 * 50; // limit to 50 days * 24 hr * 60 min * 60 sec
			duration = duration > max ? max : duration;

			int totalWaterCycle = getTotalFromHoursMinutes(waterCyclePeriodHours, waterCyclePeriodMinutes);
			totalWaterCycle = totalWaterCycle > duration ? totalWaterCycle : duration;

			newSettings.put("water_cycle_on", request.getParameter("water_cycle_on"));
			newSettings.put("water_cycle_duration", String.valueOf(duration));
			newSettings.put("water_cycle_period", String.valueOf(totalWaterCycle));
			newSettings.put("water_in_valve_on", messages[0]);

			message = message + machine.updateMachineSettings(newSettings, lang);
			session.setAttribute("message", message);
			session.setAttribute("viewComponent", "machineSettings");
		} else {
			viewMyMachines(session);
		}
	}

	private void updateLightSettings(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String lang = (String) session.getAttribute("lang");

		if (machine != null && machine.getSerialNumber() != null) {
			TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

			String lightColorString = request.getParameter("light_color");
			if (lightColorString == null) {
				lightColorString = machine.getLightColors().firstKey();
			}
			String lightColorName = lightColorString.split("-")[0];

			newSettings.put("brightness", request.getParameter("brightness"));
			newSettings.put("light_on", request.getParameter("light_on"));
			newSettings.put("light_color", lightColorName);

			session.setAttribute("message", machine.updateMachineSettings(newSettings, lang));
			session.setAttribute("viewComponent", "machineSettings");
		} else {
			viewMyMachines(session);
		}
	}

	private void updateCustomColor(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String lang = (String) session.getAttribute("lang");
		String message = null;

		String lightColor = request.getParameter("light_color");
		String redString = request.getParameter("redValue");
		String greenString = request.getParameter("greenValue");
		String blueString = request.getParameter("blueValue");

		if (machine != null && machine.getLightColors() != null && machine.getLightColors().get(lightColor) != null) {
			int redValue = 0;
			int greenValue = 0;
			int blueValue = 0;
			try {
				redValue = Integer.parseInt(redString);
				greenValue = Integer.parseInt(greenString);
				blueValue = Integer.parseInt(blueString);
			} catch (Exception e) {
				// do nothing since these will be 0 if they fail to parse
			}
			int value = redValue * 1000000 + greenValue * 1000 + blueValue;
			String messageToMachine = String.format("%d#light_%s", value, lightColor);
			message = machine.sendCommandToMachine(messageToMachine, lang);
			session.setAttribute("message", message);
			session.setAttribute("viewComponent", "machineSettings");
		} else {
			viewMyMachines(session);
		}
	}

	private void updateAirSettings(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");

		if (machine != null && machine.getSerialNumber() != null) {
			TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

			// newSettings.put("heater_on", request.getParameter("heater_on"));
			newSettings.put("fan_on", request.getParameter("fan_on"));
			newSettings.put("fan_auto", request.getParameter("fan_auto"));
			newSettings.put("fan_humidity", request.getParameter("fan_humidity"));

			String message = machine.updateMachineSettings(newSettings, (String) session.getAttribute("lang"));
			session.setAttribute("message", message);
			session.setAttribute("viewComponent", "machineSettings");
		} else {
			viewMyMachines(session);
		}
	}

	private void updateCameraSettings(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");

		if (machine != null && machine.getSerialNumber() != null) {
			TreeMap<String, String> newSettings = new TreeMap<String, String>(Collections.reverseOrder());

			String cameraCyclePeriodHours = request.getParameter("camera_cycle_period_hours");
			String cameraCyclePeriodMinutes = request.getParameter("camera_cycle_period_minutes");

			int totalCameraCycle = getTotalFromHoursMinutes(cameraCyclePeriodHours, cameraCyclePeriodMinutes);

			newSettings.put("camera_cycle_on", request.getParameter("camera_cycle_on"));
			newSettings.put("camera_cycle_period", String.valueOf(totalCameraCycle));

			session.setAttribute("message",
					machine.updateMachineSettings(newSettings, (String) session.getAttribute("lang")));
			session.setAttribute("viewComponent", "machineSettings");
		} else {
			viewMyMachines(session);
		}
	}

	private void viewCameraPage(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");

		if (machine != null && machine.getSerialNumber() != null) {
			machine.refreshImages();
			String image = (String) request.getParameter("image");

			if (image == null) {
				if (machine.getImageNames() != null && machine.getImageNames().size() > 0) {
					image = machine.getImageNames().firstKey();
				}
			}
			if (image != null) {
				machine.fetchImage(image);
			}

			session.setAttribute("selectedImageId", image);
			session.setAttribute("viewComponent", "cameraPage");
		} else {
			viewMyMachines(session);
		}
	}

	private void takePicture(HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String message = null;
		String lang = (String) session.getAttribute("lang");

		if (machine != null) {
			message = machine.takePicture((String) session.getAttribute("lang"));
			if (message.equalsIgnoreCase("success")) {
				message = message + " " + Dictionary.getInstance().get("refreshPrompt", lang);
			}
			session.setAttribute("message", message);
			session.setAttribute("viewComponent", "cameraPage");
		} else {
			message = Dictionary.getInstance().get("machineNull", lang);
			viewMyMachines(session);
		}
	}

	private void deleteImageFromMachine(HttpServletRequest request, HttpSession session) {
		Machine machine = (Machine) session.getAttribute("machine");
		String imageId = (String) request.getParameter("image");

		if (machine != null && imageId != null) {
			session.setAttribute("selectedImageId", machine.deleteImage(imageId));
		}
		session.setAttribute("viewComponent", "cameraPage");
	}

	private int getTotalFromHoursMinutes(String hoursString, String minutesString) {

		int hours = getIntFromString(hoursString);
		int minutes = getIntFromString(minutesString);
		return ((hours * 60) + minutes) * 60;
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
