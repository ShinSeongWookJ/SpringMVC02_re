<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="/top"/>

   <div class="py-5">
    <div class="container">
      <div class="row">
        <div class="col-md-12">
           <h1 class="text-center">${loginUser.name}[${loginUser.userid}]님의 장바구니</h1>
           
        </div>
      </div>
      
      <div class="row">
        <div class="col-md-12">
        <!-- 주문 폼 시작-------------------------- -->
       <form name="orderF" id="orderF" action="order">
          
          <table class="table table-striped">
             <thead>
                <tr class="info text-center">
                   <th>상품번호</th>
                   <th>상품명</th>
                   <th>수량</th>
                   <th>단가</th>
                   <th>금액</th>
                   <th>삭제</th>
                </tr>
             </thead>
             <tbody>
             <c:choose>
               <c:when test="${cartArr eq null or empty cartArr}">
                	<tr>
                		<td colspan="6"><b>장바구니에 담긴 상품이 없습니다</b></td>
                	</tr>	
               </c:when> 	
               <c:otherwise> 	
               <c:forEach var="cvo" items="${cartArr}" varStatus="state">
               <!-- 
               	varStatus속성에 지정된 변수로 반복문의 상태정보를 알아낼 수 있다.
               	state.index : 인덱스값 (0 ~ 3)
               	state.count : 반복문 횟수 (1~ 4)
                -->
                <tr>
                   <td>
                      <label>
                      <input type="checkbox" name="pnum" id="pnum${state.index}"
                       value="${cvo.pnum_fk }">
                     ${cvo.pnum_fk}
                      </label>
                   </td>
                   <td>
                   <h4>${cvo.pname}</h4>
                   <br>
                   <a href="../prodDetail?pnum=${cvo.pnum_fk}" target="_blank">
                   <img src="../resources/product_images/${cvo.pimage1}"
                    class="img-thumbnail" style="width:140px"></a>
                   </td>
                   <td>
                   <input type="number" name="oqty" id="oqty${state.index}"
                    value="${cvo.oqty}"  min="1" max="50" size="3">
                    <button type="button" class="btn btn-success"
                     onclick="cartEdit('${cvo.cartNum}','${state.index}')">
                     수정</button>
                   </td>
                   <td>
                   <fmt:formatNumber value="${cvo.saleprice}"
                    pattern="###,###"/>
                   원<br>
                   <span class="badge badge-danger">${cvo.point}</span>POINT
                   </td>
                   <td>
                   <fmt:formatNumber value="${cvo.totalPrice}"
                    pattern="###,###"/>
                   원<br>
                   <span class="badge badge-danger">${cvo.totalPoint}</span>POINT
                   </td>
                   <td>
                   <a  href="#" onclick="cartDel('${cvo.cartNum}')">삭제</a>
                   </td>
                </tr>   
                </c:forEach>       
            	</c:otherwise>
            	</c:choose>
                <!-- -------------------- -->
             </tbody>
             <tr>
                <td colspan="3">
                   <h5>장바구니 총   액: <span class="text-danger">
                   <fmt:formatNumber value="${cartTotal.cartTotalPrice}"
                    pattern="###,###"/>
                   원</span>
                   </h5>
                   
                   <h5>장바구니 총포인트: <span class="text-success">
                   ${cartTotal.cartTotalPoint}
                   POINT</span>
                   </h5>
                </td>
                <td colspan="3">
                   <button type="button" onclick="goOrder()" class="btn btn-outline-info">주문하기</button> 
                   <!-- form tag안에 버튼이 있으면 default 로 submit ==> order.jsp -->
                   <button type="button"
                    class="btn btn-outline-warning"
                     onclick="location.href='../index'">계속쇼핑</button>
                </td>
             </tr>
          </table>
       </form>
       <!-- ----------------------------------- -->
        </div>
      </div>
      <!-- 삭제form------------------------- -->
      <form name="df" action="cartDel">
            <input type="hidden" name="cartNum">
      </form>
      <!-- --------------------------------- -->
      
      <!-- 수정form ------------------------- -->
      <form name="ef" action="cartEdit">
            <input type="hidden" name="cartNum">
            <input type="hidden" name="oqty">
      </form>
      <!-- --------------------------------- -->
      
      <script>
      	function cartEdit(cnum, i){
      		//alert(cnum+"/"+i);
      		let qty=$('#oqty'+i).val();
      		//alert(qty);
      		ef.cartNum.value=cnum;
      		ef.oqty.value=qty;
      		ef.method='post';
      		ef.submit();
      	}
      
      	function cartDel(cnum){
      		//alert(cnum);
      		df.cartNum.value=cnum;
      		df.method='post';
      		df.submit();
      	}
         
      </script>
      
    </div>
  </div>
 
<c:import url="/foot" />