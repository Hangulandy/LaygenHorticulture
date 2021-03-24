<h1>Join our community</h1>
<p>To become a member of the Laygen Farming community, enter your
    information below:</p>

<form action="Controller" method="post">
    <input type="hidden" name="action" value="join" />

    <label class="pad_top">Email:</label>
    <input type="email" name="email" value="${user.email}" required />
    <br>
    <label class="pad_top">Name:</label>
    <input type="text" name="name" value="${user.name}" required />
    <br>

    <label class="pad_top">Username:</label>
    <input type="text" name="userName" value="${user.username}" required />
    <br>
    
    <label class="pad_top">Organization:</label>
    <input type="text" name="organization" value="${user.organization}" required />
    <br>
    
    <label class="pad_top">Password:</label>
    <input type="password" name="pw1" required maxlength="20"/>
    <br>
    
    <label class="pad_top">Confirm PW:</label>
    <input type="password" name="pw2" required maxlength="20"/>
    <br>
    
    <label>&nbsp;</label>
    <input class="button" type="submit" value="Join Now" class="margin_left" />
</form>

<hr/>
