<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="container">
		<div class="search">
			<form method="POST" enctype="multipart/form-data" action="/PerformTitleSearch">
				<div class="inputBar">
					<label for="fileInput">Upload a pdf
						<input class="file" name="fileInput" type="file" accept=".pdf, .tiff" />
					</label>
						<input type="hidden" name="action" value="showWD" />
						<button name="uploadFileButton" type="submit">Submit</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>