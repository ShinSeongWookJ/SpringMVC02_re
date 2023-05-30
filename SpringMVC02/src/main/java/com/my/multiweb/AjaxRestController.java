package com.my.multiweb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/* Rest : Representational State Transfer의 약자
 * - 전송방식과 URI를 결합해서 원하는 작업을 지정하여 처리하도록 하는 방식
 * 	 GET/POST/PUT/DELETE
 *   - GET : 조회
 *   - DELETE : 삭제처리
 *   - POST : INSERT처리
 *   - PUT : Update처리
 *   ...
 *   URI +GET/POST/PUT/DELETE
 *   
 *   GET : /users/100 ==> 100번 회원의 정보를 조회하는 로직 처리한다
 *   GET : /users ===> 모든 회원 목록을 조회하는 로직을 처리한다
 *   DELETE: /delete/3 ==> 3번 회원 정보를 삭제 처리...
 * @RestController==> REST방식의 데이터를 처리하는 여러 기능을 쉽게 할 수 있다.
 * */

import com.memo.model.MemoVO;

import lombok.extern.log4j.Log4j;
@RestController
@Log4j
public class AjaxRestController {
	
	@GetMapping(value="/ajaxText", produces = "text/plain; charset=UTF-8")
	public String ajaxText() {
		
		return "Hello Ajax 안녕하세요";
	}
	
	@GetMapping(value="/ajaxVO2", produces = "application/json")
	public MemoVO ajaxVO() {
		MemoVO vo=new MemoVO(55,"김철이","반가워요",null);
		return vo;
	}
	
	@GetMapping(value="/ajaxVO3", produces = "application/xml")
	public MemoVO ajaxVO3() {
		MemoVO vo=new MemoVO(55,"김철이","반가워요",null);
		return vo;
	}
	//List유형을 반환하는 메서드 구성해서 ==> 요청 보내서 받아보기 (json, xml)
	@GetMapping(value="/ajaxVO4",produces="application/xml")
	public List<MemoVO> ajaxVO4() {
		List <MemoVO> list=new ArrayList<>();
		MemoVO vo1= new MemoVO(55,"김철준","반가워요",null);
		MemoVO vo2= new MemoVO(56,"김균중","반가워요",null);
		MemoVO vo3= new MemoVO(57,"김사랑","반가워요",null);
		list.add(vo1);
		list.add(vo2);
		list.add(vo3);
		return list;
	}
	@GetMapping(value="/ajaxVO5",produces="application/json")
	public List<MemoVO> ajaxVO5() {
		List <MemoVO> list=new ArrayList<>();
		MemoVO vo1= new MemoVO(55,"김철준","반가워요",null);
		MemoVO vo2= new MemoVO(56,"김균중","반가워요",null);
		MemoVO vo3= new MemoVO(57,"김사랑","반가워요",null);
		list.add(vo1);
		list.add(vo2);
		list.add(vo3);
		return list;
	}
	//Map유형을 반환하는 메서드 구성해서 ==> 요청 보내서 받아보기 (json, xml)
	@GetMapping(value="/ajaxMap", produces="application/json")
	public Map<String, String> ajaxMap(){
		Map<String, String> map=new HashMap<>();
		map.put("isbn", "778899");
		map.put("title", "Ajax입문");
		map.put("author", "홍길동");
		return map;
	}
	
	@GetMapping(value="/ajaxMap2", produces="application/xml")
	public Map<String, String> ajaxMap2(){
		Map<String, String> map=new HashMap<>();
		map.put("isbn", "778899");
		map.put("title", "Ajax입문");
		map.put("author", "홍길동");
		return map;
	}
	//path접근 방식으로 특정 번호를 파라미터로 받아 특정번호의는 메모를 json으로 반환해보자.
	//?name=aaa&idx=100 ==> @RequestParam("name") 식으로 받는다.
	// /memo/100==>path접근방식의 요청일 경우 => @PathVariable("idx") 
	@GetMapping(value="/memo/{idx}", produces="application/json")
	public MemoVO ajaxPath(@PathVariable("idx") int idx) {
		log.info("ajaxPath() idx==="+idx);
		MemoVO vo1=new MemoVO(100,"김길동","안녕하세요", null);
		MemoVO vo2=new MemoVO(200,"최순희","반가워요", null);
		if(idx==100) return vo1;
		if(idx==200) return vo2;
		else return new MemoVO();		
	} 
	//json데이터를 파라미터로 보내면 이것을 MemoVO로 받아 Map유형(=>json)으로 반환 처리 => 스프링은 자동으로 Converting한다
	//?name=aaa&idx=100 ==> VO객체로 받으려면 ==> @ModelAttribute를 붙인다.
	//json형태의 파라미터 데이터는 => VO객체로 받으려면 =>@RequestBody를 붙인다.
	@PostMapping(value="/ajaxRestJson", produces="application/json")
	public Map<String, MemoVO> ajaxRestJson(@RequestBody MemoVO vo){
		log.info("ajaxRestJson()  vo==="+vo);
		
		Map<String, MemoVO> map=new HashMap<String, MemoVO>();
		map.put("memo1", vo);
		map.put("memo2", new MemoVO(222,"이순신","나를 따르라~~",null));
		return map;
	}
	
	

}





