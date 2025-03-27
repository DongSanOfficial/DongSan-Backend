package com.dongsan.core.domains.review;

import org.springframework.stereotype.Component;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;

@Component
public class ReviewValidator {
	private final ReviewRepository reviewRepository;

	public ReviewValidator(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	public void isReviewOwner(Long reviewId, Long memberId) {
		boolean result = reviewRepository.existsByIdAndMemberId(reviewId, memberId);
		if (!result) {
			throw new CoreException(CoreErrorCode.NOT_REVIEW_OWNER);
		}
	}
}
