<h1>${dict.get('joinHeading', lang)}</h1>
<hr>
<h3>${dict.get('joinPrompt', lang)}</h3>
<h3>${dict.get(message, lang)}</h3>
<form action="Controller" method="post">
	<table class="transparent">
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('emailLabel', lang)}:</label></td>
			<td class="transparent"><input type="email" name="email"
				value="${user.email}" required /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('nameLabel', lang)}:</label></td>
			<td class="transparent"><input type="text" name="name"
				value="${user.name}" required /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('usernameLabel', lang)}:</label></td>
			<td class="transparent"><input type="text" name="userName"
				value="${user.username}" required /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('organizationLabel', lang)}:</label></td>
			<td class="transparent"><input type="text" name="organization"
				value="${user.organization}" required /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('passwordLabel', lang)}:</label></td>
			<td class="transparent"><input type="password" name="pw1"
				required maxlength="20" /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('confirmPasswordLabel', lang)}:</label></td>
			<td class="transparent"><input type="password" name="pw2"
				required maxlength="20" /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"></td>
			<td class="transparent"><input class="button" type="submit"
				value="${dict.get('submitLabel', lang)}" /></td>
		</tr>

	</table>
	<input type="hidden" name="action" value="join" />
</form>