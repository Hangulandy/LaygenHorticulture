package com.laygen.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.laygen.beans.MyResponse;
import com.laygen.beans.User;
import com.laygen.database.Dictionary;
import com.laygen.database.UserDB;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Controller() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		action = action == null ? "home" : action;

		HttpSession session = request.getSession();

		final Object lock = session.getId().intern();

		synchronized (lock) {

			Dictionary dict = (Dictionary) session.getAttribute("dict");

			if (dict == null || dict.getEntries() == null) {
				dict = Dictionary.getInstance();
				session.setAttribute("dict", dict);
			}

			if (action.equalsIgnoreCase("getDictionary")) {
				getDictionary(request, response, session);
			} else {
				initializeSessionAttributes(session);
			}

			if (action.equalsIgnoreCase("login")) {
				login(request, response, session);
			}

			if (action.equalsIgnoreCase("logout")) {
				logout(session);
			}

			if (action.equalsIgnoreCase("join")) {
				join(request, response, session);
			}

			if (action.equalsIgnoreCase("getAuthorizations")) {
				getAuthorizations(request, response, session);
			}
		}

	}

	private void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = new User();
		String message = user.login(email, password);

		session.setAttribute("user", user);
		sendObjectWithResponse(user, message, session, response);
	}

	private void logout(HttpSession session) {
		session.invalidate();
	}

	private void join(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		User user = User.buildUserFromRequest(request);
		String message = user.getErrorMsg() == null ? UserDB.insert(user) : "null";

		sendObjectWithResponse(user, message, session, response);
	}

	private void getAuthorizations(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		User user = (User) session.getAttribute("user");
		String message = "null";

		if (user != null && user.getId() != null) {
			user.fetchAuthorizations();
			System.out.println(user.getAuthorizations().size());
			message = "selectMachinePrompt";
		} else {
			user = null;
			logout(session);
		}
		sendObjectWithResponse(user, message, session, response);		
	}

	private void sendObjectWithResponse(Object obj, String message, HttpSession session, HttpServletResponse response) {

		User user = (User) session.getAttribute("user");

		MyResponse resp = new MyResponse();
		resp.setUser(user);
		resp.setObject(user == null ? null : obj);
		resp.setMessage(message);

		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			Gson gson = new Gson();
			String jsonData = gson.toJson(resp);
			out.print(jsonData);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMapAsResponse(Map map, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=UTF-8");

			PrintWriter out = response.getWriter();
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			Type gsonType = new TypeToken<Map>() {
			}.getType();
			String jsonData = gson.toJson(map, gsonType);
			out.print(jsonData);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getDictionary(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Dictionary dict = (Dictionary) session.getAttribute("dict");
		sendMapAsResponse(dict.getEntries(), response);
	}

	private void initializeSessionAttributes(HttpSession session) {
		session.setAttribute("message", "null");
		session.setAttribute("popupMessage", "null");
		session.setAttribute("viewComponent", null);
		session.setAttribute("selectedImage", null);
		session.setAttribute("searchedUser", null);
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
