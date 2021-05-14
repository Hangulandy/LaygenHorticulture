<h1 class="sideBySide">${dict.get('registerMachineHeading', lang)}</h1><h3 class="sideBySide">${dict.get(message, lang) }</h3>
<hr>
<br>
<form action="Controller" method="post">
	<table class="transparent">
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('serialNumberLabel', lang)}:</label></td>
			<td class="transparent"><input type="text" name="serialNumber" value="${machine.serialNumber}" required /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('registrationKeyLabel', lang)}:</label></td>
			<td class="transparent"><input type="password" name="registrationKey" required min=5 max=20 /></td>
		</tr>
		<tr class="transparent">
		<td class="transparent"></td>
		<td class="transparent"><input class="button" type="submit" value="${dict.get('registerLabel', lang)}" class="margin_left" /></td>
		</tr>
	</table>
	<input type="hidden" name="action" value="registerMachine" /> 
</form>