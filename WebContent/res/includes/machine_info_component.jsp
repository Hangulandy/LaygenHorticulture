
<%@ page import="com.laygen.beans.Machine, com.laygen.database.Dictionary"%>

<h1><%=Dictionary.getInstance().get("machineInfoHeading")%></h1>
<hr>
<br>
<form class="sideBySide" action="Controller" method="post">
	<p><%=Dictionary.getInstance().get("serialNumberLabel")%> : ${machine.serialNumber}</p>
	<p><%=Dictionary.getInstance().get("modelLabel")%> : ${machine.info['model_name'] }</p>
	<p><%=Dictionary.getInstance().get("primaryUseLabel")%> : ${machine.info['primary_use'] }</p>
	<p><%=Dictionary.getInstance().get("ownerEmail")%> : ${machine.info['owner_email'] }</p>
	<p>
		<%=Dictionary.getInstance().get("nickname")%> : <input id="nickname" type="text" name="nickname"
			value="${machine.info['nickname'] }" required />
		<button id="editButton" class="button-red" type="button"
			onclick="toggleNicknameGlyph()"><%=Dictionary.getInstance().get("editButtonLabel")%></button>
	</p>
	<p><%=Dictionary.getInstance().get("ipAddressLabel")%> : ${machine.info['ip'] }</p>
	<p><%=Dictionary.getInstance().get("portLabel")%> : ${machine.info['port'] }</p>

	<input id="saveButton" class="button-red margin-top" type="submit"
		value="<%=Dictionary.getInstance().get("saveButtonLabel")%>" /> <input type="hidden" name="action"
		value="updateMachineInfo" /> <input type="hidden"
		name="selectedMachineId" value="${machine.serialNumber}" />
</form>
<br>
<form class="sideBySide margin-top" action="Controller" method="post">
	<input class="button" type="submit" value="<%=Dictionary.getInstance().get("refresh")%>" /> <input
		type="hidden" name="action" value="selectMachine" /> <input
		type="hidden" name="selectedMachineId" value="${machine.serialNumber}" />
</form>

<script>
	var nickname = document.getElementById("nickname");
	nickname.disabled = true;

	var editButton = document.getElementById("editButton");

	var saveButton = document.getElementById("saveButton");
	saveButton.style.display = "none";

	function toggleNicknameGlyph() {
		if (saveButton.style.display = "none") {
			saveButton.style.display = "inline";
			nickname.disabled = true;
			editButton.display = "inline";
		}
		if (nickname.disabled) {
			nickname.disabled = false;
			editButton.style.display = "none";
		}
	}
</script>
