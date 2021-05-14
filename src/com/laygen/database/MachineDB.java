package com.laygen.database;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.laygen.beans.Sensor;
import com.laygen.beans.User;
import com.laygen.beans.Machine;
import com.laygen.beans.Message;

public class MachineDB {

	static Charset enc = StandardCharsets.UTF_8;

	public static HashMap<String, String> fetchMachineInfoBySerialNumber(String serialNumber) {
		return fetchGroupBySerialNumber(serialNumber, "C");
	}

	public static HashMap<String, String> fetchCurrentSettingsBySerialNumber(String serialNumber) {
		return fetchGroupBySerialNumber(serialNumber, "S");
	}

	public static HashMap<String, String> fetchCurrentReadingsBySerialNumber(String serialNumber) {
		return fetchGroupBySerialNumber(serialNumber, "R");
	}

	public static HashMap<String, String> fetchGroupBySerialNumber(String serialNumber, String group) {
		HashMap<String, String> map = null;
		TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily(serialNumber, group);
		if (messages != null && messages.size() > 0) {
			map = new HashMap<String, String>();
			for (Message message : messages) {
				String value = message.getValue().equalsIgnoreCase("") ? "0" : message.getValue();
				map.put(message.getColumnName(), value);
			}
		}
		return map;
	}

	public static TreeSet<Message> fetchReadingsForMachine(String serialNumber) {
		return MessageDB.scanColumnFamilyWithRowPrefix("R", null, serialNumber);
	}

	public static Map<String, String> fetchImageNamesForMachine(String serialNumber) {
		TreeSet<Message> messages = MessageDB.scanColumnFamilyWithRowPrefix("I", "name", serialNumber + "-I-");
		HashMap<String, String> images = new HashMap<String, String>();

		for (Message message : messages) {
			images.put(message.getRowId(), message.getValue());
		}
		return images;
	}

	public static byte[] fetchImageBytesById(String imageId) {
		return MessageDB.getByteValue(imageId, "I", "data");
	}

	public static void deleteImage(String imageId) {
		MessageDB.deleteValue(imageId, "I", "name");
		MessageDB.deleteValue(imageId, "I", "data");
	}

	public static TreeMap<String, Sensor> fetchSensorList(Machine machine) {
		TreeMap<String, Sensor> sensors = null;

		if (machine.getInfo() != null && machine.getInfo().get("model_name") != null) {
			TreeSet<Message> messages = MessageDB
					.getRowMessagesByColumnFamily(machine.getInfo().get("model_name") + "-C", "C");
			if (messages.size() > 0) {
				sensors = new TreeMap<String, Sensor>();
				for (Message message : messages) {
					Sensor sensor = new Sensor();
					sensor.setMachineSerialNumber(machine.getSerialNumber());
					sensor.setName(message.getColumnName());
					sensor.setType(message.getValue());
					sensors.put(message.getColumnName(), sensor);
				}
			}
		}
		return sensors;
	}

	public static TreeMap<String, String> fetchLightColors(Machine machine) {
		TreeMap<String, String> lightColors = null;

		if (machine.getInfo() != null && machine.getInfo().get("model_name") != null) {
			TreeSet<Message> messages = MessageDB
					.getRowMessagesByColumnFamily(machine.getInfo().get("model_name") + "-L", "C");
			if (messages.size() > 0) {
				lightColors = new TreeMap<String, String>();
				for (Message message : messages) {
					lightColors.put(message.getColumnName(), message.getValue());
				}
			}
		}
		return lightColors;
	}

	public static TreeMap<String, String> fetchCustomLightColors(Machine machine) {
		TreeMap<String, String> lightColors = null;

		if (machine.getSerialNumber() != null) {
			TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily(machine.getSerialNumber() + "-L", "C");
			if (messages.size() > 0) {
				lightColors = new TreeMap<String, String>();
				for (Message message : messages) {
					lightColors.put(message.getColumnName(), message.getValue());
				}
			}
		}
		return lightColors;
	}

	public static String putNickname(Machine machine) {
		String output = "null";
		String cq = "nickname";

		boolean success = MessageDB.simplePut(machine.getSerialNumber(), "C", cq, machine.getInfo().get(cq));
		if (!success) {
			output = "unableToUpdate";
		} else {
			output = "updated";
		}
		return output;
	}

	public static TreeSet<User> fetchAuthorizedUsers(Machine machine) {
		TreeSet<User> users = new TreeSet<User>();
		TreeSet<Message> messages = MessageDB.scanColumnFamilyWithRowPrefix("C", "uuid",
				machine.getSerialNumber() + "-U");

		User user = null;
		for (Message message : messages) {
			user = UserDB.fetchUserByUUID(message.getValue());
			if (user != null) {
				users.add(user);
			}
		}
		return users;
	}

	public static boolean addAuthorization(String uuid, Machine machine) {
		boolean success = false;
		String rowId = String.format("%s-U-%s", machine.getSerialNumber(), uuid);
		String cf = "C";
		String cq = "uuid";

		success = MessageDB.simplePut(rowId, cf, cq, uuid);
		if (success) {
			rowId = String.format("%s-U-%s", uuid, machine.getSerialNumber());
			cq = "sn";
			success = MessageDB.simplePut(rowId, cf, cq, machine.getSerialNumber());
		}
		return success;
	}

	public static boolean removeAuthorization(String uuid, Machine machine) {
		boolean success = false;
		String rowId = String.format("%s-U-%s", machine.getSerialNumber(), uuid);
		String cf = "C";
		String cq = "uuid";

		success = MessageDB.deleteValue(rowId, cf, cq);
		if (success) {
			rowId = String.format("%s-U-%s", uuid, machine.getSerialNumber());
			cq = "sn";
			success = MessageDB.deleteValue(rowId, cf, cq);
		}
		return success;
	}

	public static String changeOwner(Machine machine, String newOwnerId) {
		String message = "null";

		// First, fetch the user
		User newOwner = UserDB.fetchUserByUUID(newOwnerId);
		if (newOwner != null) {
			// If it is a valid user...
			try {
				// attempt to change the owner email on the machine
				String rowId = machine.getSerialNumber();
				String cf = "C";
				String cq = "owner_email";
				String value = newOwner.getEmail();
				boolean success = MessageDB.simplePut(rowId, cf, cq, value);
				
				cq = "registration_key";
				MessageDB.deleteValue(rowId, cf, cq);
				
				rowId = String.format("%s-U-%s", machine.getSerialNumber(), newOwner.getId());
				cq = "uuid";
				value = newOwner.getId();
				MessageDB.simplePut(rowId, cf, cq, value);
				
				rowId = String.format("%s-U-%s", newOwner.getId(), machine.getSerialNumber());
				cq = "sn";
				value = machine.getSerialNumber();
				MessageDB.simplePut(rowId, cf, cq, value);

				rowId = machine.getSerialNumber();
				cq = "organization";
				value = newOwner.getOrganization();
				MessageDB.simplePut(rowId, cf, cq, value);
				
				// and set the result message
				if (success) {
					message = "success";
					
				} else {
					message = "failed";
				}
			} catch (Exception e) {
				// return connection exception message if there was a problem with the database
				message = "databaseErrorMessage";
			}

		} else {
			// Otherwise, return cannot find user message
			message = "cannotFindUserMessage";
		}
		return message;
	}

}
