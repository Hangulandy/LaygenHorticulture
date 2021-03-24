
<h1>Login</h1>
<p>Enter your login information:</p>


<form action="Controller" method="post">
    <input type="hidden" name="action" value="login" />

    <label class="pad_top">Email:</label>
    <input type="email" name="email" value="${user.email}" required />
    <br>
    <label class="pad_top">Password:</label>
    <input type="password" name="password" required min=5 max=20 />
    <br>
    <input class="button" type="submit" value="Login" class="margin_left" />
</form>

<hr />