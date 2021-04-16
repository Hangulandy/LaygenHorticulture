<div class="left-head">
	<br>
	<form class="sideBySide" action="Controller">
		<input type="hidden" name="action" value="redirectToJoin" /> <input
			class="button-white" type="submit" value="${dict.get('join', lang)}" />
	</form>

	<form class="sideBySide" action="Controller">
	<input type="hidden" name="action" value="home" /> 
		<input class="button-white" type="submit" value="${dict.get('login', lang)}" />
	</form>
</div>