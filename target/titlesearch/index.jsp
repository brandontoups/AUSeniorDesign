<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta content="width=device-width, initial-scale=1" name="viewport" />
		<title>TitleSearch</title>
		<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="index.css" />
	</head>
	<body>
		<div class="container">
			<div class="header">
				<h1><a href="/">TitleSearch</a></h1>
			</div>
			<div class="search">
				<form method="GET" action="GetWarrantyDeed">
					<div class="inputBar">
						<label for="book">Warranty
							<input placeholder="Enter Warranty Deed #" type="number" id="warranty" name="book" required />
						</label>
					</div>
					<div class="inputBar">
						<label for="page">Page
							<input placeholder="Enter Page #" type="number" id="page" name="page" required />
						</label>
					</div>
					<button type="submit">Search</button>
				</form>
			</div>
		</div>
	</body>
</html>