package com.laygen.beans;

public class Authorization implements Comparable<Authorization> {

	private String userId;
	private String machineSerialNumber;
	private String machineNickname;
	private String ownerEmail;
	private String privilegeLevel;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMachineSerialNumber() {
		return machineSerialNumber;
	}

	public void setMachineSerialNumber(String machineSerialNumber) {
		this.machineSerialNumber = machineSerialNumber;
	}

	public String getMachineNickname() {
		return machineNickname;
	}

	public void setMachineNickname(String machineNickname) {
		this.machineNickname = machineNickname;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getPrivilegeLevel() {
		return privilegeLevel;
	}

	public void setPrivilegeLevel(String privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}

	@Override
	public int compareTo(Authorization other) {
		return this.getMachineSerialNumber().compareTo(other.getMachineSerialNumber());
	}

	public String toString() {
		return String.format("Authorization for %s on machine %s", this.getUserId(), this.getMachineSerialNumber());
	}
}
