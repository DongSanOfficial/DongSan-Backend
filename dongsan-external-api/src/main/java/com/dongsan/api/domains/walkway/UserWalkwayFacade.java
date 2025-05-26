package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.walkway.dto.response.WalkwayLogWithReviewResponse;
import com.dongsan.api.domains.walkway.dto.response.WalkwaySimpleResponse;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.domain.ReviewStatistic;
import com.dongsan.rdb.domains.review.service.ReviewRdbService;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.domain.WalkwaySnapshot;
import com.dongsan.rdb.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.rdb.domains.walkway.service.WalkwayRdbService;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLog;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.rdb.support.util.CursorPage;
import com.dongsan.rdb.support.util.CursorRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    public CursorPage<WalkwaySimpleResponse> getUserWalkway(Long memberId, CursorRequest paging) {
        CursorPage<Walkway> walkways = walkwayRdbService.getUserWalkway(memberId, paging.lastId(), paging.size());
        List<WalkwaySnapshot> walkwaySnapshots = walkways.getData().stream().map(Walkway::snapshot).toList();
        List<Long> walkwayIds = walkways.getData().stream().map(Walkway::getId).toList();
        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        List<WalkwaySimpleResponse> responses = WalkwaySimpleResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap);
        return new CursorPage<>(responses, walkways.getHasNext());
    }

    @Transactional(readOnly = true)
    public CursorPage<WalkwaySimpleResponse> getUserLikedWalkway(Long memberId, CursorRequest paging) {
        CursorPage<Walkway> walkways = walkwayRdbService.getUserLikedWalkway(memberId, paging.lastId(), paging.size());
        List<WalkwaySnapshot> walkwaySnapshots = walkways.getData().stream().map(Walkway::snapshot).toList();
        List<Long> walkwayIds = walkways.getData().stream().map(Walkway::getId).toList();
        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);
        List<WalkwaySimpleResponse> responses = WalkwaySimpleResponse.from(walkwaySnapshots, reviewStatMap, likeCountMap);
        return new CursorPage<>(responses, walkways.getHasNext());
    }

    @Transactional(readOnly = true)
    public CursorPage<WalkwayLogWithReviewResponse> getUserWalkwayHistoryWithReview(Long memberId, CursorRequest paging) {
        CursorPage<WalkwayLog> walkwayLogs = walkwayLogRdbService.getUserWalkwayLog(memberId, paging.lastId(), paging.size());
        List<Long> walkwayIds = walkwayLogs.getData().stream().map(WalkwayLog::getWalkwayId).toList();
        List<Long> walkwayLogIds = walkwayLogs.getData().stream().map(WalkwayLog::getId).toList();
        Map<Long, Walkway> walkwayMap = walkwayRdbService.getWalkways(walkwayIds);  // {walkwayId, Walkway}
        Map<Long, ReviewStatistic> reviewStatMap = reviewRdbService.getReviewStats(walkwayIds);  // {walkwayId, ReviewStatistic}
        Map<Long, Long> likeCountMap = likedWalkwayRdbService.countLikesMap(walkwayIds);  // {walkwayId, likeCount}
        Map<Long, Review> reviewMap = reviewRdbService.getReviews(walkwayLogIds);  // {walkwayLogId, Review}
        List<WalkwayLogWithReviewResponse> response = WalkwayLogWithReviewResponse.from(walkwayLogs.getData(), walkwayMap, reviewMap, reviewStatMap, likeCountMap);
        return new CursorPage<>(response, walkwayLogs.getHasNext());
    }


}
