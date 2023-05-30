package com.shop.service;

import java.util.List;

import com.shop.model.CategoryVO;
import com.shop.model.ProductVO;

public interface AdminService {
	
	public List<CategoryVO> getUpcategory();
	
	public List<CategoryVO> getDowncategory(String upCg_code);
	
	public int categoryAdd(CategoryVO cvo);
	public int categoryDelete(int cg_code);
	

	/** [관리자 모드]- 상품 정보 등록하기 */
	public int productInsert(ProductVO prod);
	public List<ProductVO> productList();
	

}
