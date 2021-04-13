<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<% String popup = (String) session.getAttribute("popup");
if (popup != null){%>
<script type="text/javascript">
confirm("${popup}");

</script>
<%} %>