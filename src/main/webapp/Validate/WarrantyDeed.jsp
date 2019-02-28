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
		<link rel="stylesheet" type="text/css" href="index.css" />
	</head>
	<body>
		<div class="container containerFlex">
			<form method="POST" action="PerformTitleSearch">
				<div class="pdfView">
					<h1>PDF</h1>
					<object name="pdf" data="ShowPDF" type="application/pdf" width="800px"
						height="900px"></object>
				</div>
				<div class="textView">
						<h1>OCR Text</h1>
						<%
							ServletContext context = request.getServletContext();
							WarrantyDeed deed = (WarrantyDeed) context.getAttribute("WD");
							String pdfContent = deed.getWarrantyDeedText();
						%>
						<%
							if (pdfContent != null) {
						%>
							<textarea name="text" rows="50" cols="100"><%= pdfContent %></textarea>
						<% } %>
				</div>
					<input type="hidden" name="action" value="updateText" />
					<button type="submit">Save</button>
			</form>
		</div>
	</body>
</html>