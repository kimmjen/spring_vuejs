<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world! 이미지5개+png
</h1>

<P>  The time on the server is ${serverTime}. </P>
<img alt="" src="./resources/i01.jpg">
<img alt="" src="./resources/i02.jpg">
<img alt="" src="./resources/i03.jpg">
<img alt="" src="./resources/다운로드.jpg">
<img alt="" src="./resources/아이린1.png">

<p>
	<a href="/board/listPageSearch?num=1">글 목록(페이징 + 검색)</a>
	<a href="/board/listPage?num=1">글 목록(페이징)</a>
	<a href="./board/list">게시물 목록</a>
	<a href="./board/write">게시물 작성</a>
</p>
</body>
</html>
