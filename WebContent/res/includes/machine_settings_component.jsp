<%@ page import="com.laygen.beans.Machine"%>
<%@ page import="com.laygen.database.Dictionary"%>

<%
boolean waterInValveOpenChecked = false;
boolean lightChecked = false;
boolean heaterChecked = false;
boolean fanChecked = false;
boolean fanAutoChecked = false;
boolean uvcChecked = false;
boolean waterCycleOnChecked = false;
boolean cameraCycleChecked = false;
String waterInValveOpen = "closed";
String lightOn = "offLabel";
String heaterOn = "offLabel";
String fanOn = "offLabel";
String fanAuto = "cont";
String uvcOn = "offLabel";
String waterCycleOn = "offLabel";
String cameraCycleOn = "offLabel";
Machine machine = (Machine) session.getAttribute("machine");

String lang1 = (String) session.getAttribute("lang");


if (machine != null) {
	if (machine.getSettings().get("water_in_valve_on") != null && machine.getSettings().get("water_in_valve_on").equalsIgnoreCase("1")) {
		waterInValveOpenChecked = true;
		waterInValveOpen = "opened";
	}
	if (machine.getSettings().get("light_on") != null && machine.getSettings().get("light_on").equalsIgnoreCase("1")) {
		lightChecked = true;
		lightOn = "onLabel";
	}
	if (machine.getSettings().get("heater_on") != null && machine.getSettings().get("heater_on").equalsIgnoreCase("1")) {
		heaterChecked = true;
		heaterOn = "onLabel";
	}
	if (machine.getSettings().get("fan_on") != null && machine.getSettings().get("fan_on").equalsIgnoreCase("1")) {
		fanChecked = true;
		fanOn = "onLabel";
	}
	if (machine.getSettings().get("fan_auto") != null && machine.getSettings().get("fan_auto").equalsIgnoreCase("1")) {
		fanAutoChecked = true;
		fanAuto = "auto";
	}
	if (machine.getSettings().get("uvc_on") != null && machine.getSettings().get("uvc_on").equalsIgnoreCase("1")) {
		uvcChecked = true;
		uvcOn = "onLabel";
	}
	if (machine.getSettings().get("water_cycle_on") != null && machine.getSettings().get("water_cycle_on").equalsIgnoreCase("1")) {
		waterCycleOnChecked = true;
		waterCycleOn = "onLabel";
	}
	if (machine.getSettings().get("camera_cycle_on") != null
	&& machine.getSettings().get("camera_cycle_on").equalsIgnoreCase("1")) {
		cameraCycleChecked = true;
		cameraCycleOn = "onLabel";
	}
%>



<!-- This will only show if machine != null because it is inside the code block following the if statement -->
<h1 class="sideBySide">${dict.get('machineSettingsHeading', lang)}</h1>
<form class="sideBySide margin-top" action="Controller" method="post">
	<input class="button" type="submit"
		value="${dict.get('refresh', lang)}" /> <input type="hidden"
		name="action" value="viewMachineSettings" />
</form>
<hr>



<!-- This is the section for Grow Settings -->
<div class="small-space"></div>
<h2>${dict.get('growSettingsHeading', lang)}</h2>
<form action="Controller" method="get">
	<table>
		<colgroup span="3">
			<col class="col-left"></col>
			<col class="col-mid"></col>
			<col class="col-right"></col>
		</colgroup>
		<tr>
			<th class="left">${dict.get('itemLabel', lang)}</th>
			<th>${dict.get('valueLabel', lang)}</th>
			<th>${dict.get('adjustLabel', lang)}</th>
		</tr>
		<tr>
			<td class="left">${dict.get('plant_date', lang)}</td>
			<td>${machine.settings.get('plant_date')}</td>
			<td><input type="date" id="plant_date" name="plant_date"
				value="${machine.settings.get('plant_date')}" min="2021-01-01"
				max="2030-12-31" pattern="\d{4}-\d{2}-\d{2}"
				placeholder="yyyy-mm-dd" required /></td>
		</tr>
	</table>
	<input type="hidden" name="action" value="updateGrowSettings" /> <input
		class="button-red" type="submit"
		value="${dict.get('updateButtonLabel', lang)}" class="margin_left" />
</form>



<!-- This is the section for Water Settings -->
<div class="small-space"></div>
<h2>${dict.get('waterSettingsHeading', lang)}</h2>
<form action="Controller" method="get">
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
	<table>
		<colgroup span="3">
			<col class="col-left"></col>
			<col class="col-mid"></col>
			<col class="col-right"></col>
		</colgroup>
		<tr>
			<th class="left">${dict.get('itemLabel', lang)}</th>
			<th>${dict.get('valueLabel', lang)}</th>
			<th>${dict.get('adjustLabel', lang)}</th>
		</tr>
		<tr>
			<td class="left">${dict.get('water_in_valve_on', lang)}</td>
			<td><%=Dictionary.getInstance().get(waterInValveOpen, lang1) %></td>
			<td><label class="radio-label"><input type="radio"
					id="water_in_valve_on" name="water_in_valve_on" value="1"
					<%if (waterInValveOpenChecked) {%> checked <%}%> /> <%=Dictionary.getInstance().get("open", lang1) %></label><label
				class="radio-label"><input type="radio"
					id="water_in_valve_off" name="water_in_valve_on" value="0"
					<%if (!waterInValveOpenChecked) {%> checked <%}%> /> <%=Dictionary.getInstance().get("close", lang1) %></label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('water_cycle_on', lang)}</td>
			<td><%=Dictionary.getInstance().get(waterCycleOn, lang1)%></td>
			<td><label class="radio-label"><input type="radio"
					id="water_cycle_on" name="water_cycle_on" value="1"
					<%if (waterCycleOnChecked) {%> checked <%}%>> <%=Dictionary.getInstance().get("turnOnLabel", lang1) %></label><label
				class="radio-label"><input type="radio" id="water_cycle_off"
					name="water_cycle_on" value="0" <%if (!waterCycleOnChecked) {%>
					checked <%}%>> <%=Dictionary.getInstance().get("turnOffLabel", lang1) %></label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('water_cycle_duration', lang)}</td>
			<td>${machine.settings['water_cycle_duration'] }</td>
			<td><input type="number" id="water_cycle_duration"
				name="water_cycle_duration" step="1" min="0" max="3600"
				value="${machine.settings['water_cycle_duration']}"></td>
		</tr>
		<tr>
			<td class="left">${dict.get('water_cycle_period', lang)}</td>
			<td><%=hours%> h: <%=minutes%> m</td>
			<td><input type="number" id="water_cycle_period_hours"
				name="water_cycle_period_hours" step="1" min="0" max="100"
				value="<%=hours%>"> h <input type="number"
				id="water_cycle_perdiod_minutes" name="water_cycle_period_minutes"
				step="1" min="0" max="59" value="<%=minutes%>"> m</td>
		</tr>
	</table>
	<input type="hidden" name="action" value="updateWaterSettings" /> <input
		class="button-red" type="submit"
		value="${dict.get('updateButtonLabel', lang)}" class="margin_left" />
</form>



<!-- This is the section for Light Settings -->
<div class="small-space"></div>
<h2>${dict.get('lightSettingsHeading', lang)}</h2>
<form action="Controller" method="get">
	<table>
		<colgroup span="3">
			<col class="col-left"></col>
			<col class="col-mid"></col>
			<col class="col-right"></col>
		</colgroup>
		<tr>
			<th class="left">${dict.get('itemLabel', lang)}</th>
			<th>${dict.get('valueLabel', lang)}</th>
			<th>${dict.get('adjustLabel', lang)}</th>
		</tr>
		<tr>
			<td class="left">${dict.get('light_on', lang)}</td>
			<td><%=Dictionary.getInstance().get(lightOn, lang1) %></td>
			<td><label class="radio-label"><input type="radio"
					id="light_on" name="light_on" value="1" <%if (lightChecked) {%>
					checked <%}%> /> <%=Dictionary.getInstance().get("turnOnLabel", lang1) %></label>
				<label class="radio-label"><input type="radio"
					id="light_off" name="light_on" value="0" <%if (!lightChecked) {%>
					checked <%}%> /> <%=Dictionary.getInstance().get("turnOffLabel", lang1) %></label></td>
		</tr>
		<tr>
			<td class="left">${dict.get('brightness', lang)}</td>
			<td>${machine.settings['brightness'] }</td>
			<td><input type="number" id="brightness" name="brightness"
				step="5" min="0" max="100"
				value="${machine.settings['brightness'] }"><br
				class="mobile-only"> <br class="mobile-only"> <input
				type="range" step="5" min="0" max="100"
				value="${machine.settings['brightness']}" id="brightnessSlider"></td>
		</tr>
		<tr>
			<td class="left">${dict.get('light_color', lang)}</td>
			<td>${dict.get(machine.settings['light_color'], lang) }</td>
			<td>
				<div class="button-container">
					<%
				String[] order = {"red", "green", "blue", "yellow", "magenta", "cyan", "custom1", "custom2", "custom3"};
					for (String color : order){
						for (String key : machine.getLightColors().keySet()) {
							if (key.equalsIgnoreCase(color)){ %>
					<label class="radio-label"><input type="radio"
						id="<%=key%>-<%=machine.getLightColors().get(key)%>"
						name="light_color" onclick="checkColor(this)" value="<%=key%>"
						<%String checkedLightColor = machine.getSettings().get("light_color");%>
						<%if (checkedLightColor != null && checkedLightColor.equalsIgnoreCase(key)) {%>
						checked <%}%>> <%=Dictionary.getInstance().get(key, lang1)%></label>
					<div class="small-space"></div>

					<%}}} %>
				</div>
			</td>
		</tr>
		<!-- 
			<tr>
				<td class="left">${dict.get('uvc_on', lang)}</td>
				<td><%=uvcOn%></td>
				<td><label class="radio-label"><input type="radio"
						id="uvc_on" name="uvc_on" value="1" <%if (uvcChecked) {%>
						checked <%}%> />On</label> <label class="radio-label"><input
						type="radio" id="uvc_off" name="uvc_on" value="0"
						<%if (!uvcChecked) {%> checked <%}%> />Off</label></td>
			</tr>  -->
	</table>
	<input type="hidden" name="action" value="updateLightSettings" /> <input
		class="button-red" type="submit"
		value="${dict.get('updateButtonLabel', lang)}" class="margin_left" />
</form>

<div id="custom-color-adjuster">
	<div class="small-space"></div>
	<h2>${dict.get('customColorAdjusterHeading', lang)}</h2>
	<form action="Controller" method="get">
		<table id="custom-color-adjuster-table">
			<colgroup span="3">
				<col class="col-left"></col>
				<col class="col-mid"></col>
				<col class="col-right"></col>
			</colgroup>
			<tr>
				<th class="left">${dict.get('itemLabel', lang)}</th>
				<th>${dict.get('valueLabel', lang)}</th>
				<th>${dict.get('adjustLabel', lang)}</th>
			</tr>
			<tr id="redRow" class="custom-color-row">
				<td>${dict.get('red', lang)}</td>
				<td id="currentRed"></td>
				<td><input type="number" id="redInput" name="redValue" step="5"
					min="0" max="100" value="0"><br class="mobile-only"> <br
					class="mobile-only"> <input type="range" step="5" min="0"
					max="100" value="0" id="redSlider"></td>
			</tr>
			<tr id="greenRow" class="custom-color-row">
				<td>${dict.get('green', lang)}</td>
				<td id="currentGreen"></td>
				<td><input type="number" id="greenInput" name="greenValue"
					step="5" min="0" max="100" value="0"><br
					class="mobile-only"> <br class="mobile-only"> <input
					type="range" step="5" min="0" max="100" value="0" id="greenSlider"></td>
			</tr>
			<tr id="blueRow" class="custom-color-row">
				<td>${dict.get('blue', lang)}</td>
				<td id="currentBlue"></td>
				<td><input type="number" id="blueInput" name="blueValue"
					step="5" min="0" max="100" value="0"><br
					class="mobile-only"> <br class="mobile-only"> <input
					type="range" step="5" min="0" max="100" value="0" id="blueSlider"></td>
			</tr>
		</table>
		<input type="hidden" name="action" value="updateCustomColor" /> <input
			id="light-color-hidden-input" type="hidden" name="light_color"
			value="red" /> <input class="button-red" type="submit"
			value="${dict.get('updateButtonLabel', lang)}" class="margin_left" />
	</form>
</div>




<script>
		var brs = document.getElementById("brightnessSlider");
		var br = document.getElementById("brightness");
		
		brs.oninput = function(){
			br.value =  this.value;
		}
		
		br.oninput = function(){
			brs.value = this.value;
		}
		
		var rs = document.getElementById("redSlider");
		var r = document.getElementById("redInput");
		
		rs.oninput = function(){
			r.value =  this.value;
		}
		
		r.oninput = function(){
			rs.value = this.value;
		}
		
		var gs = document.getElementById("greenSlider");
		var g = document.getElementById("greenInput");
		
		gs.oninput = function(){
			g.value =  this.value;
		}
		
		g.oninput = function(){
			gs.value = this.value;
		}
		
		var bls = document.getElementById("blueSlider");
		var bl = document.getElementById("blueInput");
		
		bls.oninput = function(){
			bl.value =  this.value;
		}
		
		bl.oninput = function(){
			bls.value = this.value;
		}
		
		var redRow = document.getElementById("redRow");
		var greenRow = document.getElementById("greenRow");
		var blueRow = document.getElementById("blueRow");
		
		function checkColor(button){
			var parts = button.id.split("-");
			var id = parts[0];
			var value = parts[1];
			var block = document.getElementById("custom-color-adjuster");
			var lightColorInput = document.getElementById("light-color-hidden-input");
			if (id.includes("custom")){
				lightColorInput.value = id;
				block.style.display = "block";
				console.log(id, value);
				
				value = parseInt(value);
				var red = Math.floor(value / 1000000);
				value = value % 1000000;
				var green = Math.floor(value / 1000);
				value = value % 1000;
				var blue = value;
				
				console.log(red, green, blue);
				
				rs.value = red;
				r.value = red;
				gs.value = green;
				g.value = green;
				bls.value = blue;
				bl.value = blue;
				
				redRow.style.display = "none";
				greenRow.style.display = "none";
				blueRow.style.display = "none";
				
				var displayType = "table-row";
				
				if (id.includes("custom1")){
					redRow.style.display = displayType;
					greenRow.style.display = displayType;
					bls.value = 0;
					bl.value = 0;
				}
				
				if (id.includes("custom2")){
					redRow.style.display = displayType;
					blueRow.style.display = displayType;
					gs.value = 0;
					g.value = 0;
				}
				
				if (id.includes("custom3")){
					greenRow.style.display = displayType;
					blueRow.style.display = displayType;
					rs.value = 0;
					r.value = 0;
				}
			} else {
				block.style.display = "none";
			}
		}
		</script>


<!-- This is the section for Air Settings -->
<div class="small-space"></div>
<h2>${dict.get('airSettingsHeading', lang)}</h2>
<form action="Controller" method="get">
	<table>
		<colgroup span="3">
			<col class="col-left"></col>
			<col class="col-mid"></col>
			<col class="col-right"></col>
		</colgroup>
		<tr>
			<th class="left">${dict.get('itemLabel', lang)}</th>
			<th>${dict.get('valueLabel', lang)}</th>
			<th>${dict.get('adjustLabel', lang)}</th>
		</tr>
		<!-- 
			<tr>
				<td class="left">${dict.get('heater_on', lang)}</td>
				<td><%=Dictionary.getInstance().get(heaterOn, lang1) %></td>
				<td><label class="radio-label"><input type="radio"
						id="heater_on" name="heater_on" value="1" <%if (heaterChecked) {%>
						checked <%}%> /><%=Dictionary.getInstance().get("turnOnLabel", lang1) %></label> <label class="radio-label"><input
						type="radio" id="heater_off" name="heater_on" value="0"
						<%if (!heaterChecked) {%> checked <%}%> /><%=Dictionary.getInstance().get("turnOffLabel", lang1) %></label></td>
			</tr>  -->
		<tr>
			<td class="left">${dict.get('fan_on', lang)}</td>
			<td><%=Dictionary.getInstance().get(fanOn, lang1) %></td>
			<td><label class="radio-label"><input type="radio"
					id="fan_on" name="fan_on" value="1" <%if (fanChecked) {%> checked
					<%}%> /> <%=Dictionary.getInstance().get("turnOnLabel", lang1) %></label>
				<label class="radio-label"><input type="radio" id="fan_off"
					name="fan_on" value="0" <%if (!fanChecked) {%> checked <%}%> /> <%=Dictionary.getInstance().get("turnOffLabel", lang1) %></label></td>
		</tr>

		<tr>
			<td class="left">${dict.get('fan_auto', lang)}</td>
			<td><%=Dictionary.getInstance().get(fanAuto, lang1)%></td>
			<td><label class="radio-label"><input type="radio"
					id="fan_auto" name="fan_auto" value="1" <%if (fanAutoChecked) {%>
					checked <%}%> /> <%=Dictionary.getInstance().get("auto", lang1) %></label><label
				class="radio-label"><input type="radio" id="fan_cont"
					name="fan_auto" value="0" <%if (!fanAutoChecked) {%> checked <%}%> />
					<%=Dictionary.getInstance().get("cont", lang1) %></label></td>
		</tr>
		<tr>
			<%String fanHumidity = machine.getSettings().get("fan_humidity");
		int humidity = 0;
		try {
			float f = Float.parseFloat(fanHumidity);
			humidity = (int) Math.floor(f);
		} catch (Exception e){
			// do nothing
		}
		%>
			<td class="left">${dict.get('fan_humidity', lang)}</td>
			<td>${machine.settings['fan_humidity'] }</td>
			<td><input type="number" id="fan_humidity" name="fan_humidity"
				step="5" min="0" max="100" value="<%=humidity%>"><br
				class="mobile-only"> <br class="mobile-only"> <input
				type="range" step="5" min="0" max="100" value="<%=humidity %>"
				id="humiditySlider"></td>
		</tr>
	</table>
	<input type="hidden" name="action" value="updateAirSettings" /> <input
		class="button-red" type="submit"
		value="${dict.get('updateButtonLabel', lang)}" class="margin_left" />
</form>

<script>
	var i = document.getElementById("humiditySlider");
	var j = document.getElementById("fan_humidity");

	i.oninput = function() {
		j.value = this.value;
	}

	j.oninput = function() {
		i.value = this.value;
	}
</script>



<!-- This is the section for Camera Settings -->
<div class="small-space"></div>
<h2>${dict.get('cameraSettingsHeading', lang)}</h2>
<form action="Controller" method="get">
	<table>
		<colgroup span="3">
			<col class="col-left"></col>
			<col class="col-mid"></col>
			<col class="col-right"></col>
		</colgroup>
		<tr>
			<th class="left">${dict.get('itemLabel', lang)}</th>
			<th>${dict.get('valueLabel', lang)}</th>
			<th>${dict.get('adjustLabel', lang)}</th>
		</tr>
		<tr>
			<td class="left">${dict.get('camera_cycle_on', lang)}</td>
			<td><%=Dictionary.getInstance().get(cameraCycleOn, lang1) %></td>
			<td><label class="radio-label"><input type="radio"
					id="camera_cycle_on" name="camera_cycle_on" value="1"
					<%if (cameraCycleChecked) {%> checked <%}%>> <%=Dictionary.getInstance().get("turnOnLabel", lang1) %></label>
				<label class="radio-label"><input type="radio"
					id="camera_cycle_off" name="camera_cycle_on" value="0"
					<%if (!cameraCycleChecked) {%> checked <%}%>> <%=Dictionary.getInstance().get("turnOffLabel", lang1) %></label></td>
		</tr>

		<%
		minutes = 0;
		hours = 0;
		try {
			int period = Integer.parseInt(machine.getSettings().get("camera_cycle_period"));
			minutes = period / 60;
			hours = minutes / 60;
			minutes %= 60;
		} catch (Exception e) {
			// do nothing
		}
		%>

		<tr>
			<td class="left">${dict.get('camera_cycle_period', lang)}</td>
			<td><%=hours%> h: <%=minutes%> m</td>

			<td><input type="number" id="camera_cycle_period_hours"
				name="camera_cycle_period_hours" step="1" min="0" max="100"
				value="<%=hours%>"> h <input type="number"
				id="camera_cycle_perdiod_minutes" name="camera_cycle_period_minutes"
				step="1" min="0" max="59" value="<%=minutes%>"> m</td>
		</tr>
	</table>
	<input type="hidden" name="action" value="updateCameraSettings" /> <input
		class="button-red" type="submit"
		value="${dict.get('updateButtonLabel', lang)}" class="margin_left" />
</form>

<%
}
%>
