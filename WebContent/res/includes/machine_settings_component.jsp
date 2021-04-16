<%@ page
	import="com.laygen.beans.Machine"%>

<%
boolean lightChecked = false;
boolean heaterChecked = false;
boolean fanChecked = false;
boolean uvcChecked = false;
boolean pumpChecked = false;
boolean cameraChecked = false;
String lightOn = "Off";
String heaterOn = "Off";
String fanOn = "Off";
String uvcOn = "Off";
String pumpOn = "Off";
String cameraOn = "Off";
Machine machine = (Machine) session.getAttribute("machine");

if (machine != null) {
	if (machine.getSettings().get("light_on") != null && machine.getSettings().get("light_on").equalsIgnoreCase("1")) {
		lightChecked = true;
		lightOn = "On";
	}
	if (machine.getSettings().get("heater_on") != null && machine.getSettings().get("heater_on").equalsIgnoreCase("1")) {
		heaterChecked = true;
		heaterOn = "On";
	}
	if (machine.getSettings().get("fan_on") != null && machine.getSettings().get("fan_on").equalsIgnoreCase("1")) {
		fanChecked = true;
		fanOn = "On";
	}
	if (machine.getSettings().get("uvc_on") != null && machine.getSettings().get("uvc_on").equalsIgnoreCase("1")) {
		uvcChecked = true;
		uvcOn = "On";
	}
	if (machine.getSettings().get("pump_on") != null && machine.getSettings().get("pump_on").equalsIgnoreCase("1")) {
		pumpChecked = true;
		pumpOn = "On";
	}
	if (machine.getSettings().get("camera_on") != null
	&& machine.getSettings().get("camera_on").equalsIgnoreCase("1")) {
		cameraChecked = true;
		cameraOn = "On";
	}
%>



<!-- This will only show if machine != null because it is inside the code block following the if statement -->
<h1>${dict.get('machineSettingsHeading', lang)}</h1>
<hr>
<form action="Controller" method="get">
	<div class="small-space"></div>
	<h2>${dict.get('growSettingsHeading', lang)}</h2>
	<table>
		<tr>
			<th class="left">${dict.get('itemLabel', lang)}</th>
			<th>${dict.get('valueLabel', lang)}</th>
			<th>${dict.get('adjustLabel', lang)}</th>
		</tr>
		<tr>
			<td class="left">${dict.get('lightToggleLabel', lang)}</td>
			<td><%=lightOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="light_on" name="light_on" value="1" <%if (lightChecked) {%>
					checked <%}%> /> On</label>   <label class="radio-label"><input
					type="radio" id="light_off" name="light_on" value="0"
					<%if (!lightChecked) {%> checked <%}%> /> Off</label></td>
		</tr>

		<!-- 
			<tr>
				<td class="left">${dict.get('heaterToggleLabel', lang)}</td>
				<td><%=heaterOn%></td>
				<td><label class="radio-label"><input type="radio"
						id="heater_on" name="heater_on" value="1" <%if (heaterChecked) {%>
						checked <%}%> />On</label> <label class="radio-label"><input
						type="radio" id="heater_off" name="heater_on" value="0"
						<%if (!heaterChecked) {%> checked <%}%> />Off</label></td>
			</tr>  -->
		<tr>
			<td class="left">${dict.get('fanToggleLabel', lang)}</td>
			<td><%=fanOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="fan_on" name="fan_on" value="1" <%if (fanChecked) {%> checked
					<%}%> /> On</label> <label class="radio-label"><input type="radio"
					id="fan_off" name="fan_on" value="0" <%if (!fanChecked) {%> checked
					<%}%> /> Off</label></td>
		</tr>
		<!-- 
			<tr>
				<td class="left">${dict.get('uvcToggleLabel', lang)}</td>
				<td><%=uvcOn%></td>
				<td><label class="radio-label"><input type="radio"
						id="uvc_on" name="uvc_on" value="1" <%if (uvcChecked) {%>
						checked <%}%> />On</label> <label class="radio-label"><input
						type="radio" id="uvc_off" name="uvc_on" value="0"
						<%if (!uvcChecked) {%> checked <%}%> />Off</label></td>
			</tr>  -->
		<tr>
			<td class="left">${dict.get('brightnessLabel', lang)}</td>
			<td>${machine.settings['brightness'] }</td>
			<td><input type="number" id="brightness" name="brightness"
				step="1" min="0" max="100"
				value="${machine.settings['brightness'] }"></td>
		</tr>
		<tr>
			<td class="left">${dict.get('pumpToggleLabel', lang)}</td>
			<td><%=pumpOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="pump_on" name="pump_on" value="1" <%if (pumpChecked) {%>
					checked <%}%>> On</label> <label class="radio-label"><input
					type="radio" id="pump_off" name="pump_on" value="0"
					<%if (!pumpChecked) {%> checked <%}%>> Off</label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('pumpDurationLabel', lang)}</td>
			<td>${machine.settings['pump_duration'] }</td>
			<td><input type="number" id="pump_duration" name="pump_duration"
				step="1" min="0" max="1000000"
				value="${machine.settings['pump_duration']}"></td>
		</tr>
		<tr>
			<td class="left">${dict.get('pumpCycleLabel', lang)}</td>
			<td>${machine.settings['pump_cycle'] }</td>
			<td><input type="number" id="pump_cycle" name="pump_cycle"
				step="1" min="0" max="1000000"
				value="${machine.settings['pump_cycle']}"></td>
		</tr>
	</table>
	<h2>${dict.get('cameraSettingsHeading', lang)}</h2>
	<table>
		<tr>
			<th class="left">${dict.get('itemLabel', lang)}</th>
			<th>${dict.get('valueLabel', lang)}</th>
			<th>${dict.get('adjustLabel', lang)}</th>
		</tr>
		<tr>
			<td class="left">${dict.get('cameraToggleLabel', lang)}</td>
			<td><%=cameraOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="camera_on" name="camera_on" value="1" <%if (cameraChecked) {%>
					checked <%}%>> On</label> <label class="radio-label"><input
					type="radio" id="camera_off" name="camera_on" value="0"
					<%if (!cameraChecked) {%> checked <%}%>> Off</label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('cameraIntervalLabel', lang)}</td>
			<td>${machine.settings['camera_interval'] }</td>
			<td><input type="number" id="camera_interval"
				name="camera_interval" step="1" min="0" max="1000000"
				value="${machine.settings['camera_interval'] }"></td>
		</tr>
	</table>

	<input type="hidden" name="action" value="updateSettings" /> <input
		class="button-red" type="submit"
		value="${dict.get('updateButtonLabel', lang)}"
		class="margin_left" />
</form>
<%}%>

