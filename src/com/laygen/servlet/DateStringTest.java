package com.laygen.servlet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateStringTest {
	
	public static void main(String[] args) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String dateString = dtf.format(now);
		
		System.out.println(dateString);
		System.out.println(dateString.getClass());
	}

}
