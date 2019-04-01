<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta content="width=device-width, initial-scale=1" name="viewport" />
		<title>TitleSearch OCR</title>
		<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="/index.css" />
	</head>
<body>
	<div class="container">
		<div class="search">
			<form method="POST" enctype="multipart/form-data" action="UploadToDiscovery">
				<div class="inputBar">
					<label for="fileInput">Upload a pdf
						<input class="file" name="fileInput" type="file" accept=".pdf, .tiff" />
					</label>
						<button type="submit">Submit</button>
				</div>
			</form>
		</div>	
		<div class="notification">
			<h2 class="heading">${applicationScope.statusDesc}</h2>
		</div>			
	</div>
</body>
</html>