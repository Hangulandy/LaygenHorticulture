package com.laygen.database;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.TreeSet;

import com.laygen.beans.Message;

public class MachineDB {
	
	static Charset enc = StandardCharsets.UTF_8;
	
	public static HashMap<String, String> getMachineInfoBySerialNumber(String serialNumber) {
		
		HashMap<String, String> map = null;
		TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily(serialNumber, "I");		
		if (messages != null) {
			map = new HashMap<String, String>();
			for (Message message : messages) {
				map.put(message.getColumnName(), message.getValue());
			}
		}
		return map;
	}

	public static HashMap<String, String> getMachineSettingsBySerialNumber(String serialNumber) {
		
		HashMap<String, String> map = null;
		TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily(serialNumber, "S");		
		if (messages != null) {
			map = new HashMap<String, String>();
			for (Message message : messages) {
				map.put(message.getColumnName(), message.getValue());
			}
		}
		return map;
	}
	
	public static TreeSet<Message> getReadingsForMachine(String serialNumber) {
		return MessageDB.scanColumnFamilyWithRowPrefix("R", serialNumber);
	}
	
}
