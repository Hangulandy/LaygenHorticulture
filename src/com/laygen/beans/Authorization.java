package com.laygen.beans;

public class Authorization {

	private String userId;
	private String machineSerialNumber;
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

	public String getPrivilegeLevel() {
		return privilegeLevel;
	}

	public void setPrivilegeLevel(String privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}

}
