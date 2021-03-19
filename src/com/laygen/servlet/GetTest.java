package com.laygen.servlet;

import java.util.TreeSet;

import com.laygen.beans.Message;
import com.laygen.database.MessageDB;

public class GetTest {
	
	public static void main(String[] args) {
		
		TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily("0123456789", "S");
		
		for (Message message : messages) {
			System.out.println(message.toString());
		}
		
	}

}
