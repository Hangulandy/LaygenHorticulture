package com.laygen.database;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.laygen.beans.Authorization;
import com.laygen.beans.Machine;
import com.laygen.beans.Message;
import com.laygen.beans.User;

public class UserDB {

	public static String insert(User user) {
		String message = "cannotCreateAccountMessage";

		if (emailAvailable(user.getEmail())) {
			Connection conn = DBConnection.getInstance().getConnection();
			try (Table table = conn.getTable(DBConnection.getTableName())) {
				byte[] cf = Bytes.toBytes("C");
				Put put = new Put(Bytes.toBytes(user.getId()));
				put.addColumn(cf, Bytes.toBytes("email"), Bytes.toBytes(user.getEmail()));
				put.addColumn(cf, Bytes.toBytes("name"), Bytes.toBytes(user.getName()));
				put.addColumn(cf, Bytes.toBytes("username"), Bytes.toBytes(user.getUsername()));
				put.addColumn(cf, Bytes.toBytes("password"), Bytes.toBytes(user.getPassword()));
				put.addColumn(cf, Bytes.toBytes("organization"), Bytes.toBytes(user.getOrganization()));
				table.put(put);

				put = new Put(Bytes.toBytes(user.getEmail()));
				put.addColumn(cf, Bytes.toBytes("UUID"), Bytes.toBytes(user.getId()));
				table.put(put);

				message = "userAccountCreated";
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			message = "emailNotAvailableMessage";
		}
		user.setPassword(null);
		return message;
	}

	private static boolean emailAvailable(String email) {
		TreeSet<Message> messages = MessageDB.getRowById(email);
		return messages.size() == 0 ? true : false;
	}

	public static String login(User user) {
		String message = "userNotFound";
		User userFromDB = null;
		// First, get the ID using email
		String uuid = fetchUUIDByEmail(user.getEmail());
		
		// If a uuid comes back (i.e. email is in the db), get the user data by uuid
		if (uuid != null) {
			userFromDB = fetchCompleteUserByUUID(uuid);
			// if a user object came back, check password
			if (userFromDB != null) {
				if (userFromDB.getPassword().equalsIgnoreCase(user.getPassword())) {
					userFromDB.setLoggedIn(true);
					message = "null";
				} else {
					message = "wrongPassword";
					userFromDB.setLoggedIn(false);
				}
				userFromDB.setPassword(null);
			}
			user.setId(userFromDB.getId());
			user.setEmail(userFromDB.getEmail());
			user.setName(userFromDB.getName());
			user.setUsername(userFromDB.getUsername());
			user.setOrganization(userFromDB.getOrganization());
			user.setLoggedIn(userFromDB.isLoggedIn());
			user.setPassword(null);
		} else {
			user = null;
		}

		return message;
	}

	public static String fetchUUIDByEmail(String email) {
		String output = null;
		TreeSet<Message> messages = MessageDB.getRowById(email);
		if (messages.size() > 0) {
			for (Message message : messages) {
				if (message.getColumnFamily().equalsIgnoreCase("C")
						&& message.getColumnName().equalsIgnoreCase("uuid")) {
					output = message.getValue();
					break;
				}
			}
		}
		return output;
	}

	public static User fetchUserByUUID(String uuid) {
		User user = fetchCompleteUserByUUID(uuid);
		if (user != null) {
			user.setPassword(null);
		}
		return user;
	}

	private static User fetchCompleteUserByUUID(String uuid) {
		User user = null;

		if (uuid != null) {
			TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily(uuid, "C");

			if (messages.size() > 0) {
				user = new User();
				user.setId(uuid);
				for (Message message : messages) {
					if (message.getColumnName().equalsIgnoreCase("email")) {
						user.setEmail(message.getValue());
					}
					if (message.getColumnName().equalsIgnoreCase("name")) {
						user.setName(message.getValue());
					}
					if (message.getColumnName().equalsIgnoreCase("username")) {
						user.setUsername(message.getValue());
					}
					if (message.getColumnName().equalsIgnoreCase("organization")) {
						user.setOrganization(message.getValue());
					}
					if (message.getColumnName().equalsIgnoreCase("password")) {
						user.setPassword(message.getValue());
					}
				}
			}
		}
		return user;
	}

	public static TreeSet<Authorization> fetchAuthorizationsForUser(User user) {
		return AuthorizationDB.getUserAuthorizations(user);
	}

	public static String registerMachine(User user, String serialNumber, String registrationKey) {
		// TODO Auto-generated method stub
		String message = "null";

		Machine machine = new Machine();
		machine.setSerialNumber(serialNumber);
		machine.fetchInfoFromDB();

		if (machine.getInfo() != null && machine.getInfo().get("registration_key") != null
				&& machine.getInfo().get("registration_key").equalsIgnoreCase(registrationKey)) {
			message = MachineDB.changeOwner(machine, user.getId());
		} else {
			message = "invalidMachineMessage";
		}
		return message;
	}

}
