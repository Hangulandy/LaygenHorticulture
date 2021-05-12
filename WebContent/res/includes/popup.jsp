<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<% String popupMessage = (String) session.getAttribute("popupMessage");
if (popupMessage != null && !popupMessage.equalsIgnoreCase("null")){%>
<script type="text/javascript">
confirm("${dict.get(popupMessage, lang)}");

</script>
<%} %>