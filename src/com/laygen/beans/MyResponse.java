package com.laygen.beans;

public class MyResponse {

	private User user;
	private Object object;
	private String message;
	private String string;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public void printIt() {
		System.out.println("Response to send:");
		System.out.printf("User : %s\n", user != null ? user.getName() : null);
		System.out.printf("Object : %s\n", object != null ? object : null);
		System.out.printf("Message : %s\n", message);
		System.out.printf("ImageString is %sNull\n", this.getString() == null ? "" : "NOT ");
	}

}
