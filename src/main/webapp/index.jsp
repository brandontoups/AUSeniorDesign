<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="fragments/header.html" %>
<div class="container">
	<form class="form-group mt-5 mx-auto" method="GET" action="/search">
		<div>
			<h1 class="display-4 text-center mx-auto">TitleSearch</h1>
		</div>
		<div class="input-group input-group-lg w-50 mx-auto my-3">
			<div class="input-group-prepend">
			    <span class="input-group-text">Book #</span>
			</div>
			<input class="form-control" placeholder="Enter Warranty Deed #" type="number" name="book" required />
		</div>
		<div class="input-group input-group-lg w-50 mx-auto my-3">
			<div class="input-group-prepend">
			    <span class="input-group-text">Book Page #</span>
			</div>
			<input class="form-control" placeholder="Enter Page #" type="number" name="page" required />
		</div>
		<div class="w-100 my-2">				
			<input type="hidden" name="action" value="searchWD" />
			<button class="btn btn-primary btn-block btn-lg w-50 mx-auto" type="submit">Start History</button>
		</div>
	</form>
</div>
<%@ include file="fragments/footer.html" %>