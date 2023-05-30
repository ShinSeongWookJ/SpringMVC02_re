<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="/top"/>

<div class="container mt-3" style="height:600px;overflow: auto;">
	<h1 class="text-center">파일 업로드 결과</h1>
	
	<h2>올 린 이 : ${name} </h2>
	<h2>첨부 파일명: ${fname}</h2>
	<h2>첨부파일 크기: ${fsize } bytes</h2>
</div>
<c:import url="/foot" />