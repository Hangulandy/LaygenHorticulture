
<%@ page
	import="com.laygen.beans.Machine"%>

<h1>${dict.get('machineInfoHeading', lang)}</h1>
<hr>
<div class="small-space"></div>
<form class="sideBySide" action="Controller" method="post">
	<p>${dict.get('serialNumberLabel', lang)}
		: ${machine.serialNumber}
	</p>
	<p>${dict.get('modelLabel', lang)}
		: ${machine.info['model_name'] }
	</p>
	<p>${dict.get('primaryUseLabel', lang)}
		: ${machine.info['primary_use'] }
	</p>
	<p>${dict.get('ownerEmail', lang)}
		: ${machine.info['owner_email'] }
	</p>
	<p>
		${dict.get('nickname', lang)}
		: <input id="nickname" type="text" name="nickname"
			value="${machine.info['nickname'] }" required />
		<button id="editButton" class="button-red" type="button" onclick="toggleNicknameGlyph()">${dict.get('editButtonLabel', lang)}</button>
	</p>
	<p>${dict.get('ipAddressLabel', lang)}
		: ${machine.info['ip'] }
	</p>
	<p>${dict.get('portLabel', lang)}
		: ${machine.info['port'] }
	</p>
<div class="small-space"></div>
	<input id="saveButton" class="button-red margin-top" type="submit"
		value="${dict.get('saveButtonLabel', lang)}" /> <input
		type="hidden" name="action" value="updateMachineInfo" /> <input
		type="hidden" name="selectedMachineId" value="${machine.serialNumber}" />
</form>
<form class="sideBySide margin-top" action="Controller" method="post">
	<input class="button" type="submit" value="${dict.get('refresh', lang)}" /> <input
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
