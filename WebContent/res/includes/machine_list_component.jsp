<!-- This will be the component that lists machines a user is authorized to view -->

<%@ page
	import="com.laygen.beans.Authorization, com.laygen.beans.User, com.laygen.database.Dictionary, java.util.TreeSet"%>


<%
User userHere = (User) session.getAttribute("user");
if (userHere.getAuthorizations() != null && userHere.getAuthorizations().size() > 0) {
}
%>

<h1><%=Dictionary.getInstance().get("machineListHeading")%>:
</h1>
<table>
	<tr>
		<th>SN</th>
		<th><%=Dictionary.getInstance().get("nickname")%></th>
		<th><%=Dictionary.getInstance().get("ownerEmail")%></th>
		<th><%=Dictionary.getInstance().get("select")%></th>
	</tr>

	<%
		for (Authorization auth : userHere.getAuthorizations()) {
		%>
	<tr>
		<td><%=auth.getMachineSerialNumber()%></td>
		<td><%=auth.getMachineNickname()%></td>
		<td><%=auth.getOwnerEmail()%></td>
		<td><form class="sideBySide" action="Controller" method="post">
				<input class="button" type="submit"
					value="<%=Dictionary.getInstance().get("select")%>" /> <input
					type="hidden" name="action" value="selectMachine" /> <input
					type="hidden" name="selectedMachineId"
					value="<%=auth.getMachineSerialNumber()%>" />
			</form></td>
	</tr>
	<%}%>

</table>