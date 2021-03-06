<!-- This will be the component that lists machines a user is authorized to view -->

<%@ page
	import="com.laygen.beans.Authorization, com.laygen.beans.User, java.util.TreeSet"%>

<h1 class="sideBySide">${dict.get('machineListHeading', lang)}:</h1><h3 class="sideBySide">${dict.get(message, lang)}</h3>
<hr>

<%
User userHere = (User) session.getAttribute("user");
if (userHere.getAuthorizations() != null && userHere.getAuthorizations().size() > 0) {%>

<table>
	<tr>
		<th>${dict.get('serialNumberLabel', lang)}</th>
		<th>${dict.get('nickname', lang)}</th>
		<th class="not-mobile">${dict.get('owner_email', lang)}</th>
		<th>${dict.get('select', lang)}</th>
	</tr>

	<%
	if (userHere.getAuthorizations() != null){
		

	for (Authorization auth : userHere.getAuthorizations()) {
	%>
	<tr>
		<td><%=auth.getMachineSerialNumber()%></td>
		<td><%=auth.getMachineNickname()%></td>
		<td class="not-mobile"><%=auth.getOwnerEmail()%></td>
		<td><form class="sideBySide" action="Controller" method="get">
				<input class="button" type="submit"
					value="${dict.get('select', lang)}" /> <input type="hidden"
					name="action" value="selectMachine" /> <input type="hidden"
					name="selectedMachineId" value="<%=auth.getMachineSerialNumber()%>" />
			</form></td>
	</tr>
	<%}}%>
</table>
<%} else {%>
<h3>${dict.get('noAuthorizedMachines', lang)}</h3>
<%}%>

<h3 class="sideBySide">Register a new machine :</h3>
<form class="sideBySide" action="Controller" method="get">
	<input class="button" type="submit"
		value="${dict.get('registerLabel', lang)}" /> <input type="hidden"
		name="action" value="redirectToRegister" />
</form>