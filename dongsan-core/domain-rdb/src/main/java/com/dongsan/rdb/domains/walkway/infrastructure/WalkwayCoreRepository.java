package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.bookmark.infrastructure.MarkedWalkwayJpaRepository;
import com.dongsan.rdb.domains.member.Member;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.review.infrastructure.ReviewJpaRepository;
import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.UpdateWalkway;
import com.dongsan.rdb.domains.walkway.domain.LikedWalkway;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLogJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class WalkwayCoreRepository implements WalkwayRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final LikedWalkwayJpaRepository likedWalkwayJpaRepository;
    private final LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;
    private final WalkwayLogJpaRepository walkwayLogJpaRepository;
    private final WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository;
    private final ReviewJpaRepository reviewJpaRepository;
    private final MarkedWalkwayJpaRepository markedWalkwayJpaRepository;

    @Autowired
    public WalkwayCoreRepository(MemberJpaRepository memberJpaRepository,
                                 LikedWalkwayJpaRepository likedWalkwayJpaRepository,
                                 LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository,
                                 WalkwayJpaRepository walkwayJpaRepository,
                                 WalkwayQueryDSLRepository walkwayQueryDSLRepository,
                                 WalkwayLogJpaRepository walkwayLogJpaRepository,
                                 WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository,
                                 ReviewJpaRepository reviewJpaRepository,
                                 MarkedWalkwayJpaRepository markedWalkwayJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
        this.likedWalkwayJpaRepository = likedWalkwayJpaRepository;
        this.likedWalkwayQueryDSLRepository = likedWalkwayQueryDSLRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.walkwayQueryDSLRepository = walkwayQueryDSLRepository;
        this.walkwayLogJpaRepository = walkwayLogJpaRepository;
        this.walkwayHistoryQueryDSLRepository = walkwayHistoryQueryDSLRepository;
        this.reviewJpaRepository = reviewJpaRepository;
        this.markedWalkwayJpaRepository = markedWalkwayJpaRepository;
    }

    // 생성용 dto만들기
    @Override
    public Long saveWalkway(CreateWalkway createWalkway) {
        Member member = memberJpaRepository.getReferenceById(createWalkway.memberId());
        Walkway walkway = new Walkway(createWalkway, member);
        return walkwayJpaRepository.save(walkway)
                .getId();
    }

    @Override
    public Optional<Walkway> getWalkway(Long walkwayId) {
        return walkwayJpaRepository.findById(walkwayId)
                .map(Walkway::toWalkway);
    }

    @Override
    public void updateWalkway(UpdateWalkway updateWalkway) {
        Walkway walkway = walkwayJpaRepository.getReferenceById(updateWalkway.walkwayId());
        walkway.updateWalkway(updateWalkway.name(), updateWalkway.memo(), updateWalkway.exposeLevel(),
                updateWalkway.hashtags());
        walkwayJpaRepository.save(walkway);
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
    public void deleteWalkway(Long walkwayId) {
        walkwayJpaRepository.deleteById(walkwayId);
        reviewJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
        walkwayLogJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
        likedWalkwayJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
        markedWalkwayJpaRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    @Override
    public boolean existsLikedWalkway(Long memberId, Long walkwayId) {
        return likedWalkwayJpaRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    @Override
    public List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery) {
        List<Walkway> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysLiked(searchWalkwayQuery);
        return walkwayEntities.stream()
                .map(Walkway::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery) {
        List<Walkway> walkwayEntities = walkwayQueryDSLRepository.searchWalkwaysRating(searchWalkwayQuery);
        return walkwayEntities.stream()
                .map(Walkway::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<Walkway> walkwayEntities = walkwayQueryDSLRepository.getUserLikedWalkway(memberId, size,
                lastCreatedAt);
        return walkwayEntities.stream()
                .map(Walkway::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        List<Walkway> walkwayEntities = walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt);
        return walkwayEntities.stream()
                .map(Walkway::toWalkway)
                .toList();
    }

    @Override
    public void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId) {
        Walkway walkway = walkwayJpaRepository.getReferenceById(walkwayId);
        walkway.updateRatingAndReviewCount(rating, reviewCount);
        walkwayJpaRepository.save(walkway);
    }

    @Override
    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
        return likedWalkwayQueryDSLRepository.existsLikedWalkways(memberId, walkwayIds);
    }

    @Override
    public Long saveLikedWalkway(Long memberId, Long walkwayId) {
        Member member = memberJpaRepository.getReferenceById(memberId);
        Walkway walkway = walkwayJpaRepository.getReferenceById(walkwayId);

        walkway.increaseLikeCount();
        walkwayJpaRepository.save(walkway);

        LikedWalkway likedWalkway = likedWalkwayJpaRepository.save(
                new LikedWalkway(member, walkway));
        return likedWalkway.getId();
    }

    @Override
    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayJpaRepository.getReferenceById(walkwayId);
        walkway.decreaseLikeCount();
        walkwayJpaRepository.save(walkway);
        likedWalkwayJpaRepository.deleteByMemberIdAndWalkwayId(memberId, walkwayId);
    }

    @Override
    public List<Walkway> getWalkwaysLatest(Integer size, Long lastWalkwayId, Long memberId) {
        List<Walkway> walkwayEntities =
                walkwayQueryDSLRepository.getWalkwaysLatest(size, lastWalkwayId, memberId);

        return walkwayEntities.stream()
                .map(Walkway::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getWalkwaysLiked(Integer size, Long lastWalkwayId, Long memberId) {
        List<Walkway> walkwayEntities =
                walkwayQueryDSLRepository.getWalkwaysLiked(size, lastWalkwayId, memberId);

        return walkwayEntities.stream()
                .map(Walkway::toWalkway)
                .toList();
    }

    @Override
    public List<Walkway> getWalkwaysRating(Integer size, Long lastWalkwayId, Long memberId) {
        List<Walkway> walkwayEntities =
                walkwayQueryDSLRepository.getWalkwaysRating(size, lastWalkwayId, memberId);

        return walkwayEntities.stream()
                .map(Walkway::toWalkway)
                .toList();
    }
}
