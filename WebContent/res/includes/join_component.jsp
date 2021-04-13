<%@ page import="com.laygen.database.Dictionary"%>

<h1><%=Dictionary.getInstance().get("joinHeading")%></h1>
<hr>
<h3><%=Dictionary.getInstance().get("joinPrompt")%></h3>
<br>

<form action="Controller" method="post">
	<input type="hidden" name="action" value="join" /> <label
		class="pad_top"><%=Dictionary.getInstance().get("emailLabel")%>:</label>
	<input type="email" name="email" value="${user.email}" required /> <br><br>
	<label class="pad_top"><%=Dictionary.getInstance().get("nameLabel")%>:</label>
	<input type="text" name="name" value="${user.name}" required /> <br><br>

	<label class="pad_top"><%=Dictionary.getInstance().get("usernameLabel")%>:</label>
	<input type="text" name="userName" value="${user.username}" required />
	<br><br> <label class="pad_top"><%=Dictionary.getInstance().get("organizationLabel")%>:</label>
	<input type="text" name="organization" value="${user.organization}"
		required /> <br><br> <label class="pad_top"><%=Dictionary.getInstance().get("passwordLabel")%>:</label>
	<input type="password" name="pw1" required maxlength="20" /> <br><br>
	<label class="pad_top"><%=Dictionary.getInstance().get("confirmPasswordLabel")%>:</label>
	<input type="password" name="pw2" required maxlength="20" /> 
	<br>
	<br>
	<input class="button" type="submit"
		value="<%=Dictionary.getInstance().get("submitLabel")%>" />
</form>