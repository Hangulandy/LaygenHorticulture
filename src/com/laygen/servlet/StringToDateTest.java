package com.laygen.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringToDateTest {
	
	
	public static void main(String[] args) {
		
		String dateAsString = "20210326042648";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			Date date = formatter.parse(dateAsString);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
