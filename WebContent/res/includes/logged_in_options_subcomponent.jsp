<%@ page
	import="com.laygen.beans.Machine"%>

<div class="left-head">
	<p>${user.getUserMsg('lang') }</p>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="${dict.get('logout', lang)}" /> <input
			type="hidden" name="action" value="logout" />
	</form>

	<form class="sideBySide hide-large" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="${dict.get('myMachines', lang)}" /> <input
			type="hidden" name="action" value="viewMyMachines" />
	</form>
	<form class="sideBySide" action="Controller" method="post">
		<input class="button-red" type="submit" value="${dict.get('backupDB', lang)}" /> <input
			type="hidden" name="action" value="backupDB" />
	</form>

</div>

<%
Machine machine = (Machine) session.getAttribute("machine");

if (machine != null) {
%>

<div class="right-head">
	<p>${dict.get('selectedMachineLabel', lang)}
		: ${ machine.info.get('nickname')}
	</p>
	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="${dict.get('info', lang)}" /> <input
			type="hidden" name="action" value="viewMachineInfo" />
	</form>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="${dict.get('settings', lang)}" /> <input
			type="hidden" name="action" value="viewMachineSettings" />
	</form>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="${dict.get('data', lang)}" /> <input
			type="hidden" name="action" value="viewMachineData" />
	</form>

	<form class="sideBySide" action="Controller" method="post">
		<input class="button-white" type="submit"
			value="${dict.get('camera', lang)}" /> <input
			type="hidden" name="action" value="viewCameraPage" />
	</form>
</div>
<%
}
%>