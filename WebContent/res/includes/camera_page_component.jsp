<%@ page import="com.laygen.beans.Machine"%>

<%
Machine machine = (Machine) session.getAttribute("machine");
%>

<div>
	<h1>${dict.get('cameraHeading', lang)}</h1>
</div>
<hr>
<div class="small-space"></div>

<div id="image-selector" class="selector">
	<div>
		<form class="sideBySide" action="Controller" method="post">
			<input class="button" type="submit"
				value="${dict.get('capture', lang)}" /> <input type="hidden"
				name="action" value="takePicture" />
		</form>
		<form class="sideBySide" action="Controller" method="post">
			<input class="button" type="submit"
				value="${dict.get('refresh', lang)}" /> <input type="hidden"
				name="action" value="viewCameraPage" />
		</form>
	</div>


	<!-- Image list div -->
	<!-- label block -->
	<h3>${dict.get('selectAnImageLabel', lang)}:</h3>
	<form class="sideBySide" action="Controller" method="post">
		<%
		String id = (String) session.getAttribute("selectedImageId");
		if (machine != null && machine.getImageNames() != null && machine.getImageNames().size() > 0) {
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
			value="${dict.get('select', lang)}" /> <input type="hidden"
			name="action" value="viewCameraPage" />
	</form>

</div>

<div class="img-container">
	<%String imageString = machine.getImage();
if (imageString != null){ %>

	<img class="display-img" src="data:image/jpg;base64,${machine.image }" />
	<!-- delete image button will only show if there is still a selected image id session variable -->
	<%
	if (id != null) {
	%>
	<div class="small-space"></div>
	<form class="sideBySide" action="Controller" method="post">
		<input class="button-red" type="submit"
			value="${dict.get('delete', lang)}" /> <input type="hidden"
			name="action" value="deleteImage" /> <input type="hidden"
			name="imageId" value="<%=id%>" />
	</form>
	<a class="download" href="data:image/jpg;base64,${machine.image}" download="<%=id%>">
	<button id="editButton" class="button sideBySide" type="button">${dict.get('downloadButtonLabel', lang)}</button>
	</a>
	<%
	}
	%>

	<%} else { %>

	<h3>${dict.get('noImageMessage', lang) }</h3>

	<%} %>

</div>
