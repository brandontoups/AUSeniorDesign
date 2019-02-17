<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta content="width=device-width, initial-scale=1" name="viewport" />
		<title>TitleSearch OCR</title>
		<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="index.css" />
	</head>
<body>
	<div class="container">
		<% 
			String heading = (String) request.getAttribute("status");
			if(!heading.isEmpty()) {
		%>
			<div class="notification">
				<h2 class="heading"><% out.println(heading); %></h2>
			</div>
		<% } %>
	</div>
</body>
</html>