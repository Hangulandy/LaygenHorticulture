package com.laygen.database;

import java.util.TreeSet;

import com.laygen.beans.Authorization;
import com.laygen.beans.Message;
import com.laygen.beans.User;

public class AuthorizationDB {

	public static TreeSet<Authorization> getUserAuthorizations(User user) {
		TreeSet<Authorization> output = null;
		TreeSet<Message> messages = MessageDB.scanColumnFamilyWithRowPrefix("C", "sn", user.getId() + "-U-");
		
		if (messages.size() > 0) {
			output = new TreeSet<Authorization>();
			Authorization auth;
			for (Message message : messages) {
				auth = new Authorization();
				auth.setMachineSerialNumber(message.getValue());
				auth.setUserId(user.getId());
				output.add(auth);
			}
		}
		return output;
	}

}
