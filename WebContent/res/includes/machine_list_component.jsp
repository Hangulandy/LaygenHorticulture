<!-- This will be the component that lists machines a user is authorized to view -->

<%@ page
	import="com.laygen.beans.Authorization, com.laygen.beans.User, java.util.TreeSet"%>


<%
User userHere = (User) session.getAttribute("user");
if (userHere.getAuthorizations() != null && userHere.getAuthorizations().size() > 0) {%>
<h1>${dict.get('machineListHeading', lang)}:
</h1>
<table>
	<tr>
		<th>${dict.get('serialNumberLabel', lang)}</th>
		<th>${dict.get('nickname', lang)}</th>
		<th class="not-mobile">${dict.get('ownerEmail', lang)}</th>
		<th>${dict.get('select', lang)}</th>
	</tr>

	<%
	for (Authorization auth : userHere.getAuthorizations()) {
	%>
	<tr>
		<td><%=auth.getMachineSerialNumber()%></td>
		<td><%=auth.getMachineNickname()%></td>
		<td class="not-mobile"><%=auth.getOwnerEmail()%></td>
		<td><form class="sideBySide" action="Controller" method="post">
				<input class="button" type="submit"
					value="${dict.get('select', lang)}" /> <input
					type="hidden" name="action" value="selectMachine" /> <input
					type="hidden" name="selectedMachineId"
					value="<%=auth.getMachineSerialNumber()%>" />
			</form></td>
	</tr>
	<%}%>
</table>
<%} else {%>
<p>No machines found for that user.</p>
<%}%>