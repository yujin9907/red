<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form action="/boards/${boards.id}/update" method="post">

		<div class="mb-3 mt-3">
			<input type="text" value="${boards.id}/update" method="post" name="id">
			<input type="text" class="form-control" placeholder="Enter title"
				name="title" value="${boards.title}" required="required" maxlength="50"> 
				150byte=한글50자 정도라서 10개가 30바이트
				required, maxlength 
		</div>
		<div class="mb-3">
			<textarea class="form-control" rows="8" name="content" required="required">"${boards.content}"</textarea>
		</div>
		<button type="submit" class="btn btn-primary">수정완료</button>
	</form>
</div>

<%@ include file="../layout/footer.jsp"%>

