package com.my.multiweb;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shop.model.ReviewVO;
import com.shop.service.ReviewService;

import lombok.extern.log4j.Log4j;
/*--Restfull방식-------------------------------
 * - URI + 메서드 방식에 따라 처리 로직을 달리한다.
 * /reviews				GET : 특정 상품에 대한 리뷰 목록을 조회
 * /reivews/{num}  		GET : 특정(num번) 리뷰글을 조회
 * /user/reviews    	POST : 리뷰 글쓰기 처리
 * /user/reviews/{num}  PUT: 특정 리뷰글을 수정
 * /user/reivews/{num} 	DELETE: 특정 리뷰글을 삭제
 * --------------------------------------------
 * 
 * get/post방식은 일반적으로 지원되지만
 * 
 * put/delete방식일경우 404에러 발생하는 경우가 많다==>이런 경우 /WEB-INF/web.xml에 가서
 * filter를 등록하자. HiddenHttpMethodFilter, HttpPutFormContentFilter 등록하자.
 * ========web.xml======================================
 * <!-- Restful method======================================== -->
	<filter>
        <filter-name>hiddenHttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>hiddenHttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
    <filter>
        <filter-name>HttpPutFormContentFilter</filter-name>
        <filter-class>org.springframework.web.filter.HttpPutFormContentFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>HttpPutFormContentFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
 * ======================================================
 * */
@RestController
@Log4j
public class ReviewController {
	
	@Inject
	private ReviewService reviewService;
	
	@GetMapping(value="/reviews", produces = "application/json")
	public List<ReviewVO> reviewList(HttpSession ses){
		Integer pnum=(Integer)ses.getAttribute("pnum");
		
		log.info("pnum==="+pnum);
		List<ReviewVO> arr=this.reviewService.listReview(pnum);
		return arr;
	}
	
	@GetMapping(value="/reviewCnt", produces="application/json")
	public ModelMap getReviewCount(HttpSession ses) {
		Integer pnum=(Integer) ses.getAttribute("pnum");
		int count=this.reviewService.getReviewCount(pnum);
		ModelMap map=new ModelMap();
		map.put("count", count);
		return map;
	}
	
	@PostMapping(value="/user/reviews", produces = "application/xml")
	public ModelMap reviewInsert(@RequestParam(value="mfilename",required = false) MultipartFile mf,  @ModelAttribute("rvo") ReviewVO rvo, HttpSession ses) {
		log.info("Post rvo==="+rvo);
		
		ServletContext app=ses.getServletContext();
		//업로드 디렉토리 절대경로 없기
		String upDir=app.getRealPath("/resources/review_images");
		log.info("upDir===="+upDir);
		
		File dir=new File(upDir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		//업로드 처리
		try {
			mf.transferTo(new File(upDir, mf.getOriginalFilename()));
			rvo.setFilename(mf.getOriginalFilename());
			
		} catch (Exception e) {
			log.error(e);
		}
		
		int n=this.reviewService.addReview(rvo);
		
		ModelMap map=new ModelMap();
		map.addAttribute("result",n);
		return map;
	}//-------------------------
	//글 삭제 처리
	@DeleteMapping(value="/user/reviews/{num}", produces = "application/json")
	public ModelMap reviewDelete(@PathVariable("num") int num) {
		log.info("DELETE num==="+num);
		int n=this.reviewService.deleteReview(num);	
		ModelMap map=new ModelMap();
		map.addAttribute("result",n);
		return map;
	}//----------------------------------
	//특정 글 조회
	@GetMapping(value="/user/reviews/{num}", produces = "application/json")
	public ReviewVO getReview(@PathVariable("num") int num) {
		ReviewVO rvo=this.reviewService.getReview(num);
		return rvo;
	}
	//json데이터를 파라미터로 보내면 이것을 MemoVO로 받아 Map유형(=>json)으로 반환 처리 => 스프링은 자동으로 Converting한다
	//?name=aaa&idx=100 ==> VO객체로 받으려면 ==> @ModelAttribute를 붙인다.
	//json형태의 파라미터 데이터는 => VO객체로 받으려면 =>@RequestBody를 붙인다.
	//글 수정 처리
	@PutMapping(value="/user/reviews/{num}", produces="application/json")
	public ModelMap reviewUpdate(@PathVariable("num") int num, @RequestBody ReviewVO rvo) {
		log.info("PUT rvo==="+rvo);
		
		int n=this.reviewService.updateReview(rvo);
		
		ModelMap map=new ModelMap();
		map.put("result", n);
		return map;
	}

}













