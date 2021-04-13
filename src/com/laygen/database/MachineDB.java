package com.laygen.database;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.laygen.beans.Sensor;
import com.laygen.beans.Machine;
import com.laygen.beans.Message;

public class MachineDB {

	static Charset enc = StandardCharsets.UTF_8;

	public static HashMap<String, String> getMachineCurrentInfoBySerialNumber(String serialNumber) {
		return getGroupBySerialNumber(serialNumber, "C");
	}

	public static HashMap<String, String> getCurrentSettingsBySerialNumber(String serialNumber) {
		return getGroupBySerialNumber(serialNumber, "S");
	}

	public static HashMap<String, String> getCurrentReadingsBySerialNumber(String serialNumber) {
		return getGroupBySerialNumber(serialNumber, "R");
	}

	public static HashMap<String, String> getGroupBySerialNumber(String serialNumber, String group) {
		HashMap<String, String> map = null;
		TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily(serialNumber, group);
		if (messages != null) {
			map = new HashMap<String, String>();
			for (Message message : messages) {
				map.put(message.getColumnName(), message.getValue());
			}
		}
		return map;
	}

	public static TreeSet<Message> getReadingsForMachine(String serialNumber) {
		return MessageDB.scanColumnFamilyWithRowPrefix("R", null, serialNumber);
	}

	public static Map<String, String> getImageNamesForMachine(String serialNumber) {
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

	public static TreeMap<String, Sensor> getSensorList(Machine machine) {
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

	public static String putNickname(Machine machine) {
		String output = null;
		String cq = "nickname";

		boolean success = MessageDB.simplePut(machine.getSerialNumber(), "C", cq, machine.getInfo().get(cq));

		if (!success) {
			output = Dictionary.getInstance().get("unableToUpdate");
		} else {
			output = Dictionary.getInstance().get("updated");
		}

		return output;
	}

}
