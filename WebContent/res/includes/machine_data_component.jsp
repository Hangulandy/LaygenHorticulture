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

<div class="col-md-4 col-sm-4 col-xs-12">
	<div>
		<h1><%=Dictionary.getInstance().get("sensorHeading")%></h1>
		<%
		Machine machine = (Machine) session.getAttribute("machine");
		%>
	</div>
	<!-- label block -->
	<div>
		<label class="select_label"><%=Dictionary.getInstance().get("chooseSensorPrompt")%>:</label>
	</div>
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
					<%if (machine.getSelectedSensor() != null
						&& machine.getSelectedSensor().getName().equalsIgnoreCase(key)) {%>
					selected="selected" <%}%>><%=key%> :
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
			<br class="not-mobile"> <input class="button" type="submit"
				value="<%=Dictionary.getInstance().get("select")%>" /> <input
				type="hidden" name="action" value="viewMachineData" />
		</form>
	</div>
</div>

<!-- Selected Sensor View div -->
<div class="col-md-4 col-sm-8 col-xs-12">
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
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
	float val;
	for (Message reading : machine.getSelectedSensor().getReadings()) {
		map = new HashMap<Object, Object>();

		try {
			Date date = formatter.parse(reading.getTime());
			map.put("label", date);
			val = Float.parseFloat(reading.getValue());
		} catch (Exception e) {
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
						text : "<%=machine.getSelectedSensor().getType()%> on sensor <%=machine.getSelectedSensor().getName()%> "
					},
					axisX : {
						title : "Time Stamp"
					},
					axisY : {
						title : "<%=machine.getSelectedSensor().getType()%>",
							includeZero : true
						},
						data : [ {
							type : "line",
							yValueFormatString : "#.#",
							dataPoints :
	<%out.print(dataPoints);%>
		} ]
					});
			chart.render();
		}
	</script>
	<div id="chartContainer"></div>
	<script src="./res/js/canvasjs.min.js"></script>
	<%
	} else {
	%>
	<p><%=Dictionary.getInstance().get("noData")%>
		:
		<%=machine.getSelectedSensor().getName()%></p>
	<%
	}
	%>
	<%
	}
	%>
</div>