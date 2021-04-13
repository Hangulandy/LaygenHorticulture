package com.laygen.database;

import java.util.TreeSet;

import com.laygen.beans.Authorization;
import com.laygen.beans.Machine;
import com.laygen.beans.Message;
import com.laygen.beans.User;

public class AuthorizationDB {

	public static TreeSet<Authorization> getUserAuthorizations(User user) {
		TreeSet<Authorization> output = null;
		String userId = user.getId() != null ? user.getId() : "";
		TreeSet<Message> messages = MessageDB.scanColumnFamilyWithRowPrefix("C", "sn", userId + "-U-");

		if (messages.size() > 0) {
			output = new TreeSet<Authorization>();
			Authorization auth;
			for (Message message : messages) {
				auth = new Authorization();
				auth.setMachineSerialNumber(message.getValue());
				auth.setUserId(user.getId());
				output.add(auth);
			}

			Machine machine = null;
			for (Authorization item : output) {
				machine = new Machine();
				machine.setInfo(MachineDB.getMachineCurrentInfoBySerialNumber(item.getMachineSerialNumber()));
				item.setMachineNickname(machine.getInfo().get("nickname"));
				item.setOwnerEmail(machine.getInfo().get("owner_email"));
			}
		}
		return output;
	}

}
