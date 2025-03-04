package com.dongsan.core.domains.walkway;


import com.dongsan.core.support.util.PagingResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalkwayService {
    private final WalkwayReader walkwayReader;
    private final WalkwayWriter walkwayWriter;
    private final WalkwayValidator walkwayValidator;

    @Autowired
    public WalkwayService(WalkwayReader walkwayReader, WalkwayWriter walkwayWriter, WalkwayValidator walkwayValidator) {
        this.walkwayReader = walkwayReader;
        this.walkwayWriter = walkwayWriter;
        this.walkwayValidator = walkwayValidator;
    }

    @Transactional
    public Long createWalkway(CreateWalkway createWalkway) {
        return walkwayWriter.saveWalkway(createWalkway);
    }

    public Walkway getWalkway(Long memberId, Long walkwayId) {
        walkwayValidator.validateWalkwayExists(walkwayId);
        Walkway walkway = walkwayReader.getWalkway(walkwayId);
        walkwayValidator.validateWalkwayAccess(walkway, memberId);
        return walkway;
    }

    @Transactional
    public void updateWalkway(UpdateWalkway updateWalkway, Long memberId) {
        // 산책로 등록자 검증
        walkwayValidator.isOwnerOfWalkway(updateWalkway.walkwayId(), memberId);
        walkwayWriter.updateWalkway(updateWalkway);
    }

    public PagingResponse<Walkway> searchWalkway(String sortType, SearchWalkwayQuery searchWalkwayQuery) {
        if (searchWalkwayQuery.lastWalkwayId() != null) {
            walkwayValidator.validateWalkwayExists(searchWalkwayQuery.lastWalkwayId());
        }

        WalkwaySort sort = WalkwaySort.typeOf(sortType);
        List<Walkway> walkways = walkwayReader.searchWalkway(searchWalkwayQuery, sort);
        return PagingResponse.from(walkways, searchWalkwayQuery.size());
    }

    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return walkwayReader.existsLikedWalkway(memberId, walkwayId);
    }

    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return walkwayReader.existsLikedWalkways(memberId, walkwayIds);
    }

    @Transactional
    public void createLikedWalkway(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayReader.getWalkway(walkwayId);
        walkwayValidator.validateWalkwayAccess(walkway, memberId);
        boolean isLiked = walkwayReader.existsLikedWalkway(memberId, walkwayId);
        if (!isLiked) {
            walkwayWriter.saveLikedWalkway(memberId, walkwayId);
        }
    }

    @Transactional
    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayReader.getWalkway(walkwayId);
        walkwayValidator.validateWalkwayAccess(walkway, memberId);
        boolean isLiked = walkwayReader.existsLikedWalkway(memberId, walkwayId);
        if (isLiked) {
            walkwayWriter.deleteLikedWalkway(memberId, walkwayId);
        }
    }

    @Transactional(readOnly = true)
    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        if(walkwayId != null){
            // lastId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayReader.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkwayValidator.isOwnerOfWalkway(walkwayId, memberId);
            lastCreatedAt = walkway.createdAt();
        }
        List<Walkway> walkways = walkwayReader.getUserLikedWalkway(memberId, size+1, lastCreatedAt);
        boolean hasNext = walkways.size() > size;
        if(hasNext){
            walkways.remove(walkways.size()-1);
        }
        return walkways;
    }

    @Transactional(readOnly = true)
    public List<Walkway> getUserWalkway(Long memberId, Integer size, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        if(walkwayId != null){
            // lastId 검증
            // 1. 존재하는 산책로인지
            Walkway walkway = walkwayReader.getWalkway(walkwayId);
            // 2. 내가 작성한 산책로인지
            walkwayValidator.isOwnerOfWalkway(walkwayId, memberId);
            lastCreatedAt = walkway.createdAt();
        }
        List<Walkway> walkways = walkwayReader.getUserWalkway(memberId, size+1, lastCreatedAt);
        boolean hasNext = walkways.size() > size;
        if(hasNext){
            walkways.remove(walkways.size()-1);
        }
        return walkways;
    }

    @Transactional
    public Long createWalkwayHistory(CreateWalkwayHistory createWalkwayHistory) {
        walkwayValidator.validateWalkwayExists(createWalkwayHistory.walkwayId());
        return walkwayWriter.saveWalkwayHistory(createWalkwayHistory);
    }

    public List<WalkwayHistory> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size, Long lastWalkwayHistoryId) {
        walkwayValidator.validateWalkwayPrivate(walkwayId);
        LocalDateTime lastCreatedAt = null;
        if (lastWalkwayHistoryId != null) {
            WalkwayHistory walkwayHistory = walkwayReader.getWalkwayHistory(lastWalkwayHistoryId);
            lastCreatedAt = walkwayHistory.createdAt();
        }

        return walkwayReader.getCanReviewWalkwayHistory(walkwayId, memberId, size, lastCreatedAt);
    }

    public List<WalkwayHistory> getUserCanReviewWalkwayHistory(Long memberId, Long lastWalkwayHistoryId, int size) {
        LocalDateTime lastCreatedAt = null;
        if (lastWalkwayHistoryId != null) {
            WalkwayHistory walkwayHistory = walkwayReader.getWalkwayHistory(lastWalkwayHistoryId);
            lastCreatedAt = walkwayHistory.createdAt();
        }

        return walkwayReader.getUserCanReviewWalkwayHistory(memberId, size, lastCreatedAt);
    }

    public boolean isCanReview(Long walkwayHistoryId) {
        WalkwayHistory walkwayHistory = walkwayReader.getWalkwayHistory(walkwayHistoryId);
        return walkwayHistory.distance() >= walkwayHistory.walkway().courseInfo().distance();
    }
}
