package com.laygen.beans;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import com.laygen.database.Dictionary;
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
	private String image;
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

	public void refreshAllFromDB() {
		refreshInfoFromDB();
		refreshCurrentReadingsFromDB();
		refreshSettingsFromDB();
		refreshLightColorsFromDB();
		refreshSensorsFromDB();
	}

	public void refreshInfoFromDB() {
		setInfo(null);
		setInfo(MachineDB.getMachineCurrentInfoBySerialNumber(getSerialNumber()));
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public void refreshSettingsFromDB() {
		setSettings(MachineDB.getCurrentSettingsBySerialNumber(getSerialNumber()));
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

	public TreeSet<User> getAuthorizedUsers() {
		return authorizedUsers;
	}

	public void setAuthorizedUsers(TreeSet<User> authorizedUsers) {
		this.authorizedUsers = authorizedUsers;
	}

	public String updateMachineSettings(TreeMap<String, String> newSettings, String lang) {
		String message = "";

		// get port as integer
		try {
			int port = Integer.parseInt(this.getInfo().get("port"));

			// establish socket
			try (Socket socket = new Socket(this.getInfo().get("ip"), port)) {

				// establish printwriter
				try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					String msg;
					System.out.println();
					System.out.println("Messages sent to Machine: ");
					for (String key : newSettings.keySet()) {
						msg = String.format("%s#%s", newSettings.get(key), key);
						out.println(msg);
						this.getSettings().put(key, newSettings.get(key));
						System.out.println(msg);
					}
					System.out.println();
					message = message + Dictionary.getInstance().get("success");
				}
			}
		} catch (Exception e) {
			message = message + Dictionary.getInstance().get("commError");
		}
		return message;
	}

	public String takePicture(String lang) {
		// refreshTime();
		return sendCommandToMachine("1#flash_on", lang);
	}

	public String generateTimeCommand() {
		Date currentTime = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timeString = sdf.format(currentTime);
		String msg = String.format("%s#time", timeString);

		return msg;
	}

	public String refreshTime(String lang) {
		return sendCommandToMachine(generateTimeCommand(), lang);
	}

	public String sendCommandToMachine(String msg, String lang) {
		System.out.println(String.format("Attempting to send message to machine %s : %s", this.getSerialNumber(), msg));

		// get port as integer
		try {
			int port = Integer.parseInt(this.getInfo().get("port"));

			// establish socket
			try (Socket socket = new Socket(this.getInfo().get("ip"), port)) {

				// establish printwriter
				try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					System.out
							.println(String.format("Sending message to machine %s : %s", this.getSerialNumber(), msg));
					out.println(msg);
				}
				return Dictionary.getInstance().get("success", lang);
			}
		} catch (Exception e) {
			return Dictionary.getInstance().get("commError", lang);
		}
	}

	public String sendOpenValveMessage(String lang) {
		String result = null;
		// check for current value of water level
		this.refreshCurrentReadingsFromDB();
		if (this.getReadings().get("water_level1") != null) {
			String waterLevel = this.getReadings().get("water_level1");
			try {
				int wl = Integer.parseInt(waterLevel);
				if (wl <= 4) {
					result = this.sendCommandToMachine("1#water_in_valve_on", lang);
				} else {
					result = Dictionary.getInstance().get("waterLevelHighMessage", lang);
				}
			} catch (Exception e) {
				result = Dictionary.getInstance().get("invalidValueMessage", lang);
			}
		} else {
			result = Dictionary.getInstance().get("invalidValueMessage", lang);
		}
		return result;
	}

	public void refreshCurrentReadingsFromDB() {
		setReadings(MachineDB.getCurrentReadingsBySerialNumber(getSerialNumber()));
	}

	public Map<String, String> getReadings() {
		return readings;
	}

	public void setReadings(Map<String, String> readings) {
		this.readings = readings;
	}

	public void refreshImages() {
		TreeMap<String, String> map = new TreeMap<String, String>(Collections.reverseOrder());
		Map<String, String> otherMap = MachineDB.getImageNamesForMachine(this.getSerialNumber());
		for (String key : otherMap.keySet()) {
			map.put(key, otherMap.get(key));
		}
		setImageNames(map);
	}

	public String getImage() {
		if (this.getImageNames().size() > 0) {
			return image;
		} else {
			return null;
		}
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void fetchImage(String imageId) {
		this.setImage(Base64.getEncoder().encodeToString(MachineDB.fetchImageBytesById(imageId)));
	}

	public String deleteImage(String imageId) {
		if (imageId != null) {
			// delete the image from the DB
			MachineDB.deleteImage(imageId);
			// remove the image from the TreeMap
			this.getImageNames().remove(imageId);
		}
		// Set the fetched image to the first in the map no matter what imageId was
		// passed in
		if (this.getImageNames().size() > 0) {
			this.fetchImage(this.getImageNames().firstKey());
			return this.getImageNames().firstKey();
		} else {
			return null;
		}
	}

	private void refreshLightColorsFromDB() {
		TreeMap<String, String> colors = MachineDB.getLightColors(this);
		TreeMap<String, String> newColors = MachineDB.getCustomLightColors(this);
		if (newColors != null) {
			colors.putAll(newColors);			
		}
		setLightColors(colors);
	}

	public void refreshSensorsFromDB() {
		setSensors(MachineDB.getSensorList(this));

		Sensor sensor = null;
		for (String key : this.getSensors().keySet()) {
			sensor = this.getSensors().get(key);
			sensor.fetchUnitsFromDB();
			sensor.setReadings(null);
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
		String output = null;
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

	public String[] setWaterInValve(String value, String lang) {

		String[] output = new String[2];
		output[0] = "0";
		output[1] = ""; // when no message is necessary

		if (value.equalsIgnoreCase("0")) {
			// initialized values are ok
		} else  {
			// value = "1"; make sure water level is ok to open it
			int waterLevel = 0;

			try {
				waterLevel = Integer.parseInt(this.getReadings().get("water_level1"));
				if (waterLevel < 4) {
					output[0] = "1"; // message is still ok, but need to change value
				} else {
					// value is still ok, but need to change message
					output[1] = Dictionary.getInstance().get("waterLevelHighMessage", lang); 					
				}
			} catch (Exception e) {
				// number was invalid, so we need to return that message
				output[1] = Dictionary.getInstance().get("invalidValueMessage", lang); 
			}
		}
		return output;
	}

}
