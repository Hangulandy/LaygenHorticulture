<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="./../css/normalize.css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.0.0/Chart.min.js"></script>
<link rel="stylesheet" href="./../css/style.css" />
<title>Data View</title>
</head>

<body>


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


	<%
	Machine machine = (Machine) session.getAttribute("machine");
	Dictionary dict1 = (Dictionary) session.getAttribute("dict");
	String lang1 = (String) session.getAttribute("lang");
	String sensorName = (String) session.getAttribute("sensorName");

	Gson gsonObj = new Gson();
	Map<Object, Object> map = null;
	List<Map<Object, Object>> list = null;
	SimpleDateFormat parser = new SimpleDateFormat("yyyyMMddHHmmss");
	parser.setTimeZone(TimeZone.getTimeZone("JST"));
	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");

	String dataPoints = null;
	Sensor sensor = null;

	list = new ArrayList<Map<Object, Object>>();
	float val;

	sensor = machine.getSensors().get(sensorName);
	if (sensor != null && sensor.getReadings() != null) {
		for (Message reading : sensor.getReadings()) {
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
	}

	list.sort(new Comparator<Map<Object, Object>>() {
		@Override
		public int compare(Map<Object, Object> m1, Map<Object, Object> m2) {
			return m1.get("date").toString().compareTo(m2.get("date").toString());
		}
	});

	dataPoints = gsonObj.toJson(list);
	%>

	<div class="chart-container-large">
		<canvas id="canvas" class="display-chart"></canvas>

		<script>
		var dataPoints = <%out.print(dataPoints);%>;

		var labels = dataPoints.map(function(e) {
			return e.date;
		});
		var data = dataPoints.map(function(e) {
			return e.value;
		});
		
		var chartId = canvas;

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
							display: true,
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
	</div>
</body>

</html>