<h1>${dict.get('joinHeading', lang)}</h1>
<hr>
<h3>${dict.get('joinPrompt', lang)}</h3>
<br>

<form action="Controller" method="post">
	<input type="hidden" name="action" value="join" /> <label
		class="pad_top">${dict.get('emailLabel', lang)}:</label> <input
		type="email" name="email" value="${user.email}" required /> <br>
	<br> <label class="pad_top">${dict.get('nameLabel', lang)}:</label>
	<input type="text" name="name" value="${user.name}" required /> <br>
	<br> <label class="pad_top">${dict.get('usernameLabel', lang)}:</label>
	<input type="text" name="userName" value="${user.username}" required />
	<br>
	<br> <label class="pad_top">${dict.get('organizationLabel', lang)}:</label>
	<input type="text" name="organization" value="${user.organization}"
		required /> <br>
	<br> <label class="pad_top">${dict.get('passwordLabel', lang)}:</label>
	<input type="password" name="pw1" required maxlength="20" /> <br>
	<br> <label class="pad_top">${dict.get('confirmPasswordLabel', lang)}:</label>
	<input type="password" name="pw2" required maxlength="20" /> <br>
	<br> <input class="button" type="submit"
		value="${dict.get('submitLabel', lang)}" />
</form>