<h1>${dict.get('login', lang)}</h1>
<hr>
<br>
<form action="Controller" method="post">
	<table class="transparent">
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('emailLabel', lang)}:</label></td>
			<td class="transparent"><input type="email" name="email" value="${user.email}" required /></td>
		</tr>
		<tr class="transparent">
			<td class="transparent"><label class="pad_top">${dict.get('passwordLabel', lang)}:</label></td>
			<td class="transparent"><input type="password" name="password" required min=5 max=20 /></td>
		</tr>
		<tr class="transparent">
		<td class="transparent"></td>
		<td class="transparent"><input class="button" type="submit" value="${dict.get('login', lang)}" class="margin_left" /></td>
		</tr>
	</table>
	<input type="hidden" name="action" value="login" /> 
</form>

<h3>${dict.get(message, lang) }</h3>