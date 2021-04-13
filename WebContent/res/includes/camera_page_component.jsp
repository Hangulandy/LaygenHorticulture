<%@ page
	import="com.laygen.beans.Machine, com.laygen.database.Dictionary"%>

<%
Machine machine = (Machine) session.getAttribute("machine");
%>

<div>
	<h1><%=Dictionary.getInstance().get("cameraHeading")%></h1>
</div>
<hr>
<br>

<div id="image-selector" class="selector">
	<div>
		<form class="sideBySide" action="Controller" method="post">
			<input class="button" type="submit"
				value="<%=Dictionary.getInstance().get("capture")%>" /> <input
				type="hidden" name="action" value="takePicture" />
		</form>
		<form class="sideBySide" action="Controller" method="post">
			<input class="button" type="submit"
				value="<%=Dictionary.getInstance().get("refresh")%>" /> <input
				type="hidden" name="action" value="viewCameraPage" />
		</form>
	</div>


	<!-- Image list div -->
	<!-- label block -->
	<h3><%=Dictionary.getInstance().get("selectAnImageLabel")%>:
	</h3>
	<form class="sideBySide" action="Controller" method="post">
		<%
		String id = (String) session.getAttribute("selectedImageId");
		if (machine != null && machine.getImageNames() != null) {
		%>
		<!-- list box -->
		<select name="image" size="10">
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
		<br class="not-mobile"> <input class="button" type="submit"
			value="<%=Dictionary.getInstance().get("select")%>" /> <input
			type="hidden" name="action" value="viewCameraPage" />
	</form>

</div>

<div class="img-container">
	<img class="display-img" src="data:image/jpg;base64,${machine.image }" />
	<!-- delete image button will only show if there is still a selected image id session variable -->
	<%
	if (id != null) {
	%>
	<form>
		<input class="button-red" type="submit"
			value="<%=Dictionary.getInstance().get("delete")%>" /> <input
			type="hidden" name="action" value="deleteImage" /> <input
			type="hidden" name="image" value="<%=id%>" />
	</form>
	<%
	}
	%>
</div>
