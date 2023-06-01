<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/top" />

<script>
	$(function(){
		$('#bt1').click(function(){
			let url="ajaxVO";
			$.ajax({
				url:url,
				type:'get',
				data:'num=100&name='+encodeURIComponent('홍길동'),
				dataType:'json',
				cache:false,
				success:function(res){
					//alert(res)
					let str='<h3>Num: '+res.idx+"</h3>";
					str+='<h3>Name: '+res.name+"</h3>";
					$('#resultView').html(str);
				},
				error:function(err){
					alert('err')
				}
				
			})
		});
		$('#bt2').click(function(){
			let url="ajaxList";
			$.ajax({
				url:url,
				type:'get',
				data:'idx=33&name='+encodeURIComponent("김철수"),
				dataType:'json',
				cache:false
			})
			.done(function(res){
				//alert(JSON.stringify(res));
				let str='<ul>';
					$.each(res, function(i, vo){
						str+='<li>'+vo.idx+", "+vo.name+", "+vo.msg+"</li>";
					})
					str+='</ul>'
				$('#resultView').html(str);
			})
			.fail(function(err){
				alert('err')
			})
		});
		
		$('#bt3').click(function(){
			let jsonData={
				idx:111,
				name:'King',
				msg:'How are you?',
				wdate:"2022-11-22"	
			};
			//json데이터를 파라미터로 보내고, 응답도 json으로 받아보자			
			$.ajax({
				type:'post', //요청방식은 post로 보내야 한다
				url:'ajaxRestJson',
				contentType:'application/json',//contentType을 json유형으로 지정하자
				data: JSON.stringify(jsonData),//json객체로 문자열로 직렬화해서 보내자.
				dataType:'json',
				cache:false,
				success:function(res){
					alert(JSON.stringify(res));
					let wdate=new Date(res.memo1.wdate);
					//getMonth(): 1월: 0을 반환, 2월 :1, ....11월:10, 12월:11
					let dateStr=wdate.getFullYear()+"-"+(wdate.getMonth()+1)+"-"+wdate.getDate();
					let str='<h3>작성자명: ';
						str+=res.memo1.name+"</h3>";
						str+='<h3>메모내용: '+res.memo1.msg+"</h3>";
						str+='<h3>작성일: '+dateStr+"</h3>";
						$('#resultView').html(str);
				},
				error:function(err){
					alert('err: '+err.status);
				}
			})
		})
		/*
		 const imageInput = $("#imageInput")[0];
  // 파일을 여러개 선택할 수 있으므로 files 라는 객체에 담긴다.
  console.log("imageInput: ", imageInput.files)

 	/*
				processData는 데이터를 일반적인 쿼리스트링으로 변환할 것인지를 결정함
				processData의 기본값은 true. 이는 기본값으로 
				'application/x-www-form-urlencode'타입으로 전송함.
				다른 타입으로 보내기 위해서는 false를 지정
				contentType:false,
				/*contentType의 기본값 역시 'application/x-www-form-urlencode'이므로
				multipart/form-data로 보내려면 false를 지정해야 함
  				const formData = new FormData();
  				formData.append("image", imageInput.files[0]);

		*/
		$('#frm').submit(function(e){
			e.preventDefault();
			const file=$('#filename')[0]
			//console.dir(file)
			if(file.files.length==0){
				alert('파일을 선택하세요')
				return;
			}
			let formData=new FormData();
			console.log(">>>"+file.files[0].name)
			formData.append('filename',file.files[0]);
			formData.append('name',$('#name').val())
			formData.append('msg',$('#msg').val())
			//alert(JSON.stringify(formData))
			let url='ajaxFile';
			$.ajax({
				type:'post',
				url:url,
				data:formData,
				contentType:false,//기본
				processData:false,//
				success:function(res){
					alert(res.result);
				},
				error:function(err){
					alert('err')
				}
			})
		})
		
		$('#frm2').submit(function(e){
			e.preventDefault();
			let jsondata={
					name:'aaa',
					msg:'bbb'
			}
			$.ajax({
                url:'ajaxPut',
                type:'put',                    
                data:JSON.stringify(jsondata),
                contentType:'application/json;charset=UTF-8',
                success:function(data){
                 alert(data.result)           
                 }            

            }); 
		})
	})//$() end------------------------
	
	
	
</script>

<div class="container mt-3" style="height: 600px; overflow: auto;">
	<h1 class="text-center">Ajax Test Page</h1>
	<button id="bt1" class="btn btn-outline-success">ajax(VO)</button>
	<button id="bt2" class="btn btn-outline-danger">ajax(List)</button>
	<button id="bt3" class="btn btn-outline-primary">ajaxRest(JSON파라미터전달==>VO로
		받기)</button>
	<hr>
	<form id="frm" method='post' enctype="multipart/form-data">
		올린이<input type="text" name="name" id="name"><br> 
		올린이<input type="text" name="msg" id="msg"><br>
		첨부파일:<input type="file"
			name="filename" id="filename"><br>
		<button>업로드</button>
	</form>
	
	<hr>
	<form id="frm2" method='post' enctype="multipart/form-data">
		올린이<input type="text" name="name2" id="name"><br> 
		올린이<input type="text" name="msg2" id="msg"><br>		
		<button>업로드</button>
	</form>
	<div id="resultView"></div>
</div>
<c:import url="/foot" />


