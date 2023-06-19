package com.shop.service;

import java.util.List;

import com.shop.model.ReviewVO;

public interface ReviewService {
	
	public int addReview(ReviewVO rvo);
	
	public List<ReviewVO> listReview(int pnum_fk);
	
	public int getReviewCount(int pnum_fk);
	
	public ReviewVO getReview(int num);
	
	public int updateReview(ReviewVO rvo);
	
	public int deleteReview(int num);

}
