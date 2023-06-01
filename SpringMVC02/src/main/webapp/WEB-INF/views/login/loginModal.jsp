<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- The Modal -->
<div class="modal" id="loginModal">
  <div class="modal-dialog">
    <div class="modal-content">
	   <form name="loginF" method='post'
	    action='${pageContext.request.contextPath}/login'>
      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title">Login</h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>
		
      <!-- Modal body -->
      <div class="modal-body">
      	<div class="form-group">
      		<label for="userid">아이디:</label>
      		<input type="text" name="userid" id="userid" placeholder="User ID" required class="form-control">
      	</div>
      
      	<div class="form-group">
      		<label for="pwd">비밀번호:</label>
      		<input type="password" name="pwd" id="pwd" placeholder="Password" required class="form-control">
      	</div>
       
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
      	<button class="btn btn-info">Login</button>
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
      </div>
		</form>
    </div>
  </div>
</div>