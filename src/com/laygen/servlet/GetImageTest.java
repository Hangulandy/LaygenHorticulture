package com.laygen.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.laygen.database.MachineDB;
import com.laygen.database.MessageDB;

public class GetImageTest {
	
	static byte[] convertByteArrayToImage(byte[] data, String path) throws IOException {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		BufferedImage bImage = ImageIO.read(bis);
		ImageIO.write(bImage, "jpg", new File(path));
		System.out.println(String.format("Image %s created.", path));
		bis.close();
		
		return data;
	}
	
	
	static void downloadImageFromHBase(String row, String imgPath) {
		
		byte[] data = null;
		data = MessageDB.getByteValue(row, "I", "data");
		System.out.println(data);
		try {
			convertByteArrayToImage(data, imgPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String[] sns = {"0123456789"};
		
		for (String sn : sns) {
			
			Map<String, String> map = MachineDB.getImageNamesForMachine(sn);
			
			for (String key : map.keySet()) {
				downloadImageFromHBase(key, key);			
			}
		}
	}
	
	

}
