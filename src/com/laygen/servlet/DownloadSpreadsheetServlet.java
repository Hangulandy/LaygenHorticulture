package com.laygen.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.laygen.beans.Machine;
import com.laygen.beans.Message;
import com.laygen.beans.Sensor;

/**
 * Servlet implementation class DownloadSpreadsheetServelet
 */
@WebServlet("/DownloadSpreadsheetServlet")
public class DownloadSpreadsheetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadSpreadsheetServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		final Object lock = session.getId().intern();

		synchronized (lock) {

			Machine machine = (Machine) session.getAttribute("machine");
			String sensorString = request.getParameter("sensor");

			if (machine != null && machine.getSensors() != null && machine.getSensors().get(sensorString) != null) {
				Sensor sensor = machine.getSensors().get(sensorString);

				if (sensor.getReadings() != null) {

					TreeSet<Message> readings = sensor.getReadings();

					Workbook workbook = new HSSFWorkbook();
					Sheet sheet = workbook.createSheet(sensorString);

					Row row = sheet.createRow(0);
					row.createCell(0).setCellValue("rowId");
					row.createCell(1).setCellValue("sensor");
					row.createCell(2).setCellValue("date");
					row.createCell(3).setCellValue("value");

					int index = 1;
					for (Message reading : readings) {
						row = sheet.createRow(index);
						row.createCell(0).setCellValue(reading.getRowId());
						row.createCell(1).setCellValue(reading.getSensor());
						row.createCell(2).setCellValue(reading.getTime());
						row.createCell(3).setCellValue(reading.getValue());
						index++;
					}

					response.setHeader("content-disposition", "attachment; filename=data.csv");
					response.setHeader("cache-control", "no-cache");

					OutputStream out = response.getOutputStream();
					workbook.write(out);
					out.close();
					workbook.close();
				}

			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
