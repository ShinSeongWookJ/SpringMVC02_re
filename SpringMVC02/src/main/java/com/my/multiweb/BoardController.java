package com.my.multiweb;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.board.model.BoardVO;
import com.board.service.BoardService;
import com.common.CommonUtil;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/board")
@Log4j
public class BoardController {
	
	@Resource(name="boardServiceImpl")
	private BoardService boardService;
	
	@Inject
	private CommonUtil util;
	
	@GetMapping("/write")
	public String boardForm() {
		
		return "board/boardWrite";
	}
	@PostMapping("/write")
	public String boardInsert(Model m,
			HttpServletRequest req,
			@RequestParam("mfilename") MultipartFile mfilename, @ModelAttribute BoardVO board) {
		log.info("board==="+board);
		//1. 파일 업로드 처리
		//업로드 디렉토리 절대경로 얻기
		ServletContext app=req.getServletContext();
		String upDir=app.getRealPath("/resources/board_upload");
		File dir=new File(upDir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		if(!mfilename.isEmpty()) {//첨부파일이 있다면
			//1) 먼저 첨부파일명과 파일크기를 알아내자
			String originFname=mfilename.getOriginalFilename();//원본파일명
			long fsize=mfilename.getSize();//파일크기
			log.info(originFname+">>>"+fsize);
			
			//2) 동일한 파일명이 서버에 있을 경우 덮어쓰기를 방지하기 위해
			//   "랜덤한문자열_원본파일명"==>물리적 파일명을 생성하자
			UUID uuid=UUID.randomUUID();
			String filename=uuid.toString()+"_"+originFname;//물리적파일명=>실제 업로드시킬 파일명
			log.info("filename==="+filename);
			
			//3_1) mode가 edit(수정)이고 예전에 첨부했던 파일이 있다면
			//예전 파일 삭제 처리
			if(board.getMode().equals("edit")&& board.getOld_filename()!=null) {
				//수정 모드라면 예전에 업로드했던 파일은 삭제 처리
				File delF=new File(upDir, board.getOld_filename());
				if(delF.exists()) {
					boolean b=delF.delete();
					log.info("old file삭제여부: "+b);
				}
				
			}
			
			//3_2) 업로드 처리
			try {
				mfilename.transferTo(new File(upDir, filename));
				log.info("upDir==="+upDir);
			} catch (Exception e) {
				log.error("board write upload error:"+e);
			}
			
			//4) BoardVO객체에 filename,originFilename,filesize 셋팅
			board.setFilename(filename);
			board.setOriginFilename(originFname);
			board.setFilesize(fsize);
		}
		
		
		//2. 유효성 체크 (subject, name, passwd)==> redirect "write"
		if(board.getName()==null||board.getSubject()==null||board.getPasswd()==null||
				board.getName().trim().isEmpty()||board.getSubject().trim().isEmpty()|| board.getPasswd().trim().isEmpty()) {
			return "redirect:write";
		}
		
		//3. boardService의 insertBoard()호출하기
		int n=0;
		String str="",loc="";
		if("write".equals(board.getMode())) {//글쓰기 모드라면
			
			n=this.boardService.insertBoard(board);//글쓰기 처리 메서드 호출
			str="글쓰기 ";
			
		}else if("rewrite".equals(board.getMode())) {//답변 글쓰기 모드라면
			n=this.boardService.rewriteBoard(board);
			str="답변 글쓰기 ";		
		}else if("edit".equals(board.getMode())) {//글 수정이라면
			
			n=this.boardService.updateBoard(board);//글 수정 처리 메서드 호출
			str="글수정 ";
		}
		str+=(n>0)?"성공":"실패";
		loc=(n>0)?"list":"javascript:history.back()";
		
		//4. 그 결과 message, loc 저장
		//m.addAttribute("message","test");
		//m.addAttribute("loc","list");
		
		return util.addMsgLoc(m,str,loc);//msg를 반환
	}//--------------------------------------
	@GetMapping("/list")
	public String boardList(Model m) {
		//게시판 목록 가져와서 모델에 저장하기
		//"boardArr"
		List<BoardVO> boardArr=this.boardService.selectBoardAll(null);
		m.addAttribute("boardArr", boardArr);
		
		return "board/boardList";
	}//---------------------------
	
	@GetMapping("/view/{num}")
	public String boardView(Model m,@PathVariable("num") int num) {
		log.info("num==="+num);
		//조회수 증가
		int n=this.boardService.updateReadnum(num);
		
		BoardVO board=this.boardService.selectBoardByIdx(num);
		m.addAttribute("board",board);
		
		return "board/boardView";
	}//-------------------------------
	
	@PostMapping("/delete")
	public String boardDelete(Model m, 
			HttpServletRequest req,
			@RequestParam(defaultValue = "0") int num,
			@RequestParam(defaultValue = "") String passwd) {
		log.info("num=="+num+", passwd=="+passwd);
		if(num==0||passwd.isEmpty()) {
			return "redirect:list";
		}
		//해당 글을 db에서 가져오기
		BoardVO vo=this.boardService.selectBoardByIdx(num);
		if(vo==null) {
			return util.addMsgBack(m, "해당 글은 존재하지 않아요");
		}
		//비밀번호 일치여부 체크해서 일치하면 삭제 처리
		String dbPwd=vo.getPasswd();
		if(!dbPwd.equals(passwd)) {
			return util.addMsgBack(m, "비밀번호가 일치하지 않아요");
		}
		//db에서 글 삭제처리
		int n=this.boardService.deleteBoard(num);
		
		ServletContext app=req.getServletContext();
		String upDir=app.getRealPath("/resources/board_upload");
		log.info("upDir===="+upDir);
		
		//서버에 업로드한 첨부파일이 있다면 서버에서 삭제 처리
		if(n>0 && vo.getFilename()!=null) {
			File f=new File(upDir, vo.getFilename());
			if(f.exists()) {
				boolean b=f.delete();
				log.info("파일삭제 여부: "+b);
			}
		}
		String str=(n>0)?"글 삭제 성공":"삭제 실패";
		String loc=(n>0)?"list":"javascript:history.back()";
		
		return util.addMsgLoc(m, str, loc);
	}//-------------------------------
	
	@PostMapping("/edit")
	public String boardEditform(Model m,
			@RequestParam(defaultValue = "0") int num,
			@RequestParam(defaultValue = "") String passwd) {
		//1. 글번호,비번 유효성 체크 ==> list redirect이동
		if(num==0||passwd.isEmpty()) {
			return "redirect:list";
		}
		
		//2. 글번호로 해당 글 내용 가져오기
		//   없으면 "없는 글입니다"
		BoardVO vo=this.boardService.selectBoardByIdx(num);
		if(vo==null) {
			return util.addMsgBack(m, "해당 글은 없어요");
		}
		
		//3. 비번 체크-> 일치하지 않으면 "불일치" back이동
		if(!vo.getPasswd().equals(passwd)) {
			return util.addMsgBack(m, "비밀번호가 일치하지 않아요");
		}
		
		//4. Model에 해당 글 저장 "board"
		m.addAttribute("board",vo);
		
		return "board/boardEdit";
		//boardWrite.jsp를 save as해서 만들기
	}//-------------------------------
	
	@PostMapping("/rewrite")
	public String boardRewrite(Model m, @ModelAttribute BoardVO vo) {
		log.info("vo==="+vo);
		m.addAttribute("num", vo.getNum());//부모글의 글번호
		m.addAttribute("subject",vo.getSubject());//부모글의 제목
		return "board/boardRewrite";
	}

}//////////////////////////////////












