<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:import url="/top"/>

<div class="container mt-3" style="height:600px;overflow: auto;">
	<h1 class="text-center">Spring Board 내용 보기</h1>
	
	<c:if test="${board eq null}">
		<div class="alert alert-danger my-5 text-center">
			<h4>존재 하지 않는 글입니다.</h4>
		</div>
	</c:if>
	
	<c:if test="${board ne null}">
		<table class="table mt-4">
            <tr>
               <td width="20%">글번호</td>
               <td width="30%">
               <c:out value="${board.num}"/>
               </td>
               <td width="20%">작성일</td>
               <td width="30%">
               <fmt:formatDate value="${board.wdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
               </td>
            </tr>
            <tr>
               <td width="20%">글쓴이</td>
               <td width="30%">
               <c:out value="${board.name}"/>
               </td>
               <td width="20%">조회수</td>
               <td width="30%">
               <c:out value="${board.readnum}"/>
               </td>
            </tr>

            <tr>
               <td width="20%">제목</td>
               <td colspan="3" align="left">
               <c:out value="${board.subject}"/>
               </td>
            </tr>
            <tr height="60">
               <td width="20%">글내용</td>
               <td colspan="3" align="left">
               ${board.content}
               	
               </td>
            </tr>
            <tr>
               <td width="20%">첨부파일</td>
               <td colspan="3">&nbsp; 
                     <!--  첨부파일이 있다면 -->
                     <c:if test="${board.filename ne null}">
                     
                     <%-- <a href="${pageContext.request.contextPath}/resources/board_upload/${board.filename}" download> --%>
                      
                     <a href="#" onclick="down()">
                     ${board.originFilename}
                     </a> 
                     [ <c:out value="${board.filesize}"/> bytes]  
            		</c:if>
            		
            		<c:set var="fname" value="${fn:toLowerCase(board.filename)}"/>
            	<!-- 파일명의 확장자를 검사하기 위해 모두 소문자로 바꾸자 -->
            	<c:if test="${fn:endsWith(fname,'.jpg') or fn:endsWith(fname,'.png') or fn:endsWith(fname,'.gif')}">
                        <img width="80px" class="img img-thumbnail"
                           src="${pageContext.request.contextPath}/resources/board_upload/${board.filename}">
                </c:if>
                       
               </td>
            </tr>
            <tr>
               <td colspan="4" align=center><a href="../write">글쓰기</a>| <a
                  href="../list">목록</a>| <a href="#" onclick="go(1)">편집</a>| <a
                  href="#" onclick="go(2)">삭제</a>| <a href="javascript:goRe()">답변</a></td>
            </tr>
         </table>
	</c:if>
	<!-- 파일 다운로드를 위한 form------------------------------------ -->
	<form name="fileF" id="fileF" method="POST" action="../../fileDown">
		<input type="hidden" name="fname" value="<c:out value="${board.filename}"/>">
		<input type="hidden" name="origin_fname" value="<c:out value="${board.originFilename}"/>">
	</form>	
	<!--  ------------------------------------------------------- -->
</div>

<script>
	//파일 다운로드 처리
	function down(){
		fileF.submit();		
	}
</script>

<c:import url="/foot" />



