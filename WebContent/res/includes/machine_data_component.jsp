<%@ page import="com.laygen.beans.Machine"%>
<%@ page import="com.laygen.beans.Sensor"%>
<%@ page import="com.laygen.beans.Message"%>
<%@ page import="com.laygen.database.Dictionary"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.TimeZone"%>

<h1>${dict.get('sensorHeading', lang)}</h1>
<hr>
<div class="small-space"></div>
<div id="data-selector">
	<%
	Machine machine = (Machine) session.getAttribute("machine");
	Dictionary dict1 = (Dictionary) session.getAttribute("dict");
	String lang1 = (String) session.getAttribute("lang");
	%>
	<!-- label block -->

	<form class="sideBySide" action="Controller" method="post">


		<%
			String startDate;
			String startTime;
			String endDate;
			String endTime;

			// Order of priority is 1) user-chosen start date 2) plant date 3) minimum date 
			if (machine.getStartDate() != null) {
				startDate = machine.getStartDate();
			} else if (machine.getSettings().get("plant_date") != null) {
				startDate = machine.getSettings().get("plant_date");
			} else {
				startDate = "2021-01-01";
			}

			// Order of priority is 1) user-chosen start time 2) midnight
			if (machine.getStartTime() != null) {
				startTime = machine.getStartTime();
			} else {
				startTime = "00:00";
			}

			// Order of priority is 1) user=chosen end date 2) current date
			if (machine.getEndDate() != null) {
				endDate = machine.getEndDate();
			} else {
				Date currentTime = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				endDate = sdf.format(currentTime);
			}

			// Order of priority is 1) user-chosen end time 2) before midnight
			if (machine.getEndTime() != null) {
				endTime = machine.getEndTime();
			} else {
				endTime = "23:59";
			}
			%>
		<label>${dict.get('fromLabel', lang)}:</label> <input type="date"
			id="start-date" name="startDate" value="<%=startDate%>"
			min="2021-01-01" max="2030-12-31" required /> <input type="time"
			id="start-time" name="startTime" value="<%=startTime%>" min="00:00"
			max="23:59" required /> <br class="mobile-only"> <br
			class="mobile-only"> <label>${dict.get('toLabel', lang)}:</label>
		<input type="date" id="end-date" name="endDate" value="<%=endDate%>"
			min="2021-01-01" max="2030-12-31" pattern="\d{4}-\d{2}-\d{2}"
			required /> <input type="time" id="end-time" name="endTime"
			value="<%=endTime%>" min="00:00" max="23:59" required /> <br
			class="mobile-only"> <br class="mobile-only"> <input
			class="button" type="submit" value="${dict.get('select', lang)}" />
		<input type="hidden" name="action" value="viewMachineData" />
	</form>
</div>

<%
Gson gsonObj = new Gson();
Map<Object, Object> map = null;
List<Map<Object, Object>> list = null;
SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmmss");
parser.setTimeZone(TimeZone.getTimeZone("JST"));
SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");

String[] types = {"temperature", "humidity", "co2_ppm", "water_level", "ppdf", "lux", "cds"};

String dataPoints = null;
Sensor sensor = null;

int chartNum = 0;
String chartId = null;

for (String type : types) {
	for (String key : machine.getSensors().keySet()) {
		list = new ArrayList<Map<Object, Object>>();
		float val;
		int count = 0;
		chartNum++;
		sensor = machine.getSensors().get(key);
		if (sensor != null && sensor.getReadings() != null && sensor.getType().equalsIgnoreCase(type)) {
	for (Message reading : machine.getSensors().get(key).getReadings()) {
		map = new HashMap<Object, Object>();
		if (count % 10 == 0) {
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
		count++;
	}
	
	list.sort(new Comparator<Map<Object, Object>>(){
	    @Override
	    public int compare(Map<Object, Object> m1, Map<Object, Object> m2) {
	        return m1.get("date").toString().compareTo(m2.get("date").toString());
	     }
	});
	
	dataPoints = gsonObj.toJson(list);
	chartId = String.format("canvas%s", chartNum);
%>
<div class="chart-container">
	<canvas id="<%=chartId%>" class="display-chart"></canvas>

	<script>
		var dataPoints = <%out.print(dataPoints);%>;

		var labels = dataPoints.map(function(e) {
			return e.date;
		});
		var data = dataPoints.map(function(e) {
			return e.value;
		});
		
		var chartId = <%=chartId%>;

		var ctx = eval(chartId).getContext('2d');
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
				animation: {
					duration: 0,
				},
				scales : {
					x : {
						type : 'time',
					},
					xAxes: [{
						ticks: {
							display: false,
						},
						gridLines: {
			                color: "rgba(0, 0, 0, 0)",
			            }
					}],
				},
				title : {
					display : true,
					text : "<%=sensor.getType()%> on sensor <%=dict1.get(sensor.getName(), lang1)%>",
				},
				responsive : true,
			}
		};

		var chart = new Chart(ctx, config);
	</script>
	<br>
	<form class="sideBySide" action="NonLoadingMethodsServlet">
		<input type="hidden" name="sensor" value="<%=key%>" />
		<input type="hidden" name="action" value="downloadData"/>
		<input class="button" type="submit" value="${dict.get('downloadButtonLabel', lang)}" />
	</form>
	<form class="sideBySide" action="ShowLargeChartServlet" target="_blank">
		<input type="hidden" name="sensor" value="<%=key%>" /> 
		<input class="button" type="submit" value="${dict.get('enlargeButtonLabel', lang)}" />
	</form>
	<br>
</div>
<%
}
}
}
%>