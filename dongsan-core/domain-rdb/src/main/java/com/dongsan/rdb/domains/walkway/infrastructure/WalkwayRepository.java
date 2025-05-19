package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.UpdateWalkway;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WalkwayRepository {
    // 산책로
    Long saveWalkway(CreateWalkway createWalkway);

    Optional<Walkway> getWalkway(Long walkwayId);

    void updateWalkway(UpdateWalkway updateWalkway);

    boolean existsWalkway(Long walkwayId);

    boolean existsWalkway(Long walkwayId, Long memberId);

    List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery);

    List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery);

    List<Walkway> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);

    List<Walkway> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt);

    void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId);

    void deleteWalkway(Long walkwayId);

    List<Walkway> getWalkwaysLatest(Integer size, Long lastWalkwayId, Long memberId);

    List<Walkway> getWalkwaysLiked(Integer size, Long lastWalkwayId, Long memberId);

    List<Walkway> getWalkwaysRating(Integer size, Long lastWalkwayId, Long memberId);

    // 좋아요
    boolean existsLikedWalkway(Long memberId, Long walkwayId);

    Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds);

    Long saveLikedWalkway(Long memberId, Long walkwayId);

    void deleteLikedWalkway(Long memberId, Long walkwayId);


}
