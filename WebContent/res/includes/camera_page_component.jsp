<%@ page import="com.laygen.beans.Machine"%>


<h1>Camera</h1>
<!-- div to hold subcomponents -->
<form class="sideBySide" action="Controller" method="post">
	<input class="button" type="submit" value="Take a Picture" /> <input
		type="hidden" name="action" value="takePicture" />
</form>
<form class="sideBySide" action="Controller" method="post">
	<input class="button" type="submit" value="Refresh" /> <input
		type="hidden" name="action" value="viewCameraPage" />
</form>
<ul>

	<%
	Machine machine = (Machine) session.getAttribute("machine");
		if (machine != null && machine.getImageNames() != null) {
			for (String key : machine.getImageNames().keySet()) {
	%>
	<li><%=key%> : <%=machine.getImageNames().get(key)%></li>
	<%
	}
		}
	%>

</ul>

<div>
	<!-- Image list div -->
	<div>
		<!-- label block -->
		<label>Images for this machine:</label>
		<form class="sideBySide" action="Controller" method="post">
			<%
			String id = (String) session.getAttribute("selectedImageId");
				if (machine != null && machine.getImageNames() != null) {
			%>
			<p>Selected Image ID is <%=id%></p>
			<!-- list box -->
			<select name="image">
				<%
				for (String key : machine.getImageNames().keySet()) {
				%>
				<option value="<%=key%>"
					<%if (id != null && id.equalsIgnoreCase(key)) {%>
					selected="selected" <%}%>><%=machine.getImageNames().get(key)%></option>
				<%
				}
				%>
			</select>
			<%
			} else {
			%>
			<p>No images to display</p>
			<%
			}
			%>
			<input class="button" type="submit" value="Select" /> <input
				type="hidden" name="action" value="viewCameraPage" />
		</form>

		<!-- div for buttons -->
		<div>
			<!-- View (select) button -->
			<!-- Refresh list button -->
		</div>
	</div>
	<!-- Image view div -->
	<div>
		<!-- label block -->
		<!-- image viewer? -->
		<img src="data:image/jpg;base64,${machine.image }" height="480"
			width="640" />
		<!-- div for buttons -->
		<div>
			<!-- delete image button will only show if there is still a selected image id session variable -->
			<% if ( id != null){%>
			<form>
				<input class="button" type="submit" value="Delete" /> <input
					type="hidden" name="action" value="deleteImage" />
					<input type="hidden" name="image" value="<%=id%>"/>
			</form>
			<%} %>
		</div>
	</div>
</div>