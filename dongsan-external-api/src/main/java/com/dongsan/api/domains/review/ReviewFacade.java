package com.dongsan.api.domains.review;

import com.dongsan.api.domains.review.dto.CreateReviewRequest;
import com.dongsan.rdb.domains.review.RatingCalculator;
import com.dongsan.rdb.domains.review.ReviewSort;
import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.service.ReviewRdbService;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.service.WalkwayRdbService;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLog;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.rdb.support.util.CursorRequest;
import com.dongsan.rdb.support.util.PagingResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReviewFacade {

    private final ReviewRdbService reviewRdbService;
    private final WalkwayRdbService walkwayRdbService;
    private final WalkwayLogRdbService walkwayLogRdbService;

    public ReviewFacade(ReviewRdbService reviewRdbService, WalkwayRdbService walkwayRdbService, WalkwayLogRdbService walkwayLogRdbService) {
        this.reviewRdbService = reviewRdbService;
        this.walkwayRdbService = walkwayRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
    }

    public Long createReview(Long memberId, Long walkwayId, CreateReviewRequest request) {
        walkwayRdbService.getWalkway(walkwayId);
        WalkwayLog walkwayLog = walkwayLogRdbService.getWalkwayLog(request.walkwayLogId());
        walkwayLogRdbService.validateReviewWritable(request.walkwayLogId());
        

        // 기존 산책로의 2/3 이상을 걸어야 등록 가능 (이거는 도메인 내부에 넣자) & 리뷰 등록

        // TODO : 히스토리 어떻게 쌓을건지 고민 필요
        return reviewRdbService.save(request.walkwayLogId(), request.rating(), request.content());
    }

    @Transactional
    public Long createReview(CreateReview createReview) {

        // 이용 기록 검증
        walkwayLogValidator.validateWalkwayAndMember(walkwayHistory, createReview.walkwayId(),
                createReview.memberId());
        walkwayLogValidator.validateDistance(walkwayHistory);
        walkwayLogValidator.validateIsReviewed(walkwayHistory);

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
