<%@ page import="com.laygen.beans.Machine"%>

<p>${machineSettingsViewMessage}</p>

<%
boolean lightChecked = false;
boolean pumpChecked = false;
String lightOn = "Off";
String pumpOn = "Off";
Machine machine = (Machine) session.getAttribute("machine");

if (machine != null) {
	if (machine.getSettings().get("light_on") != null && machine.getSettings().get("light_on").equalsIgnoreCase("1")) {
		lightChecked = true;
		lightOn = "On";
	}
	if (machine.getSettings().get("pump_on") != null && machine.getSettings().get("pump_on").equalsIgnoreCase("1")) {
		pumpChecked = true;
		pumpOn = "On";
	}
%>

<!-- This will only show if machine != null because it is inside the code block following the if statement -->
<form action="Controller" method="get">
	<table>
		<tr>
			<th>Setting</th>
			<th>Value</th>
			<th>Adjust</th>
		</tr>
		<tr>
			<td>Light On / Off</td>
			<td><%=lightOn%></td>
			<td><label class="radio-inline"><input type="radio"
					id="light_on" name="light_on" value="1" <%if (lightChecked) {%>
					checked <%}%>>On</label> <label class="radio-inline"><input
					type="radio" id="light_off" name="light_on" value="0"
					<%if (!lightChecked) {%> checked <%}%>>Off</label></td>
		</tr>
		<tr>
			<td>Brightness</td>
			<td>${machine.settings['brightness'] }</td>
			<td><input type="number" id="brightness" name="brightness"
				step="5" min="0" max="100" value="${machine.settings['brightness'] }"></td>
		</tr>
		<tr>
			<td>Pump On / Off</td>
			<td><%=pumpOn%></td>
			<td><label class="radio-inline"><input type="radio"
					id="pump_on" name="pump_on" value="1" <%if (pumpChecked) {%>
					checked <%}%>>On</label> <label class="radio-inline"><input
					type="radio" id="pump_off" name="pump_on" value="0"
					<%if (!pumpChecked) {%> checked <%}%>>Off</label></td>
		</tr>
		<tr>
			<td>Pump Duration (ms)</td>
			<td>${machine.settings['pump_duration'] }</td>
			<td><input type="number" id="pump_duration" name="pump_duration"
				step="100" min="0" max="10000"
				value="${machine.settings['pump_duration']}"></td>
		</tr>
		<tr>
			<td>Pump Cycle (ms)</td>
			<td>${machine.settings['pump_cycle'] }</td>
			<td><input type="number" id="pump_cycle" name="pump_cycle"
				step="100" min="0" max="10000"
				value="${machine.settings['pump_cycle']}"></td>
		</tr>
	</table>
	<br> <input type="hidden" name="action" value="updateSettings" />
	<input class="button" type="submit" value="Update Settings"
		class="margin_left" />
</form>



<%
}

Object thisLock = session.getId().intern();
synchronized (thisLock) {
session.setAttribute("machineSettingsViewMessage", null);
}
%>

