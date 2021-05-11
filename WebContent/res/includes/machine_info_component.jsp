
<%@ page
	import="com.laygen.beans.Machine, com.laygen.beans.User, com.laygen.beans.Sensor, com.laygen.database.Dictionary"%>

<h1 class="sideBySide">${dict.get('machineInfoHeading', lang)}</h1>
<form class="sideBySide margin-top" action="Controller" method="post">
	<input class="button" type="submit"
		value="${dict.get('refresh', lang)}" /> <input type="hidden"
		name="action" value="selectMachine" /> <input type="hidden"
		name="selectedMachineId" value="${machine.serialNumber}" />
</form>
<h3 class="sideBySide">${message}</h3>
<hr>

<%
Machine machine = (Machine) session.getAttribute("machine");
Dictionary dict1 = (Dictionary) session.getAttribute("dict");
String lang1 = (String) session.getAttribute("lang");
%>


<div class="info-page-tile">
	<h3>${dict.get('currentSettingsHeading', lang) }</h3>
	<table class="transparent">

		<%
		String[] settingsToDisplay = {"plant_date", "light_on", "light_color", "brightness", "fan_on", "fan_auto",
				"fan_humidity"};
		if (machine != null && machine.getSettings() != null) {
			String value = null;
			for (String setting : settingsToDisplay) {
				if (setting.contains("_on")) {
			value = machine.getSettings().get(setting).equalsIgnoreCase("0")
					? dict1.get("offLabel", lang1)
					: dict1.get("onLabel", lang1);
				} else if (setting.contains("_auto")) {
			value = machine.getSettings().get(setting).equalsIgnoreCase("0")
					? dict1.get("cont", lang1)
					: dict1.get("auto", lang1);
				} else {
			value = setting.equalsIgnoreCase("light_color")
					? dict1.get(machine.getSettings().get(setting), lang1)
					: machine.getSettings().get(setting);
				}
		%>
		<tr class="transparent">
			<td class="transparent"><%=dict1.get(setting, lang1)%></td>
			<td class="transparent">:</td>
			<td class="transparent"><%=value%></td>
		</tr>
		<%
		}
		}
		%>
	</table>
</div>


<div class="info-page-tile">
	<h3>${dict.get('currentSensorDataHeading',lang)}</h3>
	<table class="transparent">
		<%
		if (machine != null && machine.getSensors() != null) {
			String value = null;
			Sensor sensor = null;
			for (String key : machine.getSensors().keySet()) {
				sensor = machine.getSensors().get(key);
				value = sensor != null ? machine.getReadings().get(sensor.getName()) : "0";
				value = value != null ? value : "0";
		%>
		<tr class="transparent">
			<td class="transparent"><%=dict1.get(key, lang1)%></td>
			<td class="transparent">:</td>
			<td class="transparent"><%=value%> <%=sensor.getUnits()%></td>
		</tr>
		<%
		}
		%>

	</table>
	<%
	} else {
	%>
	<p>${dict.get('noSensors', lang)}</p>
	<%
}
%>
	<br>
</div>

<div class="info-page-tile">
	<h3>${dict.get('generalInfoHeading', lang)}</h3>

	<form class="sideBySide" action="Controller" method="post">
		<table class="transparent">
			<tr class="transparent">
				<td class="transparent">${dict.get('serialNumberLabel', lang)}</td>
				<td class="transparent">:</td>
				<td class="transparent">${machine.serialNumber}</td>
			</tr>

			<tr class="transparent">
				<td class="transparent">${dict.get('model_name', lang)}</td>
				<td class="transparent">:</td>
				<td class="transparent">${machine.info['model_name']}</td>
			</tr>
			<tr class="transparent">
				<td class="transparent">${dict.get('primary_use', lang)}</td>
				<td class="transparent">:</td>
				<td class="transparent">${machine.info['primary_use']}</td>
			</tr>
			<tr class="transparent">
				<td class="transparent">${dict.get('owner_email', lang)}</td>
				<td class="transparent">:</td>
				<td class="transparent">${machine.info['owner_email'] }</td>
			</tr>
			<tr class="transparent">
				<td class="transparent">${dict.get('nickname', lang)}</td>
				<td class="transparent">:</td>
				<td class="transparent"><input id="nickname" type="text"
					name="nickname" value="${machine.info['nickname'] }" required /></td>
				<td class="transparent"><button id="editButton"
						class="button-red" type="button" onclick="toggleNicknameGlyph()">${dict.get('editButtonLabel', lang)}</button></td>
			</tr>
			<tr class="transparent">
				<td class="transparent">${dict.get('ip', lang)}</td>
				<td class="transparent">:</td>
				<td class="transparent">${machine.info['ip']}</td>
			</tr>
			<tr class="transparent">
				<td class="transparent">${dict.get('port', lang)}</td>
				<td class="transparent">:</td>
				<td class="transparent">${machine.info['port'] }</td>
			</tr>
			<tr class="transparent">
				<td class="transparent"></td>
				<td class="transparent"></td>
				<td class="transparent"><input id="saveButton"
					class="button-red margin-top" type="submit"
					value="${dict.get('saveButtonLabel', lang)}" /></td>
			</tr>
		</table>
		<input type="hidden" name="action" value="updateMachineInfo" /> <input
			type="hidden" name="selectedMachineId"
			value="${machine.serialNumber}" />
	</form>
	<br>

</div>

<script>
	var nickname = document.getElementById("nickname");
	nickname.disabled = true;

	var editButton = document.getElementById("editButton");

	var saveButton = document.getElementById("saveButton");
	saveButton.style.display = "none";

	function toggleNicknameGlyph() {
		if (saveButton.style.display = "none") {
			saveButton.style.display = "inline";
			nickname.disabled = true;
			editButton.display = "inline";
		}
		if (nickname.disabled) {
			nickname.disabled = false;
			editButton.style.display = "none";
		}
	}
</script>

<div class="info-page-tile clear">
	<h3>Authorized Users</h3>

	<%
	if (machine != null && machine.getAuthorizedUsers() != null && machine.getAuthorizedUsers().size() > 0) {
	%>

	<table>
		<tr>
			<th class="left">Name</th>
			<th>Organization</th>
			<th>Email</th>
			<th>Revoke</th>
		</tr>

		<%
		for (User authUser : machine.getAuthorizedUsers()) {
		%>
		<tr>
			<td class="left"><%=authUser.getName()%></td>
			<td class="left"><%=authUser.getOrganization()%></td>
			<td class="left"><%=authUser.getEmail()%></td>
			<td>
				<form class="sideBySide" action="Controller" method="get">
					<input class="button" type="submit" value="Revoke" /> <input
						type="hidden" name="action" value="removeUser" /> <input
						type="hidden" name="userToRemove" value="<%=authUser.getId()%>" />
				</form>
			</td>
		</tr>
		<%
		}
		%>
	</table>
	<%
	} else {
	%>
	<p>There are no registered users for that machine.</p>
	<%
	}
	%>
</div>

<div class="info-page-tile">
	<h3>Search Users</h3>
	<form action="Controller" method="get">
		<input type="text" name="email" placeholder="other user email"
			required /> <input type="hidden" name="action" value="searchForUser">
		<input class="button" type="submit" value="Search" />
	</form>
</div>


<div class="info-page-tile clear">

	<%
	User searchedUser = (User) session.getAttribute("searchedUser");
	if (searchedUser != null) {
	%>
	<h3>Add User</h3>

	<table>
		<tr>
			<th class="left">Name</th>
			<th>Organization</th>
			<th>Email</th>
			<th>Add</th>
		</tr>
		<tr>
			<td class="left"><%=searchedUser.getName()%></td>
			<td class="left"><%=searchedUser.getOrganization()%></td>
			<td class="left"><%=searchedUser.getEmail()%></td>
			<td>
				<%
				// What are the conditions to show the add button?
				User user1 = (User) session.getAttribute("user");
				if (user1 != null && user1.getEmail() != null && machine != null && machine.getInfo() != null
						&& machine.getInfo().get("owner_email") != null
						&& machine.getInfo().get("owner_email").equalsIgnoreCase(user1.getEmail())
						&& !machine.getInfo().get("owner_email").equalsIgnoreCase(searchedUser.getEmail())
						&& machine.getAuthorizedUsers() != null && !machine.getAuthorizedUsers().contains(searchedUser)) {
				%>
				<form class="sideBySide" action="Controller" method="get">
					<input class="button" type="submit" value="Add" /> <input
						type="hidden" name="action" value="addUser" /> <input
						type="hidden" name="userToAdd" value="<%=searchedUser.getId()%>" />
				</form> <% } %>
			</td>
		</tr>
	</table>
	<%
	}
	%>
</div>

