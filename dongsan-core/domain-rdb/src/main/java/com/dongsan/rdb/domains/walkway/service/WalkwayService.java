package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.WalkwaySort;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalkwayService {
    private final WalkwayReader walkwayReader;

    @Autowired
    public WalkwayService(WalkwayReader walkwayReader) {
        this.walkwayReader = walkwayReader;
    }

    public PagingResponse<Walkway> searchWalkway(String sortType, SearchWalkwayQuery searchWalkwayQuery) {
        if (searchWalkwayQuery.lastWalkwayId() != null) {
            walkwayValidator.validateWalkwayExists(searchWalkwayQuery.lastWalkwayId());
        }

        WalkwaySort sort = WalkwaySort.typeOf(sortType);
        List<Walkway> walkways = walkwayReader.searchWalkway(searchWalkwayQuery, sort);
        return PagingResponse.from(walkways, searchWalkwayQuery.size());
    }

    @Transactional(readOnly = true)
    public PagingResponse<Walkway> getUserLikedWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        if (walkwayId != null) {
            // lastId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayReader.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkwayValidator.isOwnerOfWalkway(walkwayId, memberId);
            lastCreatedAt = walkway.createdAt();
        }
        List<Walkway> walkways = walkwayReader.getUserLikedWalkway(memberId, size + 1, lastCreatedAt);
        return PagingResponse.from(walkways, size);
    }

    @Transactional(readOnly = true)
    public PagingResponse<Walkway> getUserWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        if (walkwayId != null) {
            // lastId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayReader.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkwayValidator.isOwnerOfWalkway(walkwayId, memberId);
            lastCreatedAt = walkway.createdAt();
        }
        List<Walkway> walkways = walkwayReader.getUserWalkway(memberId, size + 1, lastCreatedAt);
        return PagingResponse.from(walkways, size);
    }

    @Transactional
    public Long createWalkwayHistory(CreateWalkwayHistory createWalkwayHistory) {
        walkwayValidator.validateWalkwayExists(createWalkwayHistory.walkwayId());
        return walkwayWriter.saveWalkwayHistory(createWalkwayHistory);
    }

    public PagingResponse<WalkwayHistory> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size,
                                                                     Long lastWalkwayHistoryId) {
        walkwayValidator.validateWalkwayPrivate(walkwayId);
        LocalDateTime lastCreatedAt = null;
        if (lastWalkwayHistoryId != null) {
            WalkwayHistory walkwayHistory = walkwayReader.getWalkwayHistory(lastWalkwayHistoryId);
            lastCreatedAt = walkwayHistory.createdAt();
        }
        List<WalkwayHistory> walkwayHistories = walkwayReader.getCanReviewWalkwayHistory(walkwayId, memberId, size + 1,
                lastCreatedAt);
        return PagingResponse.from(walkwayHistories, size);
    }

    public PagingResponse<WalkwayHistory> getUserCanReviewWalkwayHistory(Long memberId, Long lastWalkwayHistoryId,
                                                                         int size) {
        LocalDateTime lastCreatedAt = null;
        if (lastWalkwayHistoryId != null) {
            WalkwayHistory walkwayHistory = walkwayReader.getWalkwayHistory(lastWalkwayHistoryId);
            lastCreatedAt = walkwayHistory.createdAt();
        }
        List<WalkwayHistory> walkwayHistories = walkwayReader.getUserCanReviewWalkwayHistory(memberId, size + 1,
                lastCreatedAt);
        return PagingResponse.from(walkwayHistories, size);
    }

    public boolean isCanReview(Long walkwayHistoryId) {
        WalkwayHistory walkwayHistory = walkwayReader.getWalkwayHistory(walkwayHistoryId);
        return walkwayHistory.distance() >= (walkwayHistory.walkway()
                .courseInfo()
                .distance()) * 2 / 3;
    }

    @Transactional(readOnly = true)
    public PagingResponse<Walkway> getWalkways(Integer size, Long lastWalkwayId, Long memberId, String sort) {
        List<Walkway> walkways = switch (sort) {
            case "liked" -> walkwayReader.getWalkwaysLiked(size + 1, lastWalkwayId, memberId);
            case "rating" -> walkwayReader.getWalkwaysRating(size + 1, lastWalkwayId, memberId);
            default -> walkwayReader.getWalkwaysLatest(size + 1, lastWalkwayId, memberId);
        };

        return PagingResponse.from(walkways, size);
    }
}
