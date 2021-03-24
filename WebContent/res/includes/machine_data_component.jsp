
<h1>Machine Data</h1>

<%@ page import="com.laygen.beans.Message, java.util.TreeSet"%>

<%
TreeSet<Message> readings = (TreeSet<Message>) session.getAttribute("readings");
%>

<table>
	<tr>
		<th>Sensor</th>
		<th>Type</th>
		<th>Time</th>
		<th>Reading</th>
	</tr>

	<%
	for (Message reading : readings) {
	%>
	<tr>
		<td><%=reading.getSensor()%></td>
		<td><%=reading.getColumnName()%></td>
		<td><%=reading.getTime()%></td>
		<td><%=reading.getValue()%></td>
	</tr>

	<%
	}
	%>
</table>
