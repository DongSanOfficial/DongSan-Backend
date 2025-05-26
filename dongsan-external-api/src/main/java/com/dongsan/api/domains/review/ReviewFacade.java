package com.dongsan.api.domains.review;

import com.dongsan.api.domains.review.dto.CreateReviewRequest;
import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.review.factory.ReviewSort;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.domain.domains.review.service.ReviewRdbService;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.service.MetaWalkwayRatingRdbService;
import com.dongsan.domain.domains.walkway.service.WalkwayRdbService;
import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.support.util.CursorRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewFacade {

    private final ReviewRdbService reviewRdbService;
    private final WalkwayRdbService walkwayRdbService;
    private final WalkwayLogRdbService walkwayLogRdbService;
    private final MetaWalkwayRatingRdbService metaWalkwayRatingRdbService;

    public ReviewFacade(ReviewRdbService reviewRdbService, WalkwayRdbService walkwayRdbService, WalkwayLogRdbService walkwayLogRdbService, MetaWalkwayRatingRdbService metaWalkwayRatingRdbService) {
        this.reviewRdbService = reviewRdbService;
        this.walkwayRdbService = walkwayRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
        this.metaWalkwayRatingRdbService = metaWalkwayRatingRdbService;
    }

    @Transactional
    public Long createReview(Long memberId, Long walkwayId, CreateReviewRequest request) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        WalkwayLog walkwayLog = walkwayLogRdbService.getWalkwayLog(request.walkwayLogId());
        walkwayLog.validateRelation(memberId, walkwayId);
        walkwayLog.validateSufficientDistance(walkway.getDistance());
        reviewRdbService.validateReviewWritable(walkwayLog.getId());
        metaWalkwayRatingRdbService.addRating(walkwayId, request.rating());
        return reviewRdbService.save(request.walkwayLogId(), request.rating(), request.content());
    }

    @Transactional(readOnly = true)
    public CursorPage<ReviewWithMemberQuery> getWalkwayReviews(String type, Long walkwayId, Long memberId,
                                                               CursorRequest paging) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        ReviewSort sort = ReviewSort.typeOf(type);
        Review review = paging.lastId() == null ? null : reviewRdbService.getReview(paging.lastId());
        return reviewRdbService.getWalkwayReviews(walkwayId, sort, review, paging.size());
    }

    @Transactional(readOnly = true)
    public ReviewStatistic getWalkwayRating(Long walkwayId, Long memberId) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        return reviewRdbService.getReviewStat(walkwayId);
    }
}
