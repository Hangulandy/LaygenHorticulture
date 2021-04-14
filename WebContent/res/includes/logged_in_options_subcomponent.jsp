<%@ page
	import="com.laygen.beans.Machine, com.laygen.database.Dictionary"%>

<div class="left-head">
	<p>${user }</p>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="<%=Dictionary.getInstance().get("logout")%>" /> <input
			type="hidden" name="action" value="logout" />
	</form>

	<form class="sideBySide hide-large" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="<%=Dictionary.getInstance().get("myMachines")%>" /> <input
			type="hidden" name="action" value="viewMyMachines" />
	</form>
	<!-- 	<form class="sideBySide" action="Controller" method="post">
		<input class="button-red" type="submit" value="<%=Dictionary.getInstance().get("backupDB")%>" /> <input
			type="hidden" name="action" value="backupDB" />
	</form>  -->

</div>

<%
Machine machine = (Machine) session.getAttribute("machine");

if (machine != null) {
%>

<div class="right-head">
	<p><%=Dictionary.getInstance().get("selectedMachineLabel")%>
		: ${ machine.serialNumber}
	</p>
	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="<%=Dictionary.getInstance().get("info")%>" /> <input
			type="hidden" name="action" value="viewMachineInfo" />
	</form>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="<%=Dictionary.getInstance().get("settings")%>" /> <input
			type="hidden" name="action" value="viewMachineSettings" />
	</form>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="<%=Dictionary.getInstance().get("data")%>" /> <input
			type="hidden" name="action" value="viewMachineData" />
	</form>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="<%=Dictionary.getInstance().get("camera")%>" /> <input
			type="hidden" name="action" value="viewCameraPage" />
	</form>
</div>
<%
}
%>