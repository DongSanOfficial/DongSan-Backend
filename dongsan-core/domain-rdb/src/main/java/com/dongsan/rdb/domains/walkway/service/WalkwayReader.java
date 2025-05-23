package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.WalkwaySort;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.factory.SearchWalkwayFactory;
import com.dongsan.rdb.domains.walkway.infrastructure.WalkwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WalkwayReader {

    @Autowired
    public WalkwayReader(WalkwayRepository walkwayRepository, SearchWalkwayFactory searchWalkwayFactory) {
        this.walkwayRepository = walkwayRepository;
        this.searchWalkwayFactory = searchWalkwayFactory;
    }

    private final WalkwayRepository walkwayRepository;
    private final SearchWalkwayFactory searchWalkwayFactory;


    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getUserWalkway(memberId, size, lastCreatedAt);
    }

    public boolean existsWalkway(Long walkwayId) {
        return walkwayRepository.existsWalkway(walkwayId);
    }

    public List<Walkway> searchWalkway(SearchWalkwayQuery searchWalkwayQuery, WalkwaySort sort) {
        return searchWalkwayFactory.getService(sort)
                .search(searchWalkwayQuery);
    }

    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayRepository.existsLikedWalkway(memberId, walkwayId);
    }

    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);
    }

    public List<WalkwayHistory> getUserCanReviewWalkwayHistory(Long memberId, int size, LocalDateTime lastCreatedAt) {
        return walkwayRepository.getUserCanReviewWalkwayHistory(memberId, size, lastCreatedAt);
    }

    public List<Walkway> getWalkwaysLatest(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayRepository.getWalkwaysLatest(size, lastWalkwayId, memberId);
    }

    public List<Walkway> getWalkwaysLiked(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayRepository.getWalkwaysLiked(size, lastWalkwayId, memberId);
    }

    public List<Walkway> getWalkwaysRating(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayRepository.getWalkwaysRating(size, lastWalkwayId, memberId);
    }
}
