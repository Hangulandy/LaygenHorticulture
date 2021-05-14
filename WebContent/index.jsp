<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page
	import="com.laygen.beans.*, com.laygen.database.Dictionary, javax.servlet.http.HttpSession"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="lib/bootstrap.min.css">
<link rel="stylesheet" href="styles/style.css">
<link rel="shortcut icon" href="favicon.ico">
<title>Horticulture by Laygen</title>
</head>

<%
Dictionary dict = (Dictionary) session.getAttribute("dict");
String lang = (String) session.getAttribute("lang");
lang = lang == null ? "ko" : "en";
if (dict == null) {
	session.setAttribute("dict", Dictionary.getInstance());
}
%>

<body ng-app='HorticultureApp'>

	<h1>${dict.get('bannerText', lang) }</h1>

	<ui-view></ui-view>

	<!-- Libraries -->
	<script src="lib/jquery-2.1.4.min.js"></script>
	<script src="lib/bootstrap.min.js"></script>
	<script src="lib/angular.min.js"></script>
	<script src="lib/angular-ui-router.min.js"></script>

	<!-- Main Horticulture App Module -->
	<script src="src/horticultureapp.module.js"></script>

	<!-- Public Module -->
	<script src="src/public/public.module.js"></script>
	<script src="src/public/public.routes.js"></script>
	<script src="src/public/not-logged-in/login/login.controller.js"></script>

	<!-- Common Module -->
	<script src="src/common/common.module.js"></script>
	<script src="src/common/appdata.service.js"></script>

	<div>
		<a href="Controller?selectedLanguage=en&action=selectLanguage"><img
			class="flag" src="img/united-kingdom.png" /></a> <a
			href="Controller?selectedLanguage=ko&action=selectLanguage"><img
			class="flag" src="img/south-korea.png" /></a><br>
		<p class="small-print">
			Flag icons made by <a class="footer-link"
				href="https://www.freepik.com" title="Freepik">Freepik</a> from <a
				class="footer-link" href="https://www.flaticon.com/"
				title="Flaticon">www.flaticon.com</a>
		</p>
	</div>
</body>
</html>