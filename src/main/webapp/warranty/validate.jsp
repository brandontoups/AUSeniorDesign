<%@page import="java.io.PrintWriter"%>
<%@page import="java.nio.ByteBuffer"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.seniordesign.titlesearch.WarrantyDeed"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Insert title here</title>
		<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	</head>
	<body>
		<div class="container">
				<form method="POST" action="/validated" class="my-3">
					<div class="form-row">
						<div class="form-group col">
							<label for="pdf">PDF</label>
							<object class="form-control" name="pdf" data="ShowPDF" type="application/pdf" width="800px"
								height="900px"></object>
						</div>
						<div class="form-group col">
								<label for="text">OCR Text</label>							
								<textarea class="form-control" name="text" rows="50" cols="100">${WD.text}</textarea>
						</div>
					</div>
					<input type="hidden" name="action" value="updateText" />
					<button class="btn btn-primary btn-lg btn-block" type="submit">Save</button>
				</form>
			</div>
	</body>
</html>