package com.laygen.beans;

import java.util.TreeSet;

import com.laygen.database.MessageDB;

public class Sensor implements Comparable<Sensor> {

	private String machineSerialNumber;
	private String name;
	private String type;
	private String units;
	private TreeSet<Message> readings;

	public String getMachineSerialNumber() {
		return machineSerialNumber;
	}

	public void setMachineSerialNumber(String machineSerialNumber) {
		this.machineSerialNumber = machineSerialNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TreeSet<Message> getReadings() {
		return readings;
	}

	public void setReadings(TreeSet<Message> readings) {
		this.readings = readings;
	}

	public void fetchReadingsFromDB(String startDate, String endDate) {
		String startRow = String.format("%s-%s-%s", this.getMachineSerialNumber(), this.getName(), startDate);
		String stopRow = String.format("%s-%s-%s", this.getMachineSerialNumber(), this.getName(), endDate);
		this.setReadings(MessageDB.scanColumnFamilyWithRowPrefix("R", this.getType(), null, startRow, stopRow));
	}

	@Override
	public int compareTo(Sensor that) {
		if (this.getMachineSerialNumber() != that.getMachineSerialNumber()) {
			return this.getMachineSerialNumber().compareTo(that.getMachineSerialNumber());
		}
		return this.getName().compareTo(that.getName());
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public void fetchUnitsFromDB() {
		TreeSet<Message> messages = MessageDB.getRowById(this.getType());
		this.setUnits("None");
		for (Message message : messages) {
			if (message.getColumnName().equalsIgnoreCase("units")) {
				this.setUnits(message.getValue());
				break;
			}
		}
	}
}
