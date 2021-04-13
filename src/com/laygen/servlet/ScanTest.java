package com.laygen.servlet;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.laygen.beans.Message;
import com.laygen.database.MessageDB;

public class ScanTest {

	public static String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}

	public static String convertToCSV(String[] data) {
		return Stream.of(data).map(ScanTest::escapeSpecialCharacters).collect(Collectors.joining(","));
	}

	public static void main(String[] args) {

		String sensor = "water_level1";

		TreeSet<Message> messages = MessageDB.scanColumnFamilyWithRowPrefix("R", "water_level",
				"9876543210-" + sensor + "-");

		System.out.println("Messages size is " + messages.size());

		List<String[]> dataLines = new ArrayList<>();
		for (Message message : messages) {
			String row = message.getRowId() != null ? message.getRowId() : "";
			String cf = message.getColumnFamily() != null ? message.getColumnFamily() : "";
			String cq = message.getColumnName() != null ? message.getColumnName() : "";
			String ts = message.getTimestamp() != null ? message.getTimestamp() : "";
			String time = message.getTime() != null ? message.getTime() : "";
			String value = message.getValue() != null ? message.getValue() : "";
			dataLines.add(new String[] { row, cf, cq, ts, time , value});
		}

		File csvOutputFile = new File("data.csv");

		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream().map(ScanTest::convertToCSV).forEach(pw::println);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assertTrue(csvOutputFile.exists());
	}

}
