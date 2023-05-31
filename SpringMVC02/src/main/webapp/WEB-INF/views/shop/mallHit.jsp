<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container" style="text-align:center">

<div class = "row">
   <div class = "col-md-12">
      <h1 class = "badge badge-warning">${param.pspec}</h1>
   </div>
</div>

<div class = "row">
	<c:if test="${pList eq null or empty pList}">
		<div class="col-md-3">
			<h4>상품 준비 중</h4>
		</div>
	</c:if>
	<c:if test="${pList ne null and not empty pList}">
		<c:forEach var="pd" items="${pList}">
	      <div class = "col-md-3" style="text-align:center">
	         <a href="prodDetail?pnum=${pd.pnum}">
	         <c:if test="${pd.pimage1 ne null}">
	         <img src = "resources/product_images/${pd.pimage1}"
	         	style="width:90%;margin:auto;height:50%"
	          class = "img-fluid">
	         </c:if>
	         <c:if test="${pd.pimage1 eq null}">
	         <img src = "resources/product_images/noimage.png" class = "img-fluid"
	         style = "height:220px">
	         </c:if>
	         </a>
	         <br><br>
	         <h4> ${pd.pname} </h4>
	         <del> 
				<fmt:formatNumber value="${pd.price}" pattern="###,###"/>
	         </del> 원 <br>
	         <span style="color:blue;font-weight:bold"> 
	         	<fmt:formatNumber value="${pd.saleprice}" pattern="###,###"/>
	         </span> 원 <br>
	         <span class = "label label-danger">${pd.percent} %</span> <br>
	         <span class = "label label-success">${pd.point}</span> POINT <br>
	      </div> <!--  col-md-3 end -->
	      </c:forEach>
	   </c:if> 
      	</div> <!--  row end -->
		
	</div><!--  .container end -->

    