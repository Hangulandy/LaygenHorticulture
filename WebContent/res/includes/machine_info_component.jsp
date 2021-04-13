
<%@ page import="com.laygen.beans.Machine"%>

<h1>Machine Information</h1>
<form class="sideBySide" action="Controller" method="post">
	<p>Serial Number : ${machine.serialNumber}</p>
	<p>Model Name : ${machine.info['model_name'] }</p>
	<p>Primary Use : ${machine.info['primary_use'] }</p>
	<p>Owner Email : ${machine.info['owner_email'] }</p>
	<p>
		Nickname : <input id="nickname" type="text" name="nickname"
			value="${machine.info['nickname'] }" required />
		<button id="editButton" class="button-red" type="button"
			onclick="toggleNicknameGlyph()">Edit
		</button>
	</p>
	<p>IP Address : ${machine.info['ip'] }</p>
	<p>Port : ${machine.info['port'] }</p>

	<input id="saveButton" class="button-red margin-top" type="submit" value="Save" />
	<input type="hidden" name="action" value="updateMachineInfo" /> <input
		type="hidden" name="selectedMachineId" value="${machine.serialNumber}" />
</form>

<form class="sideBySide margin-top" action="Controller" method="post">
	<input class="button" type="submit" value="Refresh" /> <input
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
		if (saveButton.style.display = "none"){
			saveButton.style.display = "inline";
			nickname.disabled = true;
			editButton.display = "inline";
		}
		if (nickname.disabled){
			nickname.disabled = false;
			editButton.style.display = "none";
		} 
	}
</script>
