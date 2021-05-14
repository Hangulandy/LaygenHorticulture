package com.laygen.beans;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.laygen.database.AuthorizationDB;
import com.laygen.database.Dictionary;
import com.laygen.database.UserDB;

public class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String email;
	private String name;
	private String username;
	private String organization;
	private String password;
	private boolean loggedIn;
	private TreeSet<Authorization> authorizations;
	public final static int MAX_NAME_LEN = 40;

	public User(String email, String name, String userName, String organization) {
		this.id = UUID.randomUUID().toString();
		this.email = email;
		this.name = name;
		this.username = userName;
		this.organization = organization;
		this.password = null;
		setLoggedIn(false);
	}

	public User() {
		this.id = UUID.randomUUID().toString();
		this.email = null;
		this.name = null;
		this.username = null;
		this.organization = null;
		this.password = null;
		this.loggedIn = false;
	}

	public static User buildUserFromRequest(HttpServletRequest request) {
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String userName = request.getParameter("userName");
		String organization = request.getParameter("organization");

		User user = new User(email, name, userName, organization);

		if (pwSame(request)) {
			user.setPassword(request.getParameter("pw1"));
		}
		return user;
	}

	private static boolean pwSame(HttpServletRequest request) {
		String pw1 = request.getParameter("pw1");
		String pw2 = request.getParameter("pw2");
		if (StringUtils.isNotBlank(pw1) && StringUtils.isNotBlank(pw2)) {
			return pw1.equalsIgnoreCase(pw2);
		} else {
			return false;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getErrorMsg() {

		if (getEmail().trim().length() == 0) {
			return "emailNotValidMessage";
		}

		if (getName().trim().length() == 0) {
			return "nameTooShortMessage";
		}

		if (getName().trim().length() > MAX_NAME_LEN) {
			return "nameTooLongMessage";
		}

		if (getUsername().trim().length() == 0) {
			return "usernameTooShortMessage";
		}

		if (getUsername().trim().length() > MAX_NAME_LEN) {
			return "usernameTooLongMessage";
		}

		if (getPassword() == null) {
			return "passwordsDoNoMatchMessage";
		} else {
			if (getPassword().length() < 8 || getPassword().length() > 20) {
				return "passwordWrongLengthMessage";
			}
		}
		return null;
	}

	@Override
	public int compareTo(User other) {
		return this.getEmail().compareTo(other.getEmail());
	}

	public String getUserMsg(String lang) {
		if (this.isLoggedIn()) {
			return String.format("%s, %s", Dictionary.getInstance().get("hello", lang), getName());
		}
		return Dictionary.getInstance().get("notLoggedIn", lang);
	}

	public void printUser() {
		System.out.println(this.toString());
		System.out.println("UUID on file : " + this.id);
		System.out.println("Name on file : " + this.name);
		System.out.println("Username on file : " + this.username);
		System.out.println("Organization on file : " + this.organization);
		System.out.println("Password on file : " + this.password);
	}

	public void fetchAuthorizations() {
		this.setAuthorizations(AuthorizationDB.getUserAuthorizations(this));
	}

	public TreeSet<Authorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(TreeSet<Authorization> authorizations) {
		this.authorizations = authorizations;
	}

	public String registerMachine(String serialNumber, String registrationKey) {
		String message = this.getId() != null ? UserDB.registerMachine(this, serialNumber, registrationKey) : "failure";
		this.fetchAuthorizations();
		return message;
	}

}
