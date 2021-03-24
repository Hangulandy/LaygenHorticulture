package com.laygen.database;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NavigableMap;
import java.util.TreeSet;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import com.laygen.beans.Message;

public class MessageDB {

	static Charset enc = StandardCharsets.UTF_8;

	public static TreeSet<Message> scanColumnFamily(String columnFamily){
		return scanColumnFamilyWithRowPrefix(null, null, columnFamily);
	}
	
	public static TreeSet<Message> scanColumnFamilyWithRowPrefix(String columnFamily, String columnQualifier, String rowPrefix) {
		Connection conn = DBConnection.getInstance().getConnection();
		String tableName = DBConnection.getTableName();
		TreeSet<Message> messages = new TreeSet<Message>();

		try (Table table = conn.getTable(TableName.valueOf(tableName))) {
			Scan scan = new Scan();
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
		String tableName = DBConnection.getTableName();
		TreeSet<Message> messages = new TreeSet<Message>();

		try (Table table = conn.getTable(TableName.valueOf(tableName))) {
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
		String tableName = DBConnection.getTableName();
		byte[] output = null;
		
		byte[] row = Bytes.toBytes(rowId);
		byte[] cf = Bytes.toBytes(columnFamily);
		byte[] cq = Bytes.toBytes(columnQualifier);

		try (Table table = conn.getTable(TableName.valueOf(tableName))) {
			Get get = new Get(row);
				get.addColumn(cf, cq);
			Result rr = table.get(get);
			output = rr.getFamilyMap(cf).get(cq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
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
	
	public static void simplePut(byte[] row, byte[] cf, byte[] cq, byte[] value) {
		
		Connection conn = DBConnection.getInstance().getConnection();
		String tableName = DBConnection.getTableName();
		
		try (Table table = conn.getTable(TableName.valueOf(tableName))) {
			Put put = new Put(row);
			put.addColumn(cf, cq, value);
			table.put(put);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static boolean deleteValue(String rowId, String columnFamily, String columnQualifier) {
		byte[] row = Bytes.toBytes(rowId);
		byte[] cf = Bytes.toBytes(columnFamily);
		byte[] cq = Bytes.toBytes(columnQualifier);
		
		Connection conn = DBConnection.getInstance().getConnection();
		String tableName = DBConnection.getTableName();
		
		try (Table table = conn.getTable(TableName.valueOf(tableName))){
			Delete del = new Delete(row);
			del.addColumn(cf, cq);
			table.delete(del);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
