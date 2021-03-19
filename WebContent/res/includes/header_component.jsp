
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page
	import="com.laygen.beans.*, java.util.TreeSet, javax.servlet.http.HttpSession"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./res/css/style.css" />
<script src="./res/js/bootstrap.min.css"></script>
<title>Horticulture by Laygen</title>
</head>
<body>
	<div class="banner">

		<div id="bannerRight">
			<p id="bannerTitle">Laygen Horticulture Interface</p>
		</div>
	</div>
	<hr />

	<%
	User user = (User) session.getAttribute("user");
	final Object lock = session.getId().intern();
	%>

	<!-- option bar -->

	<%
	if (user == null || !user.isLoggedIn()) {
	%>
	<%@ include file="/res/includes/not_logged_in_options_subcomponent.jsp"%>
	<%
	} else {
	%>
	<%@ include file="/res/includes/logged_in_options_subcomponent.jsp"%>
	<%
	}
	%>




	<hr>