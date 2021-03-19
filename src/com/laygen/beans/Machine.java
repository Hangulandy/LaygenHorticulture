package com.laygen.beans;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.laygen.database.MachineDB;

public class Machine {

	private String serialNumber;
	private Map<String, String> info; // key is the column qualifier and value is the value; i.e. key: location,
										// value: Laygen office
	private Map<String, String> settings; // key is the column qualifier and value is the value; i.e. key: brightness,
											// value: 50
	private TreeSet<Component> components;
	private List<String> imageNames;
	private TreeSet<User> authorizedUsers;
	private Socket socket;
	private PrintWriter out;

	public Machine() {
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public void refreshInfoFromDB() {
		setInfo(MachineDB.getMachineInfoBySerialNumber(getSerialNumber()));
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}
	
	public void refreshSettingsFromDB() {
		setSettings(MachineDB.getMachineSettingsBySerialNumber(getSerialNumber()));
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

	public List<String> getImageNames() {
		return imageNames;
	}

	public void setImageNames(List<String> imageNames) {
		this.imageNames = imageNames;
	}

	public TreeSet<User> getAuthorizedUsers() {
		return authorizedUsers;
	}

	public void setAuthorizedUsers(TreeSet<User> authorizedUsers) {
		this.authorizedUsers = authorizedUsers;
	}

	public String updateMachineSettings(HashMap<String, String> newSettings) {
		// TODO - perhaps this should be asynchronous in the future?
		String portString = this.getInfo().get("port");
		
		this.refreshInfoFromDB();
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
				if (this.getSettings().get(key).equalsIgnoreCase(newSettings.get(key))) {
					// no need to send an update to the device
				} else {
					// update the current setting that is different
					this.getSettings().put(key, newSettings.get(key));
					
					// need to send an update for just that setting to the device
					msg = String.format("%s#%s", this.getSettings().get(key), key);
					System.out.println(msg);
					out.println(msg);
				}
			}
			out.close();
			socket.close();
			return "Success";
		} catch (Exception e) {
			return "Cannot establish a communication socket with that machine";
		}
	}

	public void refreshAllFromDB() {
		this.refreshInfoFromDB();
		this.refreshSettingsFromDB();
	}

}
