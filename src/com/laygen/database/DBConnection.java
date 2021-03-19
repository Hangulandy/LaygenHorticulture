package com.laygen.database;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class DBConnection {
	
	static Configuration conf;
	static Connection conn;
	private static DBConnection instance = new DBConnection();
	
	private static String tableName = "horticulture";
	
	private DBConnection() {
		conf = HBaseConfiguration.create();

		try {
			conn = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static DBConnection getInstance() {
		return instance;
	}
	
	public Connection getConnection() {
		return conn;
	}

	public static String getTableName() {
		return tableName;
	}
	

}