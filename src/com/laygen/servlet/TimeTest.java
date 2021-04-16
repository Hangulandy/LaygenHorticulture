package com.laygen.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeTest {
	
	public static void main(String[] args) {
		Date currentTime = new Date();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timeString = sdf.format(currentTime);
		
		String msg = String.format("%s#time", timeString);

		System.out.println(msg);
	}

}
