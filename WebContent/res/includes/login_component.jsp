<h1>${dict.get('login', lang)}</h1>
<hr>
<br>
<form action="Controller" method="post">
	<input type="hidden" name="action" value="login" /> <label
		class="pad_top">${dict.get('emailLabel', lang)}:</label> <input
		type="email" name="email" value="${user.email}" required /> <br>
	<br> <label class="pad_top">${dict.get('passwordLabel', lang)}:</label>
	<input type="password" name="password" required min=5 max=20 /> <br>
	<br> <input class="button" type="submit"
		value="${dict.get('login', lang)}" class="margin_left" />
</form>
