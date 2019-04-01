<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<meta content="width=device-width, initial-scale=1" name="viewport" />
		<title>TitleSearch OCR</title>
		<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="./index.css" />
	</head>
<body>
	<div class="container">
		<div class="search">
			<form method="POST" action="QueryResult">
				<div class="inputBar">
					<label for="query">Search using term
						<input placeholder="Enter a search term" type="text" id="query" name="query" required />
					</label>
				</div>
			</form>
			<form method="POST" action="FindFile">
				<div class="inputBar">
					<label for="query">Search using filename
						<input placeholder="Enter a filename" type="text" id="query" name="query" required />
						<input type="hidden" name="action" value="findDocument" />
					</label>
				</div>
			</form>
			<form method="POST" action="FindFile">
				<div class="inputBar">
					<label for="query">Search using filename
						<input placeholder="Enter a filename" type="text" id="query" name="query" required />
						<input type="hidden" name="action" value="getPassages" />
					</label>
				</div>
			</form>
		</div>
		<div class="container">
				<table>
					<thead>
						<tr>
							<th colspan="2">Results</th>
						</tr>
					</thead>
					<tbody>
					<tr>
						<td class="label">Filename</td>
						<td>${fileName}</td>
					</tr>
					<tr>
						<td class="label">Text</td>
						<td>${text}</td>
					</tr>
					<!-- <tr>
						<td class="label">Land Description</td>
						<td></td>
					</tr> -->
					</tbody>
				</table>
		</div>
	</div>
</body>
</html>