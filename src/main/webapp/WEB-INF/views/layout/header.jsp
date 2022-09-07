<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Blog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 부트스트랩을 위한 링크 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

	세션에 담은 거 찾는 법. 세션스코프.키값 그냥 프린시팔만 적어도 되는데, 충돌날 수도 있어서(세션이랑 리퀘스트사이에) 꼭
	적어주기 사실 리퀘스트는 그냥 주고, 세션만 구분해주면 됨.
	<h1>${sessionScope.principal.username}</h1>

	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="/boards">Blog</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#collapsibleNavbar">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="collapsibleNavbar">


				<ul class="navbar-nav">

					<c:choose>
						<c:when test="${empty principal}">
							<li class="nav-item"><a class="nav-link" href="/loginForm">로그인</a>
							</li>
							<li class="nav-item"><a class="nav-link" href="/joinForm">회원가입</a>
							</li>
						</c:when>
						<c:otherwise>
							<li class="nav-item"><a class="nav-link"
								href="/boards/writeForm">글쓰기</a></li>
							<li class="nav-item"><a class="nav-link"
								href="/boards/writeForm" href="/logout">로그아웃</a></li>
						</c:otherwise>
					</c:choose>

				</ul>
			</div>
		</div>
	</nav>