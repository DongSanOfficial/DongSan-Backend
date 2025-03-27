package com.dongsan.core.domains.review;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.core.domains.walkway.WalkwayHistoryValidator;
import com.dongsan.core.domains.walkway.WalkwayReader;
import com.dongsan.core.domains.walkway.WalkwayValidator;
import com.dongsan.core.domains.walkway.WalkwayWriter;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;

@Service
public class ReviewService {

	private final ReviewWriter reviewWriter;
	private final WalkwayReader walkwayReader;
	private final ReviewReader reviewReader;
	private final WalkwayWriter walkwayWriter;
	private final WalkwayHistoryValidator walkwayHistoryValidator;
	private final WalkwayValidator walkwayValidator;

	@Autowired
	public ReviewService(ReviewWriter reviewWriter, WalkwayReader walkwayReader,
		ReviewReader reviewReader, WalkwayWriter walkwayWriter,
		WalkwayHistoryValidator walkwayHistoryValidator, WalkwayValidator walkwayValidator) {
		this.reviewWriter = reviewWriter;
		this.walkwayReader = walkwayReader;
		this.reviewReader = reviewReader;
		this.walkwayWriter = walkwayWriter;
		this.walkwayHistoryValidator = walkwayHistoryValidator;
		this.walkwayValidator = walkwayValidator;
	}

	@Transactional
	public Long createReview(CreateReview createReview) {
		// 회원, 산책로, 이용기록 불러오기
		walkwayValidator.validateWalkwayExists(createReview.walkwayId());
		WalkwayHistory walkwayHistory
			= walkwayReader.getWalkwayHistory(createReview.walkwayHistoryId());

		// 이용 기록 검증
		walkwayHistoryValidator.validateWalkwayAndMember(walkwayHistory, createReview.walkwayId(),
			createReview.memberId());
		walkwayHistoryValidator.validateDistance(walkwayHistory);
		walkwayHistoryValidator.validateIsReviewed(walkwayHistory);

		// 이용기록 수정
		walkwayWriter.updateWalkwayHistoryIsReviewed(createReview.walkwayHistoryId(), true);

		// 리뷰 생성
		Long reviewId = reviewWriter.createReview(createReview);

		// 산책로 별점 수정
		Map<Rating, Long> ratingCounts = reviewReader.getWalkwaysRating(createReview.walkwayId());
		Integer totalReviewCount = RatingCalculator.calculateTotalReviewCount(ratingCounts);
		Double avgRating = totalReviewCount > 0
			? Math.round(RatingCalculator.calculateAverageRating(ratingCounts) * 10.0) / 10.0
			: 0.0;

		walkwayWriter.updateWalkwayRating(totalReviewCount, avgRating, createReview.walkwayId());

		return reviewId;
	}

	@Transactional(readOnly = true)
	public PagingResponse<Review> getWalkwayReviews(String type, Long walkwayId, Long memberId,
		CursorRequest cursorRequest) {
		Walkway walkway = walkwayReader.getWalkway(walkwayId);
		walkwayValidator.validateWalkwayAccess(walkway, memberId);

		Review review = null;
		if (cursorRequest.lastId() != null) {
			review = reviewReader.getReview(cursorRequest.lastId());
		}
		ReviewSort sort = ReviewSort.typeOf(type);
		List<Review> reviews = reviewReader.getWalkwayReviews(cursorRequest.size() + 1, review, walkwayId, sort);
		return PagingResponse.from(reviews, cursorRequest.size());
	}

	@Transactional(readOnly = true)
	public Map<Rating, Long> getWalkwayRating(Long walkwayId, Long memberId) {
		Walkway walkway = walkwayReader.getWalkway(walkwayId);
		walkwayValidator.validateWalkwayAccess(walkway, memberId);

		return reviewReader.getWalkwaysRating(walkwayId);
	}
}
