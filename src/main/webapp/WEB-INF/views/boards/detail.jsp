<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<br /> <br /> 원래는 이런 br 아니고 css로 디자인함 부트스트랩 버튼스가서 적용할 수 있는 디자인 확인
	수정하러가기=수정폼이 필요함 삭제=삭제수행, 원래는 삭제를 폼 태그로 하지 않지만 js 아직 몰라서 일단
	<div class="d-flex">

		<c:choose>
			<c:when test="${empty principal}">

			</c:when>
			<c:otherwise>
				<!-- 겟 요청이니까 폼 빼고 a 태그로 박음 -->
				<a href="/boards/${boards.id}/updateForm" class="btn btn-warning">수정하러가기</a>
				<form action="/boards/${boards.id}/delete" method="post">
					<button class="btn btn-danger">삭제</button>
				</form>
			</c:otherwise>
		</c:choose>


	</div>
	<br />
	<div>
		<h3>${boards.title}</h3>
	</div>
	<hr />

	<div>${boards.content}</div>


</div>

<%@ include file="../layout/footer.jsp"%>

