<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 조회</title>
</head>
<body>
	<div id="nav">
		<%@ include file="../include/nav.jsp"%>
	</div>
	<form method="post">
		<label>제목</label> <input tpye="text" name="title"
			value="${view.title }" /><br /> <label>작성자</label> <input
			tpye="text" name="writer" value="${view.writer }" /><br /> <label>내용</label>
		<textarea cols="50" rows="5" name="content">${view.content }</textarea>
		<br />

		<div>
			<a href="/board/modify?bno=${view.bno }">게시물 수정</a> <a
				href="/board/delete?bno=${view.bno}">게시물 삭제</a>
			<!-- <a href="/board/list">게시물 목록</a> -->
		</div>

	</form>

	<%-- <label>제목</label>
${view.title}<br />

<label>작성자</label>
${view.writer}<br />

<label>내용</label><br />
${view.content}<br />
 
<div>
	<a href="/board/modify?bno=${view.bno }">게시물 수정</a>
</div>
--%>
	<!-- 댓글시작 -->
	<hr />
	<ul>
		<!-- <li>
			<div>
				<p>첫번째 댓글 작성자</p>
				<p>첫번째 댓글</p>
			</div>
		</li>
		<li>
			<div>
				<p>두번째 댓글 작성자</p>
				<p>두번째 댓글</p>
			</div>
		</li>
		<li>
			<div>
				<p>세번째 댓글 작성자</p>
				<p>세번째 댓글</p>
			</div>
		</li> -->
		<c:forEach items="${reply }" var="reply">
			<li>
				<div>
					<p>${reply.writer }
						/
						<fmt:formatDate value="${reply.regDate }" pattern="yyyy-MM-dd" />
					</p>
					<p>${reply.content }</p>
				</div>
			</li>
		</c:forEach>
	</ul>
	<div>
	<form method="post" action="/reply/writer">
		<p>
			<label>댓글 작성자</label> <input type="text" name="writer">
		</p>
		<p>
			<textarea rows="5" cols="50" name="content"></textarea>
		</p>
		<p>
			<input tpye="hidden" name="bno" value="${view.bno }">
			<button type="submit">댓글 작성</button>
		</p>
	</form>
		<!-- <p>
			<label>댓글 작성자</label> <input type="text">
		</p>
		<p>
			<textarea rows="5" cols="5-"></textarea>
		</p>
		<p>
			<buttontpye"button">댓글 작성</button>
		</p> -->
	</div>
	<!-- 댓글 끝 -->
</body>
</html>