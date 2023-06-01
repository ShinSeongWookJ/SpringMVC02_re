package com.shop.mapper;

import java.util.List;

import com.shop.model.CartVO;

public interface CartMapper {
	
	Integer selectCartCountByPnum(CartVO cartVo);
	
	int updateCartQty(CartVO cartVo);
	int addCart(CartVO cartVo);
	
	List<CartVO> selectCartView(int idx_fk);
	int delCart(int cartNum);
	int editCart(CartVO cartVo);
	CartVO getCartTotal(int idx_fk);

}
