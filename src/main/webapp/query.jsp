<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.ibm.watson.developer_cloud.util.ResponseUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.watson.developer_cloud.discovery.v1.model.QueryPassages"%>
<%@page import="com.ibm.watson.developer_cloud.discovery.v1.model.QueryResult"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse"%>
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
					</label>
				</div>
			</form>
		</div>
		<div class="container">
			<% if(request.getAttribute("queryResponse") != null) { %>
				<% QueryResponse queryResponse = (QueryResponse) request.getAttribute("queryResponse"); %>
				<% Long matchingResults = queryResponse.getMatchingResults(); %>
				<% List<QueryResult> results = queryResponse.getResults(); %>
				<% List<QueryPassages> passages = queryResponse.getPassages(); %>
				<table>
					<thead>
						<tr>
							<th colspan="2">Results</th>
						</tr>
					</thead>
					<tbody>
				<% for(int i = 0; i < matchingResults; i++) { %>
					<% HashMap<String, Object> metadata = results.get(i); %>
					<tr>
						<td class="label">Filename</td>
						<% JsonObject extractedMetadata = ResponseUtils.getJsonObject(metadata.get("extracted_metadata").toString()); %>
						<td><% if(metadata.containsKey("extracted_metadata")) out.println(extractedMetadata.get("filename").getAsString()); %></td>
					</tr>
					<tr>
						<td class="label">Text</td>
						<td><% if(metadata.containsKey("text")) out.println(metadata.get("text")); %></td>
					</tr>
					<tr>
						<td class="label">Land Description</td>
						<td><% if(metadata.containsKey("landdescription")) out.println(metadata.get("landdescription")); %></td>
					</tr>
					<tr>
						<td class="label">Keys</td>
						<td><% out.println(metadata.keySet()); %></td>
					</tr>
				<% } %>
					</tbody>
				</table>
			<% } %>
		</div>
	</div>
</body>
</html>