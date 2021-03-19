package com.laygen.database;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.TreeSet;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import com.laygen.beans.Message;

public class MessageDB {

	static Charset enc = StandardCharsets.UTF_8;

	public static TreeSet<Message> scanColumnFamily(String columnFamily){
		return scanColumnFamilyWithRowPrefix(null, columnFamily);
	}
	
	public static TreeSet<Message> scanColumnFamilyWithRowPrefix(String columnFamily, String rowPrefix) {
		Connection conn = DBConnection.getInstance().getConnection();
		String tableName = DBConnection.getTableName();
		TreeSet<Message> messages = new TreeSet<Message>();

		try (Table table = conn.getTable(TableName.valueOf(tableName))) {
			Scan scan = new Scan();
			if (columnFamily != null) {
				scan.addFamily(Bytes.toBytes(columnFamily));
			}
			if (rowPrefix != null) {
				scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));
			}
			ResultScanner scanner = table.getScanner(scan);
			for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
				messages.addAll(buildMessagesFromResult(rr));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messages;
	}

	public static TreeSet<Message> getRowMessagesByColumnFamily(String rowId, String columnFamily) {
		Connection conn = DBConnection.getInstance().getConnection();
		String tableName = DBConnection.getTableName();
		TreeSet<Message> messages = new TreeSet<Message>();

		try (Table table = conn.getTable(TableName.valueOf(tableName))) {
			Get get = new Get(Bytes.toBytes(rowId));
			if (columnFamily != null) {
				get.addFamily(Bytes.toBytes(columnFamily));
			}
			Result rr = table.get(get);
			messages.addAll(buildMessagesFromResult(rr));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messages;
	}

	private static TreeSet<Message> buildMessagesFromResult(Result rr) {

		TreeSet<Message> messages = new TreeSet<Message>();

		// this should be the complete row map
		NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = rr.getMap();

		// key should be the column family
		if (map != null) {
			for (byte[] key : map.keySet()) {
				// here we want to use this key to get the family maps and parse them
				// individually
				messages.addAll(parseResultToMessagesUsingFamily(key, rr));
			}
		}
		return messages;
	}

	private static TreeSet<Message> parseResultToMessagesUsingFamily(byte[] columnFamilyIn, Result rr) {

		TreeSet<Message> messages = new TreeSet<Message>();
		String rowId = Bytes.toString(rr.getRow());
		String columnFamily = Bytes.toString(columnFamilyIn);

		NavigableMap<byte[], byte[]> map = rr.getFamilyMap(columnFamilyIn);

		for (byte[] key : map.keySet()) {
			String columnName = Bytes.toString(key);
			String value = Bytes.toString(map.get(key));

			Message message = new Message();
			message.setColumnFamily(columnFamily);
			message.setRowId(rowId);
			message.setColumnName(columnName);
			message.setValue(value);

			// System.out.println(message.toString());
			messages.add(message);
		}
		return messages;
	}

	public static TreeSet<Message> getRowById(String rowId) {
		return getRowMessagesByColumnFamily(rowId, null);
	}

	public static TreeSet<Message> getColumnFamilies() {

		TreeSet<Message> messages = new TreeSet<Message>();
		for (char c = 'A'; c <= 'Z'; c++) {
			messages.addAll(getRowById(String.valueOf(c)));
		}
		return messages;
	}

	public static HashMap<String, String> getMessageToCodeDictionary() {

		HashMap<String, String> map = null;
		TreeSet<Message> messages = MessageDB.getRowMessagesByColumnFamily("message_codes", "Z");
		if (messages != null) {
			map = new HashMap<String, String>();
			for (Message message : messages) {
				map.put(message.getValue(), message.getColumnName());
			}
		}
		return map;
	}

}
