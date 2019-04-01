<%@page import="com.seniordesign.titlesearch.BoxAPI"%>
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
		<div class="search">
			<form method="POST" action="DeleteDocument">
				<div class="inputBar">
					<label for="query">Delete a document
						<input placeholder="Enter document id" type="text" id="query" name="query" required />
					</label>
				</div>
			</form>
		</div>
	</div>
</body>
</html>