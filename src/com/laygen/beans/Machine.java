package com.laygen.beans;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.laygen.database.MachineDB;

public class Machine {

	private String serialNumber;
	private Map<String, String> info;
	private Map<String, String> settings;
	private TreeSet<Component> components;
	private TreeMap<String, String> images;
	private TreeSet<User> authorizedUsers;
	private Socket socket;
	private PrintWriter out;
	private String image;

	public Machine() {
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void refreshInfoFromDB() {
		setInfo(MachineDB.getMachineCurrentInfoBySerialNumber(getSerialNumber()));
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public TreeSet<Component> getComponents() {
		return components;
	}

	public void setComponents(TreeSet<Component> components) {
		this.components = components;
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

	public String updateMachineSettings(HashMap<String, String> newSettings) {
		// TODO - perhaps this should be asynchronous in the future?

		this.refreshAllFromDB();
		String portString = this.getInfo().get("port");

		int port = 0;

		if (portString != null) {
			try {
				port = Integer.parseInt(portString);
			} catch (Exception e) {
				return "No valid port number to send a message to that machine";
			}
		}

		try {
			socket = new Socket(this.getInfo().get("ip"), port);
			out = new PrintWriter(socket.getOutputStream(), true);
			String msg;
			for (String key : this.getSettings().keySet()) {
				msg = String.format("%s#%s", newSettings.get(key), key);
				out.println(msg);
				this.getSettings().put(key, newSettings.get(key));
				System.out.println(msg);
			}
			out.close();
			socket.close();
			return "Success";
		} catch (Exception e) {
			return "There was a communication error with that machine. Check that it is turned on and try again.";
		}
	}

	public String takePicture() {
		// TODO - perhaps this should be asynchronous in the future?

		String portString = this.getInfo().get("port");

		int port = 0;

		if (portString != null) {
			try {
				port = Integer.parseInt(portString);
			} catch (Exception e) {
				return "No valid port number to send a command to that machine";
			}
		}

		try {
			socket = new Socket(this.getInfo().get("ip"), port);
			out = new PrintWriter(socket.getOutputStream(), true);
			String msg = "1#flash";
			System.out.println(msg);
			out.println(msg);
			out.close();
			socket.close();
			return "Success";
		} catch (Exception e) {
			return "There was a communication error with that machine. Check that it is turned on and try again.";
		}
	}

	public void refreshAllFromDB() {
		refreshInfoFromDB();
		refreshSettingsFromDB();
	}

	public void refreshSettingsFromDB() {
		setSettings(MachineDB.getCurrentSettingsBySerialNumber(getSerialNumber()));
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
		return image;
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
		// Set the fetched image to the first in the map no matter what imageId was passed in
		if (this.getImageNames().size() > 0) {
			this.fetchImage(this.getImageNames().firstKey());
			return this.getImageNames().firstKey();			
		} else {
			return null;
		}
	}

}
