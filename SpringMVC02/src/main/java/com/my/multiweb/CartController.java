package com.my.multiweb;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.model.CartVO;
import com.shop.service.ShopService;
import com.user.model.UserVO;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/user")
@Log4j
public class CartController {
	
	@Inject
	private ShopService shopService;
	
	@PostMapping("/cartAdd")
	public String addCart(
			Model m, HttpSession session,
			@RequestParam(defaultValue="0") int pnum,
			@RequestParam(defaultValue="0") int oqty) {
		log.info("pnum==="+pnum+", oqty==="+oqty);
		if(pnum==0||oqty==0) {
			 return "redirect:../index";
		}
		
		UserVO loginUser=(UserVO)session.getAttribute("loginUser");
		int idx_fk=loginUser.getIdx();
		
		CartVO cvo=new CartVO();
		cvo.setPnum_fk(pnum);
		cvo.setOqty(oqty);
		//회원번호는 세션에서 로그인한 사람의 정보를 꺼내서 CartVO객체에 setting한다
		cvo.setIdx_fk(idx_fk);//임의의 회원번호를 설정
		
		int n=this.shopService.addCart(cvo);
		//장바구니에 상품 추가
		
		//장바구니 목록 가져오기
		//List<CartVO> cartArr=this.shopService.selectCartView(cvo.getIdx_fk());
		//m.addAttribute("cartArr",cartArr);
		//return "shop/cartList";
		//여기서 forward이동하면 브라우저 새로고침시 계속 상품이 추가되는 현상이 발생된다.
		//장바구니 총액이 계속 증가함===>redirect로 이동해야 함
		
		return "redirect:cartList";
	}
	
	@GetMapping("/cartList")
	public String cartList(Model m, HttpSession session) {
		
		UserVO loginUser=(UserVO)session.getAttribute("loginUser");
		int idx_fk=loginUser.getIdx();
		
		List<CartVO> cartArr=this.shopService.selectCartView(idx_fk);
		//특정 회원의 장바구니 총액 가져오기
		CartVO cartVo=this.shopService.getCartTotal(idx_fk);
		
		m.addAttribute("cartArr",cartArr);
		m.addAttribute("cartTotal",cartVo);
		
		return "shop/cartList";
	}//--------------------------------
	
	@PostMapping("/cartDel")
	public String cartDelete(@RequestParam(defaultValue="0") int cartNum) {
		if(cartNum==0) {
			return "redirect:cartList";
		}
		int n=shopService.delCart(cartNum);
		return "redirect:cartList";
	}//-----------------------------------
	
	@PostMapping("/cartEdit")
	public String cartEdit(@ModelAttribute("cvo") CartVO cvo) {
		log.info("cvo==="+cvo);
		
		shopService.editCart(cvo);
		
		return "redirect:cartList";
	}//-----------------------------------

}/////////////////////////////////////




