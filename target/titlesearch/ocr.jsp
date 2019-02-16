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
			<form method="POST" enctype="multipart/form-data" action="UploadFile">
				<div class="inputBar">
					<label for="fileInput">Upload a pdf
						<input class="file" name="fileInput" type="file" accept=".pdf, .tiff" />
					</label>
						<button type="submit">Submit</button>
				</div>
			</form>
		</div>
		<% 
			String heading = "";
			String extraInfo = "";
			boolean isDocument = request.getAttribute("document") != null;
			if(isDocument) {
				heading = "Success"; 
				extraInfo = "Document was successfully added to the collection."; 
			} else { 
				heading = "Failure"; 
				extraInfo = "An error occured during the operation. Document could not be added."; 
			} 
		%>
		<% if(isDocument) { %>
			<div class="notification">
				<h2 class="heading"><% out.println(heading); %></h2>
				<p class="extraInfo"><% out.println(extraInfo); %></p>
			</div>
		<% } %>
	</div>
</body>
</html>