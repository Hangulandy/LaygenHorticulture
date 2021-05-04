
<%@ page
	import="com.laygen.beans.Machine, com.laygen.beans.Sensor, com.laygen.database.Dictionary"%>

<h1>${dict.get('machineInfoHeading', lang)}</h1>
<hr>

<%
Machine machine = (Machine) session.getAttribute("machine");
Dictionary dict1 = (Dictionary) session.getAttribute("dict");
String lang1 = (String) session.getAttribute("lang");
%>

<div class="info-page-tile">
	<h3>${dict.get('currentSettingsHeading', lang) }</h3>
	<div class="small-space"></div>

	<%
	String[] settingsToDisplay = {"plant_date", "light_on", "light_color", "brightness", "fan_on", "fan_auto", "fan_humidity"};
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

	<p><%=dict1.get(setting, lang1)%>
		:
		<%=value%></p>
	<%
	}
	}
	%>
</div>


<div class="info-page-tile">
	<h3>${dict.get('currentSensorDataHeading',lang)}</h3>
	<div class="small-space"></div>

	<%
	if (machine != null && machine.getSensors() != null) {
		String value = null;
		Sensor sensor = null;
		for (String key : machine.getSensors().keySet()) {
			sensor = machine.getSensors().get(key);
			value = sensor != null ? machine.getReadings().get(sensor.getName()) : "0";
			value = value != null ? value : "0";
	%>
	<p><%=dict1.get(key, lang1)%>
		:
		<%=value%>
		<%=sensor.getUnits()%></p>
	<%
	}
	%>

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
	<div class="small-space"></div>
	<h3 class="sideBySide">${dict.get('generalInfoHeading', lang)}</h3>
	<form class="sideBySide margin-top" action="Controller" method="post">
		<input class="button" type="submit"
			value="${dict.get('refresh', lang)}" /> <input type="hidden"
			name="action" value="selectMachine" /> <input type="hidden"
			name="selectedMachineId" value="${machine.serialNumber}" />
	</form>
	<div class="small-space"></div>
	<form class="sideBySide" action="Controller" method="post">
		<p>${dict.get('serialNumberLabel', lang)}:${machine.serialNumber}
		</p>
		<p>${dict.get('model_name', lang)}:${machine.info['model_name'] }
		</p>
		<p>${dict.get('primary_use', lang)}:${machine.info['primary_use'] }</p>
		<p>${dict.get('owner_email', lang)}:${machine.info['owner_email'] }
		</p>
		<p>
			${dict.get('nickname', lang)} : <input id="nickname" type="text"
				name="nickname" value="${machine.info['nickname'] }" required />
			<button id="editButton" class="button-red" type="button"
				onclick="toggleNicknameGlyph()">${dict.get('editButtonLabel', lang)}</button>
		</p>
		<p>${dict.get('ip', lang)}:${machine.info['ip'] }</p>
		<p>${dict.get('port', lang)}:${machine.info['port'] }</p>
		<input id="saveButton" class="button-red margin-top" type="submit"
			value="${dict.get('saveButtonLabel', lang)}" /> <input type="hidden"
			name="action" value="updateMachineInfo" /> <input type="hidden"
			name="selectedMachineId" value="${machine.serialNumber}" />
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
