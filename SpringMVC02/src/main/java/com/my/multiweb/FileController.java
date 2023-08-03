package com.my.multiweb;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
//import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import lombok.extern.log4j.Log4j;
/*[1] 파일 업로드 처리 위해서는 
 * pom.xml에 commons-fileupload, common-io 라이브러리를 등록한다
 *[2] servlet-context.xml에 MulipartResolver빈을 등록한다. 이때 빈의 id는 반드시 "multipartResolver"로 등록해야 한다.
 * 							다른 아이디를 주면 DispatcherServlet이 MultipartResolver로	인식하질 못한다.
 * 
 * <!-- 파일 업로드 위한 MultipartResolver 설정 ============================== -->
	<!--주의: 빈의 id는 반드시 multipartResolver로 등록해야 한다.
	다른 아이디를 주면 DispatcherServlet이 MultipartResolver로
	인식하질 못한다.  -->
	<beans:bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<beans:property name="defaultEncoding" value="UTF-8"/>
		<beans:property name="maxUploadSize" value="-1"/>
		<!-- -1로 주면 무제한 업로드 가능 -->
	</beans:bean>
 * */
@Controller
@Log4j
public class FileController {
	
	@Resource(name="upDir")
	private String upDir;
	
	@GetMapping("/fileForm")
	public String fileForm() {
		return "file/fileForm";
	}
	//[1] MultipartFile을 이용하는 방법
	@PostMapping("/fileUp")
	public String fileUpload(Model m,@RequestParam("name") String name, @RequestParam("mfilename")MultipartFile mfilename) {
		log.info("upDir: "+upDir+", name: "+name+", mfilename: "+mfilename);
		//1. 첨부파일명, 파일 크기 얻어와서 Model에 저장
		if(!mfilename.isEmpty()) {
			String filename=mfilename.getOriginalFilename();//첨부파일명
			long filesize=mfilename.getSize();
			
			//2. 파일 업로드 처리 -> transferTo()메서드 이용
			try {
				mfilename.transferTo(new File(upDir, filename));			
			} catch (IOException e) {
				log.info("파일 업로드 실패: "+e);
				log.error(e);
			}
			
			m.addAttribute("fname", filename);
			m.addAttribute("fsize", filesize);			
		}//if------------
		m.addAttribute("name", name);
		return "file/fileResult";
	}//------------------------------------
	
	//[2] MulitipartHttpServletRequest를 이용하는 방법
	//==> 이 경우는 동일한 파라미터명으로 여러 개의 파일을 업로드하는 경우에 유용하다.
	@PostMapping("/fileUp2")
	public String fileUpload2(Model m, HttpServletRequest req) {
		//1. 올린이 파라미터값 받기
		String name=req.getParameter("name");
		//2. MultipartHttpServletRequest유형으로 형변환
		MultipartHttpServletRequest  mr=(MultipartHttpServletRequest)req;
		//3. 첨부파일 목록 얻기
		List<MultipartFile> mflist=mr.getFiles("mfilename");
		m.addAttribute("name",name);
		if(mflist!=null) {
			for(int i=0;i<mflist.size();i++) {
				MultipartFile mf=mflist.get(i);
				//1.파라미터명: mfilename
				String paramName=mf.getName();
				//2. 첨부파일명
				String fname=mf.getOriginalFilename();
				//3. 파일 크기
				long fsize=mf.getSize();
				//4. 파일 업로드 처리
				m.addAttribute(paramName+(i+1), fname);
				m.addAttribute("fsize"+(i+1), fsize);
				
				try {
					mf.transferTo(new File(upDir, fname));
				}catch(IOException e) {
					log.error(e);
				}
			}//-----------------
		}
		m.addAttribute("name",name);
		return "file/fileResult2";
	}
	/* 데이터와 함께 헤더 상태 메시지를 전달하고자 할때 사용한다.
	 * Http Header를 다뤄야 할 경우 ResponseEntity를 통해 헤더정보나 데이터를 전달할 수 있다.
	 * HttpEntity를 상속받아 구현한 클래스
	 * - RequestEntity : 요청 헤더정보+요청 데이터
	 * - ResponseEntity(HttpStatus, HttpHeaders, HttpBody를 포함함): 응답 헤더정보+ 응답 데이터
	 * 
	 *브라우저는 컨텐트타입이 보여줄 수 있는 형식이면 브라우저에 보여주고,
	 *잘 모르는 컨텐트타입이거나 보여줄 수 있는 형식이 아니면 다운로드 창을 띄운다. 
	 * */
	@PostMapping(value="/fileDown", produces = "application/octet-stream")
	@ResponseBody
	public ResponseEntity<org.springframework.core.io.Resource> fileDownload(
			HttpServletRequest req,
			@RequestHeader("User-Agent") String userAgent,
			@RequestParam("fname") String fname,
			@RequestParam("origin_fname") String origin_fname){
		log.info("userAgent==="+userAgent);
		log.info("fname==="+fname);
		log.info("origin_fname==="+origin_fname);
		//1. 업로드된 디렉토리 절대경로 얻기
		ServletContext app=req.getServletContext();
		String upDir=app.getRealPath("/resources/board_upload");
		
		String filePath=upDir+File.separator+fname;
		log.info("filePath==="+filePath);
		
		
		org.springframework.core.io.Resource resource=new FileSystemResource(filePath);//uuid_파일명
		//FileSystemResource 가 알아서 파일과 스트림 연결을 한다
		if(!resource.exists()) {
			//해당 파일이 없다면
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		//2. 브라우저별 인코딩 처리
		String downName=null;
		boolean checkIE=(userAgent.indexOf("MSIE")>-1 || userAgent.indexOf("Trident")>-1);
		try {
			if(checkIE){
				//IE인 경우
				downName=URLEncoder.encode(origin_fname,"UTF-8").replaceAll("\\+", " ");
			}else {		
				//그외 브라우저인 경우
				origin_fname=origin_fname.replace(",", "");//크롬은 파일명에 콤마(,) 있으면 다운로드 되지 않음
				downName=new String(origin_fname.getBytes("UTF-8"),"ISO-8859-1");
			}
		}catch(UnsupportedEncodingException e) {
			log.error("파일 다운로드 중 에러:"+e);
		}
		
		//3. HttpHeader통해 헤더 정보 설정
		HttpHeaders headers=new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename="+downName);
		
		return new ResponseEntity<>(resource,headers,HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	

}
