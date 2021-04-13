<%@ page import="com.laygen.database.Dictionary"%>
<h1><%=Dictionary.getInstance().get("login")%></h1>
<hr>
<br>
<form action="Controller" method="post">
	<input type="hidden" name="action" value="login" /> <label
		class="pad_top"><%=Dictionary.getInstance().get("emailLabel") %>:</label>
	<input type="email" name="email" value="${user.email}" required /> <br><br>
	<label class="pad_top"><%=Dictionary.getInstance().get("passwordLabel") %>:</label>
	<input type="password" name="password" required min=5 max=20 /> 
	<br>
	<br>
	<input class="button" type="submit"
		value="<%=Dictionary.getInstance().get("login")%>" class="margin_left" />
</form>
