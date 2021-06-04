package com.laygen.database;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeSet;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.metrics.ScanMetrics;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import com.laygen.beans.Message;

public class MessageDB {

	static Charset enc = StandardCharsets.UTF_8;

	public static TreeSet<Message> scanColumnFamily(String columnFamily) {
		return scanColumnFamilyWithRowPrefix(null, null, columnFamily);
	}
	
	public static TreeSet<Message> scanColumnFamilyWithRowPrefix(String columnFamily, String columnQualifier,
			String rowPrefix){
		return scanColumnFamilyWithRowPrefix(columnFamily, columnQualifier, rowPrefix, null, null);
	}

	@SuppressWarnings("deprecation")
	public static TreeSet<Message> scanColumnFamilyWithRowPrefix(String columnFamily, String columnQualifier,
			String rowPrefix, String startRow, String stopRow) {
		Connection conn = DBConnection.getInstance().getConnection();
		TreeSet<Message> messages = new TreeSet<Message>();
		
		try (Table table = conn.getTable(DBConnection.getTableName())) {
			Scan scan = new Scan();
			if (startRow != null) {
				scan.setStartRow(Bytes.toBytes(startRow));				
			}
			if (stopRow != null) {
				scan.setStopRow(Bytes.toBytes(stopRow));				
			}
			if (columnFamily != null) {
				byte[] cf = Bytes.toBytes(columnFamily);
				if (columnQualifier != null) {
					byte[] cq = Bytes.toBytes(columnQualifier);
					scan.addColumn(cf, cq);
				} else {
					scan.addFamily(cf);
				}
			}
			if (rowPrefix != null) {
				scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));
			}
			ResultScanner scanner = table.getScanner(scan);
			for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
				messages.addAll(buildMessagesFromResult(rr));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}

	public static TreeSet<Message> getRowMessagesByColumnFamily(String rowId, String columnFamily) {
		Connection conn = DBConnection.getInstance().getConnection();
		TreeSet<Message> messages = new TreeSet<Message>();

		try (Table table = conn.getTable(DBConnection.getTableName())) {
			Get get = new Get(Bytes.toBytes(rowId));
			if (columnFamily != null) {
				get.addFamily(Bytes.toBytes(columnFamily));
			}
			Result rr = table.get(get);
			messages.addAll(buildMessagesFromResult(rr));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messages;
	}

	public static byte[] getByteValue(String rowId, String columnFamily, String columnQualifier) {

		Connection conn = DBConnection.getInstance().getConnection();
		byte[] output = null;

		byte[] row = Bytes.toBytes(rowId);
		byte[] cf = Bytes.toBytes(columnFamily);
		byte[] cq = Bytes.toBytes(columnQualifier);

		try (Table table = conn.getTable(DBConnection.getTableName())) {
			Get get = new Get(row);
			get.addColumn(cf, cq);
			Result rr = table.get(get);
			output = rr.getFamilyMap(cf).get(cq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public static TreeSet<Message> buildMessagesFromResult(Result rr) {

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
			String value = new String(map.get(key), enc);

			Message message = new Message();
			message.setColumnFamily(columnFamily);
			message.setRowId(rowId);
			message.setColumnName(columnName);
			message.setValue(value);

			messages.add(message);
		}
		return messages;
	}

	public static TreeSet<Message> getRowById(String rowId) {
		return getRowMessagesByColumnFamily(rowId, null);
	}

	public static boolean simplePut(String row, String cf, String cq, String value) {

		byte[] rb = Bytes.toBytes(row);
		byte[] cfb = Bytes.toBytes(cf);
		byte[] cqb = Bytes.toBytes(cq);
		byte[] vb = value.getBytes(enc);

		return simplePut(rb, cfb, cqb, vb);
	}

	public static boolean simplePut(byte[] row, byte[] cf, byte[] cq, byte[] value) {
		boolean success = false;
		Connection conn = DBConnection.getInstance().getConnection();

		try (Table table = conn.getTable(DBConnection.getTableName())) {
			Put put = new Put(row);
			put.addColumn(cf, cq, value);
			table.put(put);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	public static boolean deleteValue(String rowId, String columnFamily, String columnQualifier) {
		byte[] row = Bytes.toBytes(rowId);
		byte[] cf = Bytes.toBytes(columnFamily);
		byte[] cq = Bytes.toBytes(columnQualifier);

		Connection conn = DBConnection.getInstance().getConnection();

		try (Table table = conn.getTable(DBConnection.getTableName())) {
			Delete del = new Delete(row);
			del.addColumn(cf, cq);
			table.delete(del);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String backupDB() {
		String message = "null";
		
		Connection conn = DBConnection.getInstance().getConnection();
		
		try (Admin admin = conn.getAdmin()) {
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			LocalDateTime now = LocalDateTime.now();
			String dateString = dtf.format(now);
			
			String tableString = DBConnection.getTableString();
			TableName tableName = DBConnection.getTableName();
			String snapshotName = String.format("%s-snapshot-%s", tableString, dateString);
			admin.snapshot(snapshotName, tableName);
			
			message = "success";
		} catch (Exception e) {
			e.printStackTrace();
			message = "failed";
		}
		return message;
	}

}
