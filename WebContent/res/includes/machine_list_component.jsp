<!-- This will be the component that lists machines a user is authorized to view -->

<%@ page import="com.laygen.beans.Authorization, com.laygen.beans.User, java.util.TreeSet"%>


<%
User userHere = (User) session.getAttribute("user");
if (userHere.getAuthorizations() != null && userHere.getAuthorizations().size() > 0) {
%>
<h1>Here are the machines you can use: </h1>
<table>
	<tr>
		<th>SN</th>
		<th>Nickname</th>
		<th>Owner Email</th>
		<th>Select</th>
	</tr>

	<%
	for (Authorization auth : userHere.getAuthorizations()) {
	%>
	<tr>
		<td><%=auth.getMachineSerialNumber()%></td>
		<td></td>
		<td></td>
		<td><form class="sideBySide" action="Controller" method="post">
				<input class="button" type="submit" value="Select This Machine" />
				<input type="hidden" name="action" value="selectMachine" /> <input
					type="hidden" name="selectedMachineId"
					value="<%=auth.getMachineSerialNumber()%>" />
			</form></td>
	</tr>
	<%}%>

</table>
<%}%>
