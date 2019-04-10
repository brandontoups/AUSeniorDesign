<%@page import="com.seniordesign.titlesearch.WarrantyDeed"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ include file="../fragments/header.html" %>
<div class="container">
	<form method="POST" action="/warranty/validate" class="my-4">
		<c:set scope="page" var="deed" value="${history.get(param.deed)}" />
		<c:set scope="page" var="index" value="${param.deed}" />
		<a href="/warranty/history" class="btn btn-primary my-3" role="button">Back</a>
		<div id="view-${index}" class="border border-dark p-3">
			<c:if test="${!errors.isEmpty()}">
					<ul class="list-group my-3">
						<c:forEach var="error" items="${errors}">
							<li class="list-group-item list-group-item-danger mb-1">${error}</li>
						</c:forEach>
						<c:remove var="errors" />
					</ul>
			</c:if>
			<div class="form-row">
				<div class="form-group col">
						<h1 class="mb-3">Extracted Information</h1>
						<div class="form-check">
							<input class="form-check-input" type="checkbox" value="isLatest" id="isLatest" name="isLatest" <c:if test="${deed.isLatest}">checked</c:if>>
						  	<label class="form-check-label" for="isLatest">
						    	Current Owner
						  	</label>
						</div>
						
						<div class="d-flex my-3">
							<div class="input-group">
								<div class="input-group-prepend">
									<span class="input-group-text">Grantor(s)</span>
								</div>
								<div id="grantorList">
									<c:forEach var="grantor" items="${deed.grantors}" varStatus="status">	
										<input type="text" name="grantor" class="form-control" placeholder="Add grantor" value="${grantor}"/>
									</c:forEach>
									<input type="text" name="grantor" class="form-control" placeholder="Add grantor" value="${grantor}"/>
								</div>
							</div>
							<div>
								<input type="button" id="addGrantor" class="btn" value="Add More"/>
							</div>					
						</div>
						
						<div class="d-flex my-3">
							<div class="input-group">
								<div class="input-group-prepend">
									<span class="input-group-text">Grantee(s)</span>
								</div>
								<div id="granteeList">
									<c:forEach var="grantee" items="${deed.grantees}" varStatus="status">	
										<input type="text" name="grantee" class="form-control" placeholder="Add grantee" value="${grantee}"/>
									</c:forEach>
									<input type="text" name="grantee" class="form-control" placeholder="Add grantee" value="${grantee}"/>
								</div>
							</div>
							<div>
								<input type="button" id="addGrantee" class="btn" value="Add More"/>
							</div>
						</div>
						
						<div class="input-group my-3">
							<div class="input-group-prepend">
								<span class="input-group-text">Book #</span>
							</div>
							<input type="number" name="bookNumber" class="form-control" value="${deed.bookNumber}" placeholder="Add book number" />
						</div>
						
						<div class="input-group my-3">
							<div class="input-group-prepend">
								<span class="input-group-text">Page #</span>
							</div>
							<input type="number" name="pageNumber" class="form-control" value="${deed.pageNumber}" placeholder="Add page number" />
						</div>
						
						<div class="input-group">
							<div class="input-group-prepend">
								<span class="input-group-text">Ownership History</span>
							</div>
							<input type="number" name="yearBought" class="form-control" value="${deed.yearBought}" />
							<div class="input-group-append">
								<span class="input-group-text">to</span>
							</div>
							<c:set scope="page" var="yearSold" value="${deed.yearSold}" />
							<c:if test="${deed.isLatest}">
								<c:set scope="page" var="yearSold" value="" />
							</c:if>
							<input type="number" name="yearSold" class="form-control" value="${pageScope.yearSold}" />
						</div>
						
						<div class="input-group my-3">
							<div class="input-group-prepend">
								<span class="input-group-text">Transaction Date</span>
							</div>
							<input type="date" name="transaction" class="form-control" value="${deed.transactionDate}"/>
						</div>
						
						<div class="input-group my-3">
							<div class="input-group-prepend">
								<span class="input-group-text">Parent Book #</span>
							</div>
							<input type="number" name="parentBookNumber" class="form-control" value="${deed.parentBookNumber}" placeholder="Add parent book number" />
						</div>
						
						<div class="input-group my-3">
							<div class="input-group-prepend">
								<span class="input-group-text">Parent Book Page #</span>
							</div>
							<input type="number" name="parentBookPageNumber" class="form-control" value="${deed.parentPageNumber}" placeholder="Add parent book page number" />
						</div>
						
						<ul class="list-group">
							<li class="input-group-text">Legal Description</li>
						</ul>
							<textarea rows="50" cols="100" class="form-control" name="legalDescription">${deed.text}</textarea>
						<input type="hidden" name="index" value="${index}" />
				</div>
				
				<div class="form-group col ml-2">
					<object class="form-control" name="pdf" data="/ShowPDF?deed=${index}" type="application/pdf" width="800px" height="900px"></object>
				</div>
			</div>
			
			<div class="w-100">
				<input type="hidden" name="action" value="updateWD" />
				<button class="btn btn-primary btn-lg btn-block w-50 mx-auto my-4" type="submit">Save</button>
			</div>
		</div>
	</form>
</div>

<script>
	var addGrantorButton = document.querySelector("#addGrantor");
	var grantors = document.querySelector("#grantorList");
	addGrantorButton.addEventListener("click", function() {
		var newGrantorInput = document.createElement("INPUT");
		newGrantorInput.classList.add("form-control");
		newGrantorInput.type = "text";
		newGrantorInput.name = "grantor";
		grantors.appendChild(newGrantorInput);
	});
		
	var addGranteeButton = document.querySelector("#addGrantee");
	var grantees = document.querySelector("#granteeList");
	addGranteeButton.addEventListener("click", function() {
		var newGranteeInput = document.createElement("INPUT");
		newGranteeInput.classList.add("form-control");
		newGranteeInput.type = "text";
		newGranteeInput.name = "grantee";
		grantees.appendChild(newGranteeInput);
	});
</script>
<%@ include file="../fragments/footer.html" %>