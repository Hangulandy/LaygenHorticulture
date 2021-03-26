package com.laygen.beans;

import java.lang.Comparable;

public class Message implements Comparable<Message> {

	private String rowId;
	private String columnFamily;
	private String columnName;
	private String value;
	private String timestamp;

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(Message that) {
		if (!this.getRowId().equals(that.getRowId())) {
			return this.getRowId().compareTo(that.getRowId());
		}

		if (!this.getColumnFamily().equals(that.getColumnFamily())) {
			return this.getColumnFamily().compareTo(that.getColumnFamily());
		}

		if (!this.getColumnName().equals(that.getColumnName())) {
			return this.getColumnName().compareTo(that.getColumnName());
		}
		
		return this.getValue().compareTo(that.getValue());
	}
	
	public String getSensor() {
		
		String[] parts = this.getRowId().split("-");
		
		if (parts.length > 1) {
			return parts[1];
		} else {
			return null;
		}
	}
	
	public String getTime() {
		String[] parts = this.getRowId().split("-");
		
		if (parts.length > 2) {
			return parts[2];
		} else {
			return null;
		}
	}

	public String toString() {
		return String.format("Message : \n row : %s \n column family : %s \n column name : %s \n value : %s ",
				this.getRowId(), this.getColumnFamily(), this.getColumnName(), this.getValue());
	}

}
