<%@ page import="com.laygen.beans.Machine"%>

<form class="sideBySide" action="Controller" method="post">
    <input class="button" type="submit" value="Logout" />
    <input type="hidden" name="action" value="logout" />
</form>

<form class="sideBySide" action="Controller" method="post">
    <input class="button" type="submit" value="Select Machine" />
    <input type="hidden" name="action" value="viewMyMachines" />
</form>

<% Machine machine = (Machine) session.getAttribute("machine"); 

if (machine != null){%>

<form class="sideBySide" action="Controller" method="post">
    <input class="button" type="submit" value="Info" />
    <input type="hidden" name="action" value="viewMachineInfo" />
</form>

<form class="sideBySide" action="Controller" method="post">
    <input class="button" type="submit" value="Settings" />
    <input type="hidden" name="action" value="viewMachineSettings" />
</form>

<form class="sideBySide" action="Controller" method="post">
    <input class="button" type="submit" value="Data" />
    <input type="hidden" name="action" value="viewMachineData" />
</form>

<form class="sideBySide" action="Controller" method="post">
    <input class="button" type="submit" value="Images" />
    <input type="hidden" name="action" value="viewCameraPage" />
</form>

<%} %>

<p class="sideBySide">${user }</p>


