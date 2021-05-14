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

			if (dict == null) {
				dict = Dictionary.getInstance();
				session.setAttribute("dict", dict);
			}

			if (action.equalsIgnoreCase("selectLanguage")) {
				selectLanguage(request, response, session);
			} else {
				initializeSessionAttributes(session);
			}

			if (action.equalsIgnoreCase("login")) {
				login(request, response, session);
			}
		}

	}

	private void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = UserDB.login(email, password);
		session.setAttribute("user", user);
		sendObjectWithResponse(user, session, response);
	}

	private void sendObjectWithResponse(Object obj, HttpSession session, HttpServletResponse response) {
		
		//User user = (User) session.getAttribute("user");
		User user = null;
		
		MyResponse resp = new MyResponse();
		resp.setUser(user);
		resp.setObject(user == null ? null : obj);
		
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

//	private void sendMapAsResponse(Map map, HttpServletResponse response) {
//		try {
//			PrintWriter out = response.getWriter();
//			response.setContentType("application/json");
//			Gson gson = new Gson();
//			Type gsonType = new TypeToken<Map>(){}.getType();
//			String jsonData = gson.toJson(map, gsonType);
//			out.print(jsonData);
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	private void selectLanguage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String lang = request.getParameter("selectedLanguage");
		String url = "/";

		if (lang != null) {
			session.setAttribute("lang", lang);
		} else {
			session.setAttribute("lang", "ko");
		}
		
		redirect(url, request, response);
	}

	private void initializeSessionAttributes(HttpSession session) {
		session.setAttribute("message", "null");
		session.setAttribute("popupMessage", "null");
		session.setAttribute("viewComponent", null);
		session.setAttribute("selectedImage", null);
		session.setAttribute("searchedUser", null);
	}
	
	private void redirect(String url, HttpServletRequest request, HttpServletResponse response) {
		try {
			getServletContext().getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
