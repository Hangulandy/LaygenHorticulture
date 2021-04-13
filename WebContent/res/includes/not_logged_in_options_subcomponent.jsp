<%@ page import="com.laygen.database.Dictionary"%>

<div class="left-head">
	<br>
	<form class="sideBySide" action="Controller">
		<input type="hidden" name="action" value="redirectToJoin" /> <input
			class="button-white" type="submit" value="<%=Dictionary.getInstance().get("join") %>" />
	</form>

	<form class="sideBySide" action="Controller">
	<input type="hidden" name="action" value="home" /> 
		<input class="button-white" type="submit" value="<%=Dictionary.getInstance().get("login") %>" />
	</form>
</div>