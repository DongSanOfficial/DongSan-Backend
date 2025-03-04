package com.dongsan.rdb.domains.walkway;

import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.CreateWalkwayHistory;
import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.UpdateWalkway;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WalkwayCoreJpaRepository implements WalkwayRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final LikedWalkwayJpaRepository likedWalkwayJpaRepository;
    private final LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;
    private final WalkwayHistoryJpaRepository walkwayHistoryJpaRepository;
    private final WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository;

    @Autowired
    public WalkwayCoreJpaRepository(MemberJpaRepository memberJpaRepository,
                                    LikedWalkwayJpaRepository likedWalkwayJpaRepository,
                                    LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository,
                                    WalkwayJpaRepository walkwayJpaRepository,
                                    WalkwayQueryDSLRepository walkwayQueryDSLRepository,
                                    WalkwayHistoryJpaRepository walkwayHistoryJpaRepository,
                                    WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository) {
        this.memberJpaRepository = memberJpaRepository;
        this.likedWalkwayJpaRepository = likedWalkwayJpaRepository;
        this.likedWalkwayQueryDSLRepository = likedWalkwayQueryDSLRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.walkwayQueryDSLRepository = walkwayQueryDSLRepository;
        this.walkwayHistoryJpaRepository = walkwayHistoryJpaRepository;
        this.walkwayHistoryQueryDSLRepository = walkwayHistoryQueryDSLRepository;
    }

    // 생성용 dto만들기
    @Override
    public Long saveWalkway(CreateWalkway createWalkway) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(createWalkway.memberId());
        WalkwayEntity walkwayEntity = new WalkwayEntity(createWalkway, memberEntity);
        return walkwayJpaRepository.save(walkwayEntity).getId();
    }

    @Override
    public Optional<Walkway> getWalkway(Long walkwayId) {
        return walkwayJpaRepository.findById(walkwayId)
                .map(WalkwayEntity::toWalkway);
    }

    @Override
    public void updateWalkway(UpdateWalkway updateWalkway) {
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(updateWalkway.walkwayId());
        walkwayEntity.updateWalkway(updateWalkway.name(), updateWalkway.memo(), updateWalkway.exposeLevel(), updateWalkway.hashtags());
        walkwayJpaRepository.save(walkwayEntity);
    }

    @Override
    public boolean existsWalkway(Long walkwayId) {
        return walkwayJpaRepository.existsById(walkwayId);
    }

    @Override
    public boolean existsWalkway(Long walkwayId, Long memberId) {
        return walkwayJpaRepository.existsByIdAndMemberId(walkwayId, memberId);
    }

    @Override
    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return likedWalkwayJpaRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    @Override
    public List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysLiked(searchWalkwayQuery);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysRating(searchWalkwayQuery);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, lastCreatedAt);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<WalkwayEntity> walkwayEntities = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt);
        return walkwayEntities.stream()
                .map(WalkwayEntity::toWalkway)
                .toList();
    }

    @Override
    public void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId) {
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(walkwayId);
        walkwayEntity.updateRatingAndReviewCount(rating, reviewCount);
        walkwayJpaRepository.save(walkwayEntity);
    }

    @Override
    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return likedWalkwayQueryDSLRepository.existsLikedWalkways(memberId, walkwayIds);
    }

    @Override
    public Long saveLikedWalkway(Long memberId, Long walkwayId) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(memberId);
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(walkwayId);

        walkwayEntity.increaseLikeCount();
        walkwayJpaRepository.save(walkwayEntity);

        LikedWalkwayEntity likedWalkwayEntity = likedWalkwayJpaRepository.save(new LikedWalkwayEntity(memberEntity, walkwayEntity));
        return likedWalkwayEntity.getId();
    }

    @Override
    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(walkwayId);
        walkwayEntity.decreaseLikeCount();
        walkwayJpaRepository.save(walkwayEntity);
        likedWalkwayJpaRepository.deleteByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    @Override
    public Long saveWalkwayHistory(CreateWalkwayHistory createWalkwayHistory) {
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(createWalkwayHistory.memberId());
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(createWalkwayHistory.walkwayId());

        WalkwayHistoryEntity walkwayHistoryEntity
                = new WalkwayHistoryEntity(memberEntity, walkwayEntity, createWalkwayHistory.distance(), createWalkwayHistory.time());
        walkwayHistoryJpaRepository.save(walkwayHistoryEntity);
        return walkwayHistoryEntity.getId();
    }

    @Override
    public List<WalkwayHistory> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size, LocalDateTime lastCreatedAt) {
        List<WalkwayHistoryEntity> walkwayHistoryEntities = walkwayHistoryQueryDSLRepository.getCanReviewWalkwayHistories(walkwayId, memberId, size, lastCreatedAt);
        return walkwayHistoryEntities.stream()
                .map(WalkwayHistoryEntity::toWalkwayHistory)
                .toList();
    }

    @Override
    public List<WalkwayHistory> getUserCanReviewWalkwayHistory(Long memberId, int size, LocalDateTime lastCreatedAt) {
        List<WalkwayHistoryEntity> walkwayHistoryEntities = walkwayHistoryQueryDSLRepository.getUserCanReviewWalkwayHistories(memberId, size, lastCreatedAt);
        return walkwayHistoryEntities.stream()
                .map(WalkwayHistoryEntity::toWalkwayHistory)
                .toList();
    }

    @Override
    public Optional<WalkwayHistory> getWalkwayHistory(Long walkwayHistoryId) {
        return walkwayHistoryJpaRepository.findById(walkwayHistoryId)
                .map(WalkwayHistoryEntity::toWalkwayHistory);
    }

    @Override
    public void updateWalkwayHistoryIsReviewed(Long walkwayHistoryId, boolean isReviewed) {
        WalkwayHistoryEntity walkwayHistory = walkwayHistoryJpaRepository.getReferenceById(walkwayHistoryId);
        walkwayHistory.updateIsReviewed(isReviewed);
        walkwayHistoryJpaRepository.save(walkwayHistory);
    }
}
