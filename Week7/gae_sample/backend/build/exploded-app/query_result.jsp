<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="edu.dartmouth.cs.gae_sample.backend.data.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Query Result</title>
</head>
<body>
	<%
		String retStr = (String) request.getAttribute("_retStr");
		if (retStr != null) {
	%>
	<%=retStr%><br>
	<%
		}
	%>
	<center>
		<b>Query Result</b>
		<form name="input" action="/query.do" method="get">
			Name: <input type="text" name="name"> <input type="submit"
				value="OK">
		</form>
	</center>
	<b>
		---------------------------------------------------------------------<br>
		<%
			ArrayList<Contact> resultList = (ArrayList<Contact>) request
					.getAttribute("result");
			if (resultList != null) {
				for (Contact contact : resultList) {
		%> Name:<%=contact.mName%>&nbsp; Address:<%=contact.mAddress%>&nbsp;
		PhoneNumber:<%=contact.mPhoneNumber%>&nbsp; &nbsp;&nbsp; <a
		href="/delete.do?name=<%=contact.mName%>">delete</a> <br> <%
 	}
 	}
 %>
		---------------------------------------------------------------------<br>
	</b> Add new contact:
	<br>
	<form name="input" action="/add.do" method="post">
		Name: <input type="text" name="name"> Address: <input
			type="text" name="addr"> Phone: <input type="text"
			name="phone"> <input type="submit" value="Add">
	</form>
	---------------------------------------------------------------------
	<br>
	<form name="input" action="/update.do" method="post">
		Name: <input type="text" name="name">
		Address: <input type="text" name="addr">
		Phone: <input type="text" name="phone">
		<input type="submit" value="Update">
	</form>
</body>
</html>