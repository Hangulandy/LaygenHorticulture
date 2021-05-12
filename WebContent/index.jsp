
<%@ include file="./res/includes/header_component.jsp"%>

<!-- Must start with the login component if there is no logged in user -->
<div class="view-area">
	<%
	String viewComponent = (String) session.getAttribute("viewComponent");
	if ((user == null || !user.isLoggedIn())) {
	%>
	<div class="under-header-not-logged-in"></div>
	<%
	if (viewComponent != null && viewComponent.equalsIgnoreCase("join")) { // include join
	%>
	<%@ include file="./res/includes/join_component.jsp"%>
	<%} else { // include login %>
	<%@ include file="./res/includes/login_component.jsp"%>
	<%}%>
	<%
	} else {// place side-bar and then list all the logged in options
	%>
	<div class="under-header"></div>

	<div class="side-bar block">
		<h3>${dict.get('quickViewHeading', lang)}:</h3>

		<%
		if (user.getAuthorizations() != null) {
			for (Authorization auth : user.getAuthorizations()) {
		%>
		<a class="shortcut"
			href="Controller?selectedMachineId=<%=auth.getMachineSerialNumber()%>&action=selectMachine"><%=auth.getMachineNickname()%></a>
		<br>
		<%
		}
		}
		%>
	</div>
	<!-- end of side-bar div -->


	<!-- Display component based on the return from the servlet -->
	<div class="content block">

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
		<%}%>

		<%if (viewComponent.equalsIgnoreCase("cameraPage")) { %>
		<%@include file="./res/includes/camera_page_component.jsp"%>
		<%}%>
		
		<%if (viewComponent.equalsIgnoreCase("registerMachine")) { %>
		<%@include file="./res/includes/register_machine.jsp"%>
		<%}%>		
		
		<%
		} else {
		%>
		<%@ include file="./res/includes/machine_list_component.jsp"%>
		<%
		}
		%>
		<!-- end of content-view div -->
		<%}%>
	</div>
</div>
<!-- end of main div -->

<%@ include file="./res/includes/footer_component.jsp"%>