package com.laygen.database;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.laygen.beans.Message;
import com.laygen.beans.User;

public class UserDB {

	public static String insert(User user) {
		// output should be the message that will be passed to the view saying if the
		// insert was successful or not and why
		String output = "Unable to set up an account for that email address at this time.";

		if (emailAvailable(user.getEmail())) {
			System.out.println("Email is available");

			Connection conn = DBConnection.getInstance().getConnection();
			try (Table table = conn.getTable(TableName.valueOf(DBConnection.getTableName()))) {
				byte[] cf = Bytes.toBytes("A");
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
				
				put = new Put(Bytes.toBytes(user.getUsername()));
				put.addColumn(cf, Bytes.toBytes("UUID"), Bytes.toBytes(user.getId()));
				table.put(put);

				output = String.format("User with email address %s successfully inserted into database.",
						user.getEmail());
				System.out.println(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Email is NOT available");
		}

		return output;
	}

	private static boolean emailAvailable(String email) {	
			TreeSet<Message> messages = MessageDB.getRowById(email);
			return messages.size() == 0 ? true : false;
	}

	public static User login(String email, String password) {
		User user = null;
		
		// First, get the ID using email
		String uuid = getUUIDByEmail(email);
		
		// If a uuid comes back (i.e. email is in the db), get the user data by uuid
		if (uuid != null) {
			user = getCompleteUserByUUID(uuid);
		}
		
		// if a user object came back, check password
		if (user != null) {
			if (password.equalsIgnoreCase(user.getPassword())) {
				user.setLoggedIn(true);
			} else {
				user.setLoggedIn(false);
			}
			user.setPassword(null);
		}
		return user;
	}
	
	public static String getUUIDByEmail(String email) {
		String output = null;
		TreeSet<Message> messages = MessageDB.getRowById(email);
		if (messages.size() > 0) {
			for (Message message : messages) {
				if (message.getColumnFamily().equalsIgnoreCase("A") && message.getColumnName().equalsIgnoreCase("uuid")) {
					output = message.getValue();
					break;
				}
			}
		}
		return output;
	}
	
	public static User getUserByUUID(String uuid) {
		User user = getCompleteUserByUUID(uuid);
		if (user != null) {
			user.setPassword(null);
		}
		return user;
	}
	
	private static User getCompleteUserByUUID(String uuid) {
		User user = null;
		TreeSet<Message> messages = MessageDB.getRowById(uuid);
		
		if (messages.size() > 0) {
			user = new User();
			for (Message message : messages) {
				if (message.getColumnName().equalsIgnoreCase("uuid")) {
					user.setId(message.getValue());
				}
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
		return user;
	}

}
