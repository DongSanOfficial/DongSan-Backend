package com.dongsan.core.domains.walkway;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalkwayReader {

    @Autowired
    public WalkwayReader(WalkwayRepository walkwayRepository, SearchWalkwayFactory searchWalkwayFactory) {
        this.walkwayRepository = walkwayRepository;
        this.searchWalkwayFactory = searchWalkwayFactory;
    }

    private final WalkwayRepository walkwayRepository;
    private final SearchWalkwayFactory searchWalkwayFactory;

    public Walkway getWalkway(Long walkwayId) {
        return walkwayRepository.getWalkway(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
    }

    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt){
        return walkwayRepository.getUserWalkway(memberId, size, lastCreatedAt);
    }

    public boolean existsWalkway(Long walkwayId) {
        return walkwayRepository.existsWalkway(walkwayId);
    }

    public List<Walkway> searchWalkway(SearchWalkwayQuery searchWalkwayQuery, WalkwaySort sort) {
        return searchWalkwayFactory.getService(sort).search(searchWalkwayQuery);
    }

    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayRepository.existsLikedWalkway(memberId, walkwayId);
    }

    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return walkwayRepository.existsLikedWalkways(memberId, walkwayIds);
    }

    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);
    }

    public WalkwayHistory getWalkwayHistory(Long walkwayHistoryId) {
        return walkwayRepository.getWalkwayHistory(walkwayHistoryId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_HISTORY_NOT_FOUND));
    }

    public List<WalkwayHistory> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getCanReviewWalkwayHistory(walkwayId, memberId, size, lastCreatedAt);
    }

    public List<WalkwayHistory> getUserCanReviewWalkwayHistory(Long memberId, int size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getUserCanReviewWalkwayHistory(memberId, size, lastCreatedAt);
    }
}
