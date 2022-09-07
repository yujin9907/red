<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<table class="table table-striped">
		<thead>
			<tr>
				<th>번호</th>
				<th>게시글제목</th>
				<th>작성자이름</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="boards" items="${boardsList}">
				<tr>
					<td>${boards.id}</td>
					<td><a href="/boards/${boards.id}">${boards.title}</a></td>
					<td>${boards.username}</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>

<!-- 	<div style="backgorund-color: 'grey';">
		<h3>${paging.totalCount}</h3>
		<h3>${paging.totalPage}</h3>
		<h3>${paging.currentPage}</h3>
		<h3>${paging.last}</h3>
		<h3>${paging.first}</h3>
	</div>

	<!-- 파람 그냥 위의 변수를 통해서 수정 -->
-->

<!-- 디스플레이 정렬 https://www.w3schools.com/bootstrap5/bootstrap_flex.php 참고해서
	그 캘린더에 vertical direction 부분 참고해서 수정해보기
	박스를 잘 만들어야 배치를 할 수 있다
 -->

	<h2>${param.page}</h2>
	<div class="d-flex justify-content-center">
	<ul class="pagination">
		<li class='page-item ${paging.first?"disabled":""}'><a class="page-link"
			href="/?page=${param.page -1}">previous</a></li>
		
		<c:forEach var="i" begin="${paging.startPageNum}" end="${paging.lastPageNum}">    
			<li class='page-item ${paging.currentPage == num-1 ? "active":""}'><a class="page-link" href="/?page=${i-1}">${i}</a></li>
		</c:forEach>
		
		<li class='page-item ${paging.last?"disabled":""}'><a class="page-link"
			href="/?page=${param.page + 1}">Next</a></li>
	</ul>
	</div>


</div>

<%@ include file="../layout/footer.jsp"%>

