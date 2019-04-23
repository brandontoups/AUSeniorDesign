<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.lang.Integer" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ include file="../fragments/header.html" %>
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
			<%--
				The history variable is given by the PerformTitleSearch.java servlet by putting it in the request scope.
				${history} searches for a variable history in all four scopes (page, application, request, session) and finds it in the request scope.
			 --%>
			<c:forEach var="deed" items="${history}" varStatus="status">
				<c:set scope="page" var="index" value="${status.count - 1}" />
				<tr>
					<c:set scope="page" var="yearSold" value="${deed.yearSold}" />
					
					<%-- 
						If the warranty deed is for the current owner, use the current year in calculating the house history length.
						Therefore, use the Date class to get the current year. 
					 --%>
					<c:if test="${deed.isLatest || empty deed.yearSold}">
						<jsp:useBean id="now" class="java.util.Date" />
						<fmt:formatDate var="year" value="${now}" pattern="yyyy" />
						<c:set scope="page" var="yearSold" value="${year}" />
					</c:if>
					
					<%--
						Calculate the house history length by using either the value from the WarrantyDeed object or the current year if the WarrantyDeed
						is the most recent one.
					 --%>
					<c:if test="${not empty deed.yearSold || pageScope.yearSold != null}">
						<c:if test="${not empty deed.yearBought}">								
							<c:set scope="page" var="houseOwnershipLength" value="${Integer.parseInt(pageScope.yearSold) - Integer.parseInt(deed.yearBought)}" />
						</c:if>
					</c:if>
					<c:set scope="page" var="historyLength" value="${pageScope.historyLength + pageScope.houseOwnershipLength}" />
					
					<td>${deed.yearBought}-<c:if test="${deed.isLatest}">Current</c:if>${deed.yearSold}</td>
					
					<%-- Removing the variables to clear the last values from the previous WarrantyDeed --%>
					<c:remove scope="page" var="houseOwnershipLength" />
					<c:remove scope="page" var="yearSold" />
					
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
	
	<c:if test="${history.size() != 0}">
		<form method="POST" action="/warranty/history" class="form-group p-3 border">
			<h1>Suggested Next Warranty Deed</h1>
			<p>Book: ${pageScope.suggestedBookNumber}</p>
			<p>Page: ${pageScope.suggestedPageNumber}</p>
			<div class="d-flex flex-row justify-content-center">
				<button class="btn btn-primary btn-lg mx-2" name="next" type="submit" 
					<c:if test="${history.size() == 0 || pageScope.suggestedBookNumber.isEmpty() || pageScope.suggestedPageNumber.isEmpty()}">disabled</c:if>>
					Next Warranty Deed
				</button>
				<button class="btn btn-success btn-lg mx-2" name="complete" type="submit" <c:if test="${pageScope.historyLength < 40}">disabled</c:if>>History Complete</button>
			</div>
			<input type="hidden" name="action" value="getNextWD" />
			<input type="hidden" name="suggestedBookNumber" value="${pageScope.suggestedBookNumber}" />
			<input type="hidden" name="suggestedPageNumber" value="${pageScope.suggestedPageNumber}" />
		</form>
	</c:if>
</div>

<%@ include file="../fragments/footer.html" %>