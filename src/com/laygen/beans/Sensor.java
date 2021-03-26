package com.laygen.beans;

import java.util.TreeSet;

import com.laygen.database.MessageDB;

public class Sensor implements Comparable<Sensor> {

	private String machineSerialNumber;
	private String name;
	private String type;
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

	public void refreshReadingsFromDB() {
		this.setReadings(MessageDB.scanColumnFamilyWithRowPrefix("R", this.getType(),
				String.format("%s-%s-", this.getMachineSerialNumber(), this.getName())));
	}

	@Override
	public int compareTo(Sensor that) {
		if (this.getMachineSerialNumber() != that.getMachineSerialNumber()) {
			return this.getMachineSerialNumber().compareTo(that.getMachineSerialNumber());
		}
		return this.getName().compareTo(that.getName());
	}
}