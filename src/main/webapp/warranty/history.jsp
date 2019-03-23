<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.lang.Integer" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>House History</title>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	</head>
	<body>
		<div class="container">
			<c:set scope="page" var="historyLength" value="${0}" />
			<h1>House History</h1>
			<table class="table table-bordered">
				<thead class="thead">
					<tr>
						<th scope="col">Date Range</th>
						<th scope="col">Grantor</th>
						<th scope="col">Grantee</th>
						<th scope="col">Book #</th>
						<th scope="col">Page #</th>
						<th scope="col">Parent Book #</th>
						<th scope="col">Parent Page #</th>
						<th scope="col">Validate</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="deed" items="${history}" varStatus="status">
						<c:set scope="page" var="index" value="${status.count - 1}" />
						<tr>
							<c:set scope="page" var="dateTo" value="${deed.dateTo}" />
							<c:if test="${deed.isLatest}">
								<jsp:useBean id="now" class="java.util.Date" />
								<fmt:formatDate var="year" value="${now}" pattern="yyyy" />
								<c:set scope="page" var="dateTo" value="${year}" />
							</c:if>
							<c:if test="${!deed.dateTo.isEmpty()}">
								<c:if test="${!deed.dateFrom.isEmpty()}">								
									<c:set scope="page" var="houseOwnershipLength" value="${Integer.parseInt(pageScope.dateTo) - Integer.parseInt(deed.dateFrom)}" />
								</c:if>
							</c:if>
							<c:set scope="page" var="historyLength" value="${pageScope.historyLength + pageScope.houseOwnershipLength}" />
							<td>${deed.dateFrom}-${pageScope.dateTo}</td>
							<td>
								<c:forEach var="grantor" items="${deed.grantors}" varStatus="grantorStatus">
									${grantor}<c:if test="${!grantorStatus.last}"><br></c:if>
								</c:forEach>
							</td>
							<td>
								<c:forEach var="grantee" items="${deed.grantees}" varStatus="granteeStatus">
									${grantee}<c:if test="${!granteeStatus.last}"><br></c:if>
								</c:forEach>
							</td>
							<td>${deed.bookNumber}</td>
							<td>${deed.pageNumber}</td>
							<td>${deed.parentBookNumber}</td>
							<td>${deed.parentPageNumber}</td>
							<td><a href="/warranty/validate?deed=${index}">Link</a></td>
						</tr>
						<c:if test="${status.last}">
							<c:set scope="page" var="suggestedBookNumber" value="${deed.parentBookNumber}" />
							<c:set scope="page" var="suggestedPageNumber" value="${deed.parentPageNumber}" />
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			<p class="alert alert-info">The report covers ${historyLength} years of house history.</p>
			<form method="POST" action="/warranty/history" class="form-group p-3 border">
				<h1>Suggested Next Warranty Deed</h1>
				<p>Book: ${pageScope.suggestedBookNumber}</p>
				<p>Page: ${pageScope.suggestedPageNumber}</p>
				<div class="d-flex flex-row justify-content-center">
					<button class="btn btn-primary btn-lg mx-2" name="next" type="submit">Next Warranty Deed</button>
					<button class="btn btn-success btn-lg mx-2" name="complete" type="submit">History Complete</button>
				</div>
				<input type="hidden" name="action" value="getNextWD" />
				<input type="hidden" name="book" value="${pageScope.suggestedBookNumber}" />
				<input type="hidden" name="page" value="${pageScope.suggestedPageNumber}" />
			</form>
		</div>
	</body>
</html>