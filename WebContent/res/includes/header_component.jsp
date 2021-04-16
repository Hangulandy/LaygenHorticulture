
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page
	import="com.laygen.beans.*, com.laygen.database.Dictionary, java.util.TreeSet, javax.servlet.http.HttpSession"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="./res/css/normalize.css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.0.0/Chart.min.js"></script>
<link rel="stylesheet" href="./res/css/style.css" />
<title>Horticulture by Laygen</title>
</head>
<body>
	<%
	Dictionary dict = (Dictionary) session.getAttribute("dict");
	%>
	<%
	if (dict == null) {
		session.setAttribute("dict", Dictionary.getInstance());
	}
	%>
	<div class="header">
		<div class="banner">
			<p>${dict.get('bannerText', lang)}</p>
		</div>
		<!-- option bar -->
		<%
		User user = (User) session.getAttribute("user");
		final Object lock = session.getId().intern();
		if (user == null || !user.isLoggedIn()) {
		%>
		<%@ include
			file="/res/includes/not_logged_in_options_subcomponent.jsp"%>
		<%
		} else {
		%>
		<%@ include file="/res/includes/logged_in_options_subcomponent.jsp"%>
		<%
		}
		%>
	</div>