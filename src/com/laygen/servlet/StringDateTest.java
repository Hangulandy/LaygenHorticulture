package com.laygen.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringDateTest {
	
	public static void main(String[] args) {
		
		String string = "2021-01-11 23:59";
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		

		try {
			Date date = df.parse(string);
			String dateString = sf.format(date);
			System.out.println(dateString);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}
