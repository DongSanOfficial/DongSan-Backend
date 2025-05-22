package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class WalkwayCoreRepository implements WalkwayRepository {
    private final MemberJpaRepository memberJpaRepository;
    private final LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;

    @Autowired
    public WalkwayCoreRepository(MemberJpaRepository memberJpaRepository,
                                 LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository,
                                 WalkwayJpaRepository walkwayJpaRepository,
                                 WalkwayQueryDSLRepository walkwayQueryDSLRepository) {
        this.memberJpaRepository = memberJpaRepository;
        this.likedWalkwayQueryDSLRepository = likedWalkwayQueryDSLRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.walkwayQueryDSLRepository = walkwayQueryDSLRepository;
    }

    @Override
    public Long save(Walkway walkway) {
        return walkwayJpaRepository.save(walkway)
                .getId();
    }

    @Override
    public Optional<Walkway> getWalkway(Long walkwayId) {
        return walkwayJpaRepository.findById(walkwayId);
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
    public List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery) {
        return walkwayQueryDSLRepository.searchWalkwaysLiked(searchWalkwayQuery);
    }

    @Override
    public List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery) {
        return walkwayQueryDSLRepository.searchWalkwaysRating(searchWalkwayQuery);
    }

    @Override
    public List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return walkwayQueryDSLRepository.getUserLikedWalkway(memberId, size,
                lastCreatedAt);
    }

    @Override
    public List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt);
    }

    @Override
    public List<Walkway> getWalkwaysLatest(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayQueryDSLRepository.getWalkwaysLatest(size, lastWalkwayId, memberId);
    }

    @Override
    public List<Walkway> getWalkwaysLiked(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayQueryDSLRepository.getWalkwaysLiked(size, lastWalkwayId, memberId);
    }

    @Override
    public List<Walkway> getWalkwaysRating(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayQueryDSLRepository.getWalkwaysRating(size, lastWalkwayId, memberId);
    }

    @Override
    public void delete(Walkway walkway) {
        walkwayJpaRepository.delete(walkway);
    }


    //    @Override
//    public void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId) {
//        Walkway walkway = walkwayJpaRepository.getReferenceById(walkwayId);
//        walkway.updateRatingAndReviewCount(rating, reviewCount);
//        walkwayJpaRepository.save(walkway);
//    }

//    @Override
//    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
//        return likedWalkwayQueryDSLRepository.existsLikedWalkways(memberId, walkwayIds);
//    }
}
