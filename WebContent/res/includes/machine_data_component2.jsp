<%@ page import="com.laygen.beans.Machine"%>
<%@ page import="com.laygen.beans.Sensor"%>
<%@ page import="com.laygen.beans.Message"%>
<%@ page import="com.laygen.database.Dictionary"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.TimeZone"%>

<h1><%=Dictionary.getInstance().get("sensorHeading")%></h1>
<hr>
<div id="data-selector" class="selector">
	<%
	Machine machine = (Machine) session.getAttribute("machine");
	%>
	<!-- label block -->
	<h3><%=Dictionary.getInstance().get("chooseSensorPrompt")%>:
	</h3>
	<div>
		<form class="sideBySide" action="Controller" method="post">
			<%
			if (machine != null && machine.getSensors() != null) {
			%>
			<!-- list box -->
			<select name=selectedSensor size="10">
				<%
				for (String key : machine.getSensors().keySet()) {
				%>
				<option value="<%=key%>"
					<%String value = "0";
				if (machine.getReadings() != null && machine.getReadings().get(key) != null) {
					value = machine.getReadings().get(key);
				}%>
					<%if (machine.getSelectedSensor() != null && machine.getSelectedSensor().getName().equalsIgnoreCase(key)) {%>
					selected="selected" <%}%>>
					<%=key%> :
					<%=value%></option>
				<%
				}
				%>
			</select>
			<%
			} else {
			%>
			<p><%=Dictionary.getInstance().get("noSensors")%></p>
			<%
			}
			%>
			<br>

			<%
			String startDate = "2021-01-01";
			String startTime = "00:00";
			String endDate = "2029-12-31";
			String endTime = "23:59";
			if (machine.getStartDate() != null) {
				startDate = machine.getStartDate();
			}
			if (machine.getStartTime() != null) {
				startTime = machine.getStartTime();
			}
			if (machine.getEndDate() != null) {
				endDate = machine.getEndDate();
			}
			if (machine.getEndTime() != null) {
				endTime = machine.getEndTime();
			}
			%>

			<p>From:</p>
			<input type="date" id="start-date" name="startDate"
				value="<%=startDate%>" min="2021-01-01" max="2030-12-31" required />
			<input type="time" id="start-time" name="startTime"
				value="<%=startTime%>" min="00:00" max="23:59" required />
			<p>To:</p>
			<input type="date" id="end-date" name="endDate" value="<%=endDate%>"
				min="2021-01-01" max="2030-12-31" pattern="\d{4}-\d{2}-\d{2}"
				required /> <input type="time" id="end-time" name="endTime"
				value="<%=endTime%>" min="00:00" max="23:59" required /> <br>
			<br> <input class="button" type="submit"
				value="<%=Dictionary.getInstance().get("select")%>" /> <input
				type="hidden" name="action" value="viewMachineData" />
		</form>
	</div>
</div>

<div class="img-container">
	<!-- label block -->
	<%
	if (machine.getSelectedSensor() != null) {
	%>
	<%
	if (machine.getSelectedSensor().getReadings() != null && machine.getSelectedSensor().getReadings().size() > 0) {
	%>
	<!-- Chart View -->
	<%
	Gson gsonObj = new Gson();
	Map<Object, Object> map = null;
	List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
	%>
	<%
	SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmmss");
	parser.setTimeZone(TimeZone.getTimeZone("JST"));
	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
	float val;
	for (Message reading : machine.getSelectedSensor().getReadings()) {
		map = new HashMap<Object, Object>();

		try {
			Date date = parser.parse(reading.getTime());
			String output = formatter.format(date);
			map.put("date", output);
			val = Float.parseFloat(reading.getValue());
		} catch (Exception e) {
			val = 0;
		}
		map.put("value", val);
		list.add(map);
	}

	String dataPoints = gsonObj.toJson(list);
	%>
	<canvas id="canvas" class="display-chart"></canvas>

	<script>
		var dataPoints =
	<%out.print(dataPoints);%>
		;

		var labels = dataPoints.map(function(e) {
			return e.date;
		});
		var data = dataPoints.map(function(e) {
			return e.value;
		});

		var ctx = canvas.getContext('2d');
		var config = {
			type : 'line', // bar, horizontalBar, pie, line, doughnut, radar
			data : {
				labels : labels,
				datasets : [ {
					label : 'Data',
					data : data,
				} ]
			},
			options : {
				scales : {
					x : {
						type : 'time',
					},
				},
				title : {
					display : true,
					text : "<%=machine.getSelectedSensor().getType()%> on sensor <%=machine.getSelectedSensor().getName()%>",
				},
				responsive : true,
			}
		};

		var chart = new Chart(ctx, config);
	</script>
	<br>
	<form class="sideBySide" action="DownloadSpreadsheetServlet">
		<input class="button" type="submit" value="<%=Dictionary.getInstance().get("downloadButtonLabel")%>" />
	</form>
	<%
	} else {
	%>
	<h3><%=Dictionary.getInstance().get("noData")%>
		:
		<%=machine.getSelectedSensor().getName()%></h3>
	<%
	}
	%>
	<%
	}
	%>
</div>