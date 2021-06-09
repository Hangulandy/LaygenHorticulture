package com.laygen.beans;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import com.laygen.database.MachineDB;

public class Machine {

	private String serialNumber;
	private Map<String, String> info;
	private Map<String, String> readings;
	private Map<String, String> settings;
	private TreeMap<String, String> lightColors;
	private TreeMap<String, Sensor> sensors;
	private TreeMap<String, String> images;
	private TreeSet<User> authorizedUsers;
	private Sensor selectedSensor;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;

	public Machine() {
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void fetchAllFromDB() {

		fetchInfoFromDB();
		if (this.getInfo() == null) {
			return;
		}

		fetchCurrentReadingsFromDB();
		if (this.getReadings() == null) {
			return;
		}

		fetchSettingsFromDB(); // includes fetch light colors
		if (this.getSettings() == null) {
			return;
		}

		fetchSensorsFromDB();
		if (this.getSensors() == null) {
			return;
		}

		fetchAuthorizedUsersFromDB();
		if (this.getAuthorizedUsers() == null) {
			return;
		}
	}

	public void fetchInfoFromDB() {
		setInfo(null);
		setInfo(MachineDB.fetchMachineInfoBySerialNumber(getSerialNumber()));
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public void fetchSettingsFromDB() {
		setSettings(MachineDB.fetchCurrentSettingsBySerialNumber(getSerialNumber()));
		this.fetchLightColorsFromDB();
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public TreeMap<String, Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(TreeMap<String, Sensor> sensors) {
		this.sensors = sensors;
	}

	public TreeMap<String, String> getImageNames() {
		return images;
	}

	public void setImageNames(TreeMap<String, String> images) {
		this.images = images;
	}

	public void fetchAuthorizedUsersFromDB() {
		this.setAuthorizedUsers(MachineDB.fetchAuthorizedUsers(this));
	}

	public TreeSet<User> getAuthorizedUsers() {
		return authorizedUsers;
	}

	public void setAuthorizedUsers(TreeSet<User> authorizedUsers) {
		this.authorizedUsers = authorizedUsers;
	}

	public String sendMachineSettngs(TreeMap<String, String> newSettings) {
		try {
			int port = Integer.parseInt(this.getInfo().get("port"));
			try (Socket socket = new Socket(this.getInfo().get("ip"), port)) {
				try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					String msg;
					for (String key : newSettings.keySet()) {
						msg = String.format("%s#%s", newSettings.get(key), key);
						out.println(msg);
						this.getSettings().put(key, newSettings.get(key));
						System.out.println(msg);
					}
					return "successAndRefreshPrompt";
				}
			}
		} catch (Exception e) {
			return "commError";
		}
	}

	public String takePicture() {
		return sendCommandToMachine("1#flash_on");
	}

	public String sendCommandToMachine(String msg) {
		try {
			int port = Integer.parseInt(this.getInfo().get("port"));
			try (Socket socket = new Socket(this.getInfo().get("ip"), port)) {
				try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					out.println(msg);
					System.out.println("Sent message to machine : " + msg);
				}
				return "successAndRefreshPrompt";
			}
		} catch (Exception e) {
			return "commError";
		}
	}

	public String sendOpenValveMessage() {
		String result = "null";
		// check for current value of water level
		this.fetchCurrentReadingsFromDB();
		if (this.getReadings().get("water_level1") != null) {
			String waterLevel = this.getReadings().get("water_level1");
			try {
				int wl = Integer.parseInt(waterLevel);
				if (wl <= 4) {
					result = this.sendCommandToMachine("1#water_in_valve_on");
				} else {
					result = "waterLevelHighMessage";
				}
			} catch (Exception e) {
				result = "invalidValueMessage";
			}
		} else {
			result = "invalidValueMessage";
		}
		return result;
	}

	public void fetchCurrentReadingsFromDB() {
		setReadings(MachineDB.fetchCurrentReadingsBySerialNumber(getSerialNumber()));
	}

	public Map<String, String> getReadings() {
		return readings;
	}

	public void setReadings(Map<String, String> readings) {
		this.readings = readings;
	}

	public void fetchImageList() {
		TreeMap<String, String> outputMap = new TreeMap<String, String>(Collections.reverseOrder());
		outputMap.putAll(MachineDB.fetchImageNamesForMachine(this.getSerialNumber()));
		setImageNames(outputMap);
	}

	// Returns the image as a Base64 encoded String
	public String fetchImage(String imageId) {
		String string = null;

		byte[] bytes = MachineDB.fetchImageBytesById(imageId);

		if (bytes != null) {
			string = Base64.getEncoder().encodeToString(bytes);
		}
		
		return string;
	}

	public void deleteImage(String imageId) {
		if (imageId != null) {
			MachineDB.deleteImage(imageId);
			this.fetchImageList();
		}
	}

	public void fetchLightColorsFromDB() {
		TreeMap<String, String> colors = MachineDB.fetchLightColors(this);
		TreeMap<String, String> newColors = MachineDB.fetchCustomLightColors(this);
		if (newColors != null) {
			colors.putAll(newColors);
		}
		setLightColors(colors);
	}

	public void fetchSensorsFromDB() {
		setSensors(MachineDB.fetchSensorList(this));

		Sensor sensor = null;
		if (this.getSensors() != null && this.getSensors().keySet() != null) {
			for (String key : this.getSensors().keySet()) {
				sensor = this.getSensors().get(key);
				sensor.fetchUnitsFromDB();
				sensor.setReadings(null);
			}
		}
	}

	public Sensor getSelectedSensor() {
		return selectedSensor;
	}

	public void setSelectedSensor(Sensor selectedSensor) {
		this.selectedSensor = selectedSensor;
	}

	public void fetchSensorReadingsByDate(String startDate, String startTime, String endDate, String endTime,
			Sensor sensor) {

		this.setStartDate(startDate);
		this.setStartTime(startTime);
		this.setEndDate(endDate);
		this.setEndTime(endTime);

		if (this.startDate == null || this.startTime == null || this.endDate == null || this.endTime == null) {
			// Do nothing
		} else {
			startDate = parseDate(String.format("%s %s", this.getStartDate(), this.getStartTime()));
			endDate = parseDate(String.format("%s %s", this.getEndDate(), this.getEndTime()));
			sensor.fetchReadingsFromDB(startDate, endDate);
		}
	}

	public String updateNickname(String nickname) {
		String output = "null";
		this.getInfo().put("nickname", nickname);
		output = MachineDB.putNickname(this);
		return output;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	private String parseDate(String string) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			Date date = df.parse(string);
			String dateString = sf.format(date);
			return dateString;
		} catch (ParseException e) {
			return "";
		}
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public TreeMap<String, String> getLightColors() {
		return lightColors;
	}

	public void setLightColors(TreeMap<String, String> lightColors) {
		this.lightColors = lightColors;
	}

	public String[] setWaterInValve(String value) {

		String[] output = new String[2];
		output[0] = "0";
		output[1] = "null"; // when no message is necessary

		if (value.equalsIgnoreCase("0")) {
			// initialized values are ok
		} else {
			// value = "1"; make sure water level is ok to open it
			int waterLevel = 0;

			try {
				waterLevel = Integer.parseInt(this.getReadings().get("water_level1"));
				if (waterLevel < 4) {
					output[0] = "1"; // message is still ok, but need to change value
				} else {
					// value is still ok, but need to change message
					output[1] = "waterLevelHighMessage";
				}
			} catch (Exception e) {
				// number was invalid, so we need to return that message
				output[1] = "invalidValueMessage";
			}
		}
		return output;
	}

	public boolean userIsAuthorized(User user) {
		boolean isAuth = false;
		if (user != null && user.getId() != null) {
			isAuth = this.userIsAuthorized(user.getId());
		}
		return isAuth;
	}

	public boolean userIsAuthorized(String userId) {
		boolean output = false;

		if (userId != null && this.getAuthorizedUsers() != null && this.getAuthorizedUsers().size() > 0) {
			for (User authorizedUser : this.getAuthorizedUsers()) {
				if (authorizedUser.getId().equalsIgnoreCase(userId)) {
					output = true;
					break;
				}
			}
		}
		return output;
	}

	public String addAuthorizationByUUID(String uuid) {
		boolean success = MachineDB.addAuthorization(uuid, this);
		String message = null;

		if (success) {
			message = "addAuthSuccessMessage";
		} else {
			message = "addAuthFailedMessage";
		}
		return message;
	}

	public String removeAuthorizationByUUID(String uuid) {
		boolean success = MachineDB.removeAuthorization(uuid, this);
		String message = null;

		if (success) {
			message = "removeAuthSuccessMessage";
		} else {
			message = "removeAuthFailedMessage";
		}
		return message;
	}

	public String transferOwnership(String newOwnerId) {
		String message = "null";

		// Check that new owner is already authorized
		if (this.userIsAuthorized(newOwnerId)) {
			// if so, can change
			message = MachineDB.changeOwner(this, newOwnerId);
			this.fetchInfoFromDB();
			this.fetchAuthorizedUsersFromDB();
		} else {
			// otherwise, cannot
			message = "userMustBeAuthorizedToTransferOwnership";
		}
		return message;
	}

	public String getOwnerEmail() {
		if (this.getInfo() != null) {
			return this.getInfo().get("owner_email");
		}
		return null;
	}

	public void reduceReadings(int max) {
		int count = 0;
		if (this.getSensors() != null && this.getSensors().size() > 0) {
			for (String key : this.getSensors().keySet()) {
				Sensor sensor = this.getSensors().get(key);
				if (sensor != null && sensor.getReadings() != null) {
					TreeSet<Message> readings = sensor.getReadings();
					float maxSize = (float) max;
					if (readings.size() > maxSize) {
						TreeSet<Message> temp = new TreeSet<Message>();

						float rate = maxSize / readings.size();
						Random ran = new Random();
						for (Message message : readings) {
							count++;
							if (ran.nextFloat() < rate) {
								temp.add(message);
							}
						}
						sensor.setReadings(temp);
					}
				}
			}
		}
		System.out.printf("Performed %d operations reducing the size of the tree sets\n", count);
	}

	public void clearImages() {
		this.setImageNames(null);
	}

	public void clearReadings() {
		for (String key : this.getSensors().keySet()) {
			Sensor sensor = this.getSensors().get(key);
			sensor.clearReadings();
		}
	}

}
