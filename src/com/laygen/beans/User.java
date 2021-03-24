package com.laygen.beans;

import java.io.Serializable;
import java.util.TreeSet;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String email;
	private String name;
	private String username;
	private String organization;
	private String password;
	private String errorMsg;
	private boolean loggedIn;
	private TreeSet<Authorization> authorizations;
	public final static int MAX_NAME_LEN = 40;
	
	// TODO - attribute for authorized machines; set of 

	public User(String email, String name, String userName, String organization) {
		this.id = UUID.randomUUID().toString();
		this.email = email;
		this.name = name;
		this.username = userName;
		this.organization = organization;
		this.password = null;
		this.errorMsg = "";
		setLoggedIn(false);
	}

	public User() {
		this.id = UUID.randomUUID().toString();
		this.email = null;
		this.name = null;
		this.username = null;
		this.organization = null;
		this.password = null;
		this.errorMsg = "";
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
		StringBuilder sb = new StringBuilder("");

		if (getEmail().trim().length() == 0) {
			sb.append("Email address is not valid. ");
		}

		if (getName().trim().length() == 0) {
			sb.append("Name must be at least one character in length. ");
		}

		if (getName().trim().length() > MAX_NAME_LEN) {
			sb.append(String.format("Name can be no more than %d characters long.", MAX_NAME_LEN));
		}

		if (getUsername().trim().length() == 0) {
			sb.append("Username must be at least one character in length. ");
		}

		if (getUsername().trim().length() > MAX_NAME_LEN) {
			sb.append(String.format("Username can only be no more than %d characters long.", MAX_NAME_LEN));
		}

		if (getPassword() == null) {
			sb.append("Passwords do not match. ");
		} else {
			if (getPassword().length() < 8 || getPassword().length() > 20) {
				sb.append("Password must be between 8 and 20 characters in length. ");
			}
		}
		errorMsg = sb.toString();
		return errorMsg;
	}

	@Override
	public int compareTo(User o) {
		return this.getEmail().compareTo(o.getEmail());
	}

	@Override
	public String toString() {
		return String.format("User %s with username %s is%slogged in.", getName(), getUsername(),
				this.isLoggedIn() ? " " : " NOT ");
	}
	
	public void printUser() {
		System.out.println(this.toString());
		System.out.println("UUID on file : " + this.id);
		System.out.println("Name on file : " + this.name);
		System.out.println("Username on file : " + this.username);
		System.out.println("Organization on file : " + this.organization);
		System.out.println("Password on file : " + this.password);
	}

	public void refreshAuthorizations() {
		// TODO Auto-generated method stub
		
	}

	public TreeSet<Authorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(TreeSet<Authorization> authorizations) {
		this.authorizations = authorizations;
	}

}
