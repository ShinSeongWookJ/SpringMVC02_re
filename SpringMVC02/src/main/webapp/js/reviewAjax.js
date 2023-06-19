

$(function(){
	show_reviews();//전체 리뷰 목록 가져오기

	//파일 업로드 처리시 ==> FormData객체에 form data를 담아 보내야 한다 (업로드)
	$('#rf').submit(function(evt){
		evt.preventDefault();
		//첨부파일
		const file=$('#mfilename')[0]
		//console.dir(file);
		//첨부파일명 얻기
		const fname=file.files[0]
		//alert(fname.name);
		const userid=$('#userid').val();
		const content=$('#content').val();
		const score=$('input[name="score"]:checked').val();
		//alert(score);
		const pnum_fk=$('#pnum_fk').val();
		
		console.log(userid+"/"+content+"/"+score+"/"+pnum_fk+"/"+fname);
		
		let fd=new FormData();
		fd.append('mfilename',fname);
		fd.append('userid',userid);
		fd.append('content',content);
		fd.append('score',score);
		fd.append('pnum_fk',pnum_fk);
		
		let url="user/reviews";
		/*
			processData:false, ==> 기본값: true ==>true면 enctype="application/x-www-form-urlencoded" 타입으로 전송한다.
			contentType:false, ==> 기본값: true enctype="application/x-www-form-urlencod"
			파일 업로드시는 enctype="multipart/form-data" 로 가야 하므로, false,false로 설정하자*/		
		$.ajax({
			type:'post',
			url:url,
			data:fd, //FormData객체 전달
			dataType:'xml',
			processData:false, //false 줘야 함
			contentType:false, //false
			cache:false,
			success:function(res){
				//alert(res);
				let result=$(res).find('result').text();
				if(result>0){
					//$('#reviewList').html("<h1>등록 성공</h1>");
					show_reviews();//전체 리뷰 목록 가져오기
				}else{
					alert('등록 실패');
				}
			},
			error:function(err){
				alert('err: '+err.status);
			}
			
		 });
		
		
	})

})//$() end-----

//리뷰목록 가져오기
const show_reviews=function(){
	let url="reviews"
	$.ajax({ 
		type:'get',
		url:url,
		dataType:'json',
		cache:false,
		success:function(res){
			//alert(JSON.stringify(res));
			showTable(res);			
		},
		error:function(err){
			alert('err: '+err.status);
		}
	});	
}//----------------------------

const showTable=function(res){
	let str='<table class="table table-striped">';
	$.each(res,function(i, rvo){	
			let d=new Date(rvo.wdate);
			let dstr=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
			
			str+='<tr><td width="15%">';
			if(rvo.filename==null){
			    str+='<img src="resources/product_images/noimage.png" class="img-thumbnail">';
			}else{
				str+='<img src="resources/review_images/'+rvo.filename+'" class="img-thumbnail">';
			}
			str+='</td><td width="60%" class="text-left">';
			str+=rvo.content+" <span class='float-right'>"+rvo.userid+"  [ "+dstr+" ]</span>";
			str+='</td>';
			str+='<td width="25%" class="text-left">';
			for(let k=0;k < rvo.score;k++){
				str+='<img src="resources/review_images/star.jpg">';
			}
			str+='<div class="mt-4">';
			
			str+='<a href="#reviewList" onclick="reviewEdit('+rvo.num+')">수정</a> | ';
			str+='<a href="#reviewList" onclick="reviewDel('+rvo.num+')">삭제</a>';
				
			str+='</div>';
			str+='</td>';
			str+='</tr>';
		});
		str+='</table>';
		
		$('#reviewList').html(str);
}//----------------------------

const reviewEdit=function(num){
	console.log(num);
	let url="reviews/"+num;
	$.ajax({
		type:'get',
		url:url,
		dataType:'json',
		cache:false,
		success:function(res){
			//alert(JSON.stringify(res));
			
			rf2.content.value=res.content;			
			
			$('#reviewModal').modal();//모달창 띄우기
		},
		error:function(err){
			alert('err');
		}
	});
}//---------------------------

const reviewDel = function(num){
	let url="user/reviews/"+num;
	$.ajax({
		type:'delete',
		url:url,		
		cache:false,
		success:function(res){
			//alert(JSON.stringify(res));
			if(res.result>0){
				show_reviews();//전체 리뷰목록 가져옥;
			}
			
		},
		error:function(err){
			alert('err:'+err.status);
		}
	});
}//-----------------------------


 //파일 업로드 없는 일반적 form data 전송시
 const send=function(){
 	let params=$('#rf').serialize(); 	
	let url="user/reviews"; 
	
	$.ajax({
		type:'post',
		url:url,
		data:params,
		cache:false,
		dataType:'xml',	
		success:function(res){
			//alert(res);//XMLDocument
			let result=$(res).find('result').text();
			alert(result);
			
		},
		error:function(err){
			alert('err'+err.status);
		}
	});	 	
 }