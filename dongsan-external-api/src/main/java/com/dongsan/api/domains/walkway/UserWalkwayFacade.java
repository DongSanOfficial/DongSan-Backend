package com.dongsan.api.domains.walkway;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.api.domains.walkway.dto.response.WalkwayLogWithReviewResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwaySimpleResponse;
import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.review.service.ReviewRdbService;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.domain.WalkwaySnapshot;
import com.dongsan.domain.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.domain.domains.walkway.service.WalkwayRdbService;
import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;

@Service
public class UserWalkwayFacade {
    private final WalkwayRdbService walkwayRdbService;
    private final LikedWalkwayRdbService likedWalkwayRdbService;
    private final WalkwayLogRdbService walkwayLogRdbService;
    private final ReviewRdbService reviewRdbService;

    public UserWalkwayFacade(WalkwayRdbService walkwayRdbService, LikedWalkwayRdbService likedWalkwayRdbService, WalkwayLogRdbService walkwayLogRdbService, ReviewRdbService reviewRdbService) {
        this.walkwayRdbService = walkwayRdbService;
        this.likedWalkwayRdbService = likedWalkwayRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
        this.reviewRdbService = reviewRdbService;
    }

    @Transactional(readOnly = true)
    public CursorResponse<WalkwaySimpleResponse> getUserWalkway(Long memberId, CursorRequest paging) {
        CursorResponse<Walkway> walkways = walkwayRdbService.getUserWalkway(memberId, paging.lastId(), paging.size());
        List<WalkwaySnapshot> walkwaySnapshots = walkways.data().stream().map(Walkway::snapshot).toList();
        List<Long> walkwayIds = walkways.data().stream().map(Walkway::getId).toList();
        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        List<WalkwaySimpleResponse> responses = WalkwaySimpleResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap);
        return new CursorResponse<>(responses, walkways.hasNext());
    }

    @Transactional(readOnly = true)
    public CursorResponse<WalkwaySimpleResponse> getUserLikedWalkway(Long memberId, CursorRequest paging) {
        CursorResponse<Walkway> walkways = walkwayRdbService.getUserLikedWalkway(memberId, paging.lastId(), paging.size());
        List<WalkwaySnapshot> walkwaySnapshots = walkways.data().stream().map(Walkway::snapshot).toList();
        List<Long> walkwayIds = walkways.data().stream().map(Walkway::getId).toList();
        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        List<WalkwaySimpleResponse> responses = WalkwaySimpleResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap);
        return new CursorResponse<>(responses, walkways.hasNext());
    }

    @Transactional(readOnly = true)
    public CursorResponse<WalkwayLogWithReviewResponse> getUserWalkwayHistoryWithReview(Long memberId, CursorRequest paging) {
        CursorResponse<WalkwayLog> walkwayLogs = walkwayLogRdbService.getUserWalkwayLog(memberId, paging.lastId(), paging.size());
        List<Long> walkwayIds = walkwayLogs.data().stream().map(WalkwayLog::getWalkwayId).toList();
        List<Long> walkwayLogIds = walkwayLogs.data().stream().map(WalkwayLog::getId).toList();
        Map<Long, Walkway> walkwayMap = walkwayRdbService.getWalkways(walkwayIds);  // {walkwayId, Walkway}
        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);  // {walkwayId, ReviewStatistic}
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);  // {walkwayId, likeCount}
        Map<Long, Review> reviewMap = reviewRdbService.getReviews(walkwayLogIds);  // {walkwayLogId, Review}
        List<WalkwayLogWithReviewResponse> response = WalkwayLogWithReviewResponse.from(walkwayLogs.data(), walkwayMap, reviewMap, reviewStatMap, likeCountMap);
        return new CursorResponse<>(response, walkwayLogs.hasNext());
    }


}
