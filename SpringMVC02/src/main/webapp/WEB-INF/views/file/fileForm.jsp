<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="/top"/>

<div class="container mt-3" style="height:600px;overflow: auto;">
	<h1 class="text-center">File Upload1</h1>
	
	<form name="f" action="fileUp" method="post" enctype="multipart/form-data">
	
		올린이: <input type="text" name="name"><br><br>
		첨부파일:<input type="file" name="mfilename"><br><br>
		<button class="btn btn-success">업로드</button>
	</form>
	<hr>
	<h1 class="text-center">File Upload2- 여러 개 첨부</h1>
	<form name="f2" action="fileUp2" method="post" enctype="multipart/form-data">
	
		올린이: <input type="text" name="name"><br><br>
		첨부파일1:<input type="file" name="mfilename"><br><br>
		첨부파일2:<input type="file" name="mfilename"><br><br>
		<button class="btn btn-success">업로드</button>
	</form>
	
</div>
<c:import url="/foot" />