package com.laygen.servlet;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;

import com.laygen.database.MessageDB;

public class PutImageTest {
	
	static byte[] convertImageToByteArray(String path) throws IOException {
		
		byte[] data = null;
		BufferedImage bImage = ImageIO.read(new File(path));
	
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bImage, "jpg", bos);
		data = bos.toByteArray();
		bos.close();
		
		return data;
	}
	
	
	static void putImage(String row, String imgPath) {
		
		byte[] rowId = Bytes.toBytes(row);
		byte[] cf = Bytes.toBytes("I");
		byte[] cq1 = Bytes.toBytes("name");
		byte[] cq2 = Bytes.toBytes("data");
		byte[] val = Bytes.toBytes(imgPath);
		byte[] data = null;
		try {
			data = convertImageToByteArray(imgPath);
			MessageDB.simplePut(rowId, cf, cq1, val);
			MessageDB.simplePut(rowId, cf, cq2, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		putImage("imageTest1", "sonic.jpg");
	}

	
}
