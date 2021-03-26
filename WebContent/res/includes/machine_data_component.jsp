<%@ page import="com.laygen.beans.Machine"%>
<%@ page import="com.laygen.beans.Sensor"%>
<%@ page import="com.laygen.beans.Message"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.TimeZone"%>


<h1>Sensors and Data</h1>

<ul>

	<%
	Machine machine = (Machine) session.getAttribute("machine");
	if (machine != null && machine.getSensors() != null) {
		for (String key : machine.getSensors().keySet()) {
	%>
	<li><%=key%> : <%=machine.getSensors().get(key).getType() %></li>
	<%
	}
	}
	%>

</ul>

<div>
	<!-- Image list div -->
	<div>
		<!-- label block -->
		<label>Sensors for this machine:</label>
		<form class="sideBySide" action="Controller" method="post">
			<%
			if (machine != null && machine.getSensors() != null) {
			%>
			<!-- list box -->
			<select name=selectedSensor>
				<%
				for (String key : machine.getSensors().keySet()) {
				%>
				<option value="<%=key%>"
					<%if (machine.getSelectedSensor() != null && machine.getSelectedSensor().getName().equalsIgnoreCase(key)) {%>
					selected="selected" <%}%>><%=key%></option>
				<%
				}
				%>
			</select>
			<%
			} else {
			%>
			<p>No sensors to display</p>
			<%
			}
			%>
			<input class="button" type="submit" value="Select" /> <input
				type="hidden" name="action" value="viewMachineData" />
		</form>

		<!-- div for buttons -->
		<div>
			<!-- View (select) button -->
			<!-- Refresh list button -->
		</div>
	</div>
	<!-- Selected Sensor View div -->
	<div>
		<!-- label block -->
		<%
		if (machine.getSelectedSensor() != null) {
		%>
		<p>
			Selected Sensor :
			<%=machine.getSelectedSensor().getName()%></p>

		<!-- Table View -->
		<%
		if (machine.getSelectedSensor().getReadings() != null && machine.getSelectedSensor().getReadings().size() > 0) {
		%>

		<table>
			<tr>
				<th>TimeStamp</th>
				<th>Value</th>
			</tr>
			<%
			for (Message reading : machine.getSelectedSensor().getReadings()) {
			%>
			<tr>
				<td><%=reading.getTime()%></td>
				<td><%=reading.getValue()%>
			</tr>
			<%
			}
			%>
		</table>

		<!-- Chart View -->
		<%
		Gson gsonObj = new Gson();
		Map<Object, Object> map = null;
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		%>
		<%
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));	
		float val;
		for (Message reading : machine.getSelectedSensor().getReadings()) {
			map = new HashMap<Object, Object>();
			
			
			try {
				Date date = formatter.parse(reading.getTime());
				map.put("label", date);
				val = Float.parseFloat(reading.getValue());
			} catch (Exception e){
				val = 0;
			}
			map.put("y", val);
			list.add(map);
		}

		String dataPoints = gsonObj.toJson(list);
		%>

		<script type="text/javascript">
			window.onload = function() {

				var chart = new CanvasJS.Chart("chartContainer", {
					theme : "light2",
					title : {
						text : "Sensor Data"
					},
					axisX : {
						title : "Time Stamp"
					},
					axisY : {
						title : "Value",
						includeZero : true
					},
					data : [ {
						type : "line",
						yValueFormatString : "#",
						dataPoints :
		<%out.print(dataPoints);%>
			} ]
				});
				chart.render();
			}
		</script>
		<div id="chartContainer" sytle="height: 370px; width: 100%;"></div>
		<script src="./res/js/canvasjs.min.js"></script>

		<%
		}
		%>

		<!-- command buttons -->
		<div>
			<!-- Button for download data? -->
			<!-- Button for table view? -->
			<!-- Button for chart view? -->
		</div>

		<%
		}
		%>


	</div>
</div>