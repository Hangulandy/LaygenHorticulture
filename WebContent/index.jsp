
<%@ include file="./res/includes/header_component.jsp"%>


<!-- Must start with the login component if there is no logged in user -->

<p>${message }</p>

<%
String viewComponent = (String) session.getAttribute("viewComponent");
if ((user == null || !user.isLoggedIn()) && viewComponent == null) {
%>
<%@ include file="./res/includes/login_component.jsp"%>
<%
} else {
%>
<!-- Here we should choose other components to display in the middle based on the return from the servlet
	TODO - this should be able to just take a string from the servlet and then include the file with string
	should not have to call each one individually here -->

<%
if (viewComponent != null) {
	if (viewComponent.equalsIgnoreCase("machineInfo")) {
%>
<%@ include file="./res/includes/machine_info_component.jsp"%>
<%
}
if (viewComponent.equalsIgnoreCase("machineSettings")) {
%>
<%@ include file="./res/includes/machine_settings_component.jsp"%>
<%
}
if (viewComponent.equalsIgnoreCase("join")) {
%>

<%@ include file="./res/includes/join_component.jsp"%>
<%
}
if (viewComponent.equalsIgnoreCase("machineData")) {
%>
<%@ include file="./res/includes/machine_data_component.jsp"%>
<%
}
if (viewComponent.equalsIgnoreCase("cameraPage")){ %>
<%@ include file="./res/includes/camera_page_component.jsp"%>
<% } 
} else { %>

<%@ include file="./res/includes/machine_list_component.jsp"%>

<% } %>

<%
}
%>

<!-- Erase the message so that it does not persist in page reloads -->
<%
synchronized (lock) {
	session.setAttribute("message", null);
	session.setAttribute("errorMessage", null);
	session.setAttribute("viewComponent", null);
}
%>

<%@ include file="./res/includes/footer_component.jsp"%>
