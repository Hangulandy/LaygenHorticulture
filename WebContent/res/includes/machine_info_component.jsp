<h1>Machine Information</h1>	

<%@ page import="com.laygen.beans.Machine" %>

	<p>Serial Number : ${machine.serialNumber}</p>
	<p>Model Name : ${machine.info['model_name'] }</p>
	<p>Primary Use : ${machine.info['primary_use'] }</p>
	<p>Owner Email : ${machine.info['owner_email'] }</p>
	<p>IP Address : ${machine.info['ip'] }</p>
	<p>Port : ${machine.info['port'] }</p>