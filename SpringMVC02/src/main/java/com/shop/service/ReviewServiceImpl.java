package com.shop.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.shop.mapper.ReviewMapper;
import com.shop.model.ReviewVO;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Inject
	private ReviewMapper reviewMapper;

	@Override
	public int addReview(ReviewVO rvo) {

		return this.reviewMapper.addReview(rvo);
	}

	@Override
	public List<ReviewVO> listReview(int pnum_fk) {
		
		return this.reviewMapper.listReview(pnum_fk);
	}

	@Override
	public int getReviewCount(int pnum_fk) {
		return this.reviewMapper.getReviewCount(pnum_fk);
	}

	@Override
	public ReviewVO getReview(int num) {		
		return this.reviewMapper.getReview(num);
	}

	@Override
	public int updateReview(ReviewVO rvo) {
		return this.reviewMapper.updateReview(rvo);
	}

	@Override
	public int deleteReview(int num) {
		return this.reviewMapper.deleteReview(num);
	}

}
