package com.laygen.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.laygen.beans.Machine;

/**
 * Servlet implementation class ShowLargeChartServlet
 */
@WebServlet("/ShowLargeChartServlet")
public class ShowLargeChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowLargeChartServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String url = "/index.jsp";
		HttpSession session = request.getSession();

		final Object lock = session.getId().intern();

		synchronized (lock) {

			String sensorName = request.getParameter("sensor");
			Machine machine = (Machine) session.getAttribute("machine");

			if (sensorName != null && machine != null && machine.getSensors() != null
					&& machine.getSensors().get(sensorName) != null
					&& machine.getSensors().get(sensorName).getReadings() != null) {
				session.setAttribute("sensorName", sensorName);
				url = "/large_chart.jsp";
			}
		}
		getServletContext().getRequestDispatcher(url).forward(request, response);
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
