<%@page import="com.seniordesign.titlesearch.WarrantyDeed"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../fragments/header.html" %>
<div class="container">
	<h1>Select correct warranty deed.</h1>
	<div class="row">
		<c:forEach var="deed" items="${multipleResults}" varStatus="status">
			<div class="form-group col-lg-6">
				<form method="POST" action="/warranty/select" class="form-group p-3">
				<c:set scope="page" var="index" value="${status.count - 1}" />
					<object class="form-control" name="pdf" data="/ShowPDF?deed=${index}&view=select" type="application/pdf" width="500px" height="800px"></object>
					<div class="w-100">
						<input type="hidden" name="action" value="chooseWD" />
						<input type="hidden" name="deedIndex" value="${index}" />
						<button class="btn btn-primary btn-lg btn-block w-50 mx-auto my-4" type="submit">Correct Deed</button>
					</div>
				</form>
			</div>
		</c:forEach>
	</div>
</div>
<%@ include file="../fragments/footer.html" %>