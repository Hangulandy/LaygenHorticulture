<%@ page import="com.laygen.beans.Machine"%>

<%
boolean waterOnChecked = false;
boolean lightChecked = false;
boolean heaterChecked = false;
boolean fanChecked = false;
boolean fanAutoChecked = false;
boolean uvcChecked = false;
boolean waterCycleOnChecked = false;
boolean cameraCycleChecked = false;
String waterOn = "Off";
String lightOn = "Off";
String heaterOn = "Off";
String fanOn = "Off";
String fanAuto = "Cont";
String uvcOn = "Off";
String waterCycleOn = "Off";
String cameraCycleOn = "Off";
Machine machine = (Machine) session.getAttribute("machine");

if (machine != null) {
	if (machine.getSettings().get("water_on") != null && machine.getSettings().get("water_in_valve_on").equalsIgnoreCase("1")) {
		waterOnChecked = true;
		waterOn = "On";
	}
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
	if (machine.getSettings().get("fan_auto") != null && machine.getSettings().get("fan_auto").equalsIgnoreCase("1")) {
		fanAutoChecked = true;
		fanAuto = "Auto";
	}
	if (machine.getSettings().get("uvc_on") != null && machine.getSettings().get("uvc_on").equalsIgnoreCase("1")) {
		uvcChecked = true;
		uvcOn = "On";
	}
	if (machine.getSettings().get("water_cycle_on") != null && machine.getSettings().get("water_cycle_on").equalsIgnoreCase("1")) {
		waterCycleOnChecked = true;
		waterCycleOn = "On";
	}
	if (machine.getSettings().get("camera_cycle_on") != null
	&& machine.getSettings().get("camera_cycle_on").equalsIgnoreCase("1")) {
		cameraCycleChecked = true;
		cameraCycleOn = "On";
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
			<td class="left">${dict.get('plantDateLabel', lang)}</td>
			<td></td>
			<td><input type="date" id="plant_date" name="plant_date"
				value="${machine.settings.get('plant_date')}" min="2021-01-01"
				max="2030-12-31" pattern="\d{4}-\d{2}-\d{2}"
				placeholder="yyyy-mm-dd" required /></td>
		</tr>
		<tr>
			<td class="left">${dict.get('waterOnToggleLabel', lang)}</td>
			<td><%=waterOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="water_in_valve_on" name="water_in_valve_on" value="1"
					<%if (waterOnChecked) {%> checked <%}%> /> On</label> <label
				class="radio-label"><input type="radio"
					id="water_in_valve_off" name="water_in_valve_on" value="0"
					<%if (!waterOnChecked) {%> checked <%}%> /> Off</label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('lightToggleLabel', lang)}</td>
			<td><%=lightOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="light_on" name="light_on" value="1" <%if (lightChecked) {%>
					checked <%}%> /> On</label> <label class="radio-label"><input
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

		<tr>
			<td class="left">${dict.get('fanAutoToggleLabel', lang)}</td>
			<td><%=fanAuto%></td>
			<td><label class="radio-label"><input type="radio"
					id="fan_auto" name="fan_auto" value="1" <%if (fanAutoChecked) {%>
					checked <%}%> /> Auto</label> <label class="radio-label"><input
					type="radio" id="fan_cont" name="fan_auto" value="0"
					<%if (!fanAutoChecked) {%> checked <%}%> /> Cont</label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('fanHumidityLabel', lang)}</td>
			<td>${machine.settings['fan_humidity'] }</td>
			<td><input type="number" id="fan_humidity" name="fan_humidity"
				step="1" min="0" max="100"
				value="${machine.settings['fan_humidity'] }"></td>
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
			<td class="left">${dict.get('lightColorLabel', lang)}</td>
			<td>${machine.settings['light_color'] }</td>
			<td>
				<div class="button-container">
					<%
					for (String key : machine.getLightColors().keySet()) {
					%>
					<label class="radio-label"><input type="radio"
						name="light_color" value="<%=key%>"
						<%String checkedLightColor = machine.getSettings().get("light_color");
if (checkedLightColor != null && checkedLightColor.equalsIgnoreCase(key)) {%>
						checked <%}%>> <%=machine.getLightColors().get(key)%></label>
					<div class="small-space"></div>
					<%
					}
					%>
				</div>
			</td>
		</tr>
		<tr>
			<td class="left">${dict.get('waterCycleOnToggleLabel', lang)}</td>
			<td><%=waterCycleOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="water_cycle_on" name="water_cycle_on" value="1"
					<%if (waterCycleOnChecked) {%> checked <%}%>> On</label> <label
				class="radio-label"><input type="radio" id="water_cycle_off"
					name="water_cycle_on" value="0" <%if (!waterCycleOnChecked) {%>
					checked <%}%>> Off</label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('waterCycleDurationLabel', lang)}</td>
			<td>${machine.settings['water_cycle_duration'] }</td>
			<td><input type="number" id="water_cycle_duration"
				name="water_cycle_duration" step="1" min="0" max="3600"
				value="${machine.settings['water_cycle_duration']}"></td>
		</tr>

		<%
		int minutes = 0;
		int hours = 0;
		try {
			int period = Integer.parseInt(machine.getSettings().get("water_cycle_period"));
			minutes = period / 60;
			hours = minutes / 60;
			minutes %= 60;
		} catch (Exception e) {
			// do nothing
		}
		%>

		<tr>
			<td class="left">${dict.get('waterCyclePeriodLabel', lang)}</td>
			<td><%=hours%> h: <%=minutes%> m</td>
			<td><input type="number" id="water_cycle_period_hours"
				name="water_cycle_period_hours" step="1" min="0" max="100"
				value="<%=hours%>"> h <input type="number"
				id="water_cycle_perdiod_minutes" name="water_cycle_period_minutes"
				step="1" min="0" max="59" value="<%=minutes%>"> m</td>
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
			<td class="left">${dict.get('cameraCycleToggleLabel', lang)}</td>
			<td><%=cameraCycleOn%></td>
			<td><label class="radio-label"><input type="radio"
					id="camera_cycle_on" name="camera_cycle_on" value="1"
					<%if (cameraCycleChecked) {%> checked <%}%>> On</label> <label
				class="radio-label"><input type="radio"
					id="camera_cycle_off" name="camera_cycle_on" value="0"
					<%if (!cameraCycleChecked) {%> checked <%}%>> Off</label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('cameraCyclePeriodLabel', lang)}</td>
			<td>${machine.settings['camera_cycle_period'] }</td>
			<td><input type="number" id="camera_cycle_period"
				name="camera_cycle_period" step="1" min="0" max="1000000"
				value="${machine.settings['camera_cycle_period'] }"></td>
		</tr>
	</table>

	<input type="hidden" name="action" value="updateSettings" /> <input
		class="button-red" type="submit"
		value="${dict.get('updateButtonLabel', lang)}" class="margin_left" />
</form>
<%}%>

