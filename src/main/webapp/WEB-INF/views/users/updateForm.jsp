<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form action="/update/${principal.id}" method="post">
		<div class="mb-3 mt-3">
			<input type="text" value="${principal.id}/update" method="post" name="id">
			<input type="password" class="form-control" placeholder="Enter title"
				name="password" required="required" maxlength="50"> 
			<input type="text" class="form-control" placeholder="Enter title"
				name="email" value="${principal.email}" required="required" maxlength="50"> 
		</div>
		
		<button type="submit" class="btn btn-primary">수정완료</button>
	</form>
</div>

<%@ include file="../layout/footer.jsp"%>

