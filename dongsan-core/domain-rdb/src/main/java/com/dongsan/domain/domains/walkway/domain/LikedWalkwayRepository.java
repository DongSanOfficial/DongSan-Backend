package com.dongsan.domain.domains.walkway.domain;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LikedWalkwayRepository {
    Optional<LikedWalkway> findByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    void save(LikedWalkway likedWalkway);

    void delete(LikedWalkway likedWalkway);

    void deleteAllInBatchByWalkwayId(Long walkwayId);

    int countByWalkwayId(Long walkwayId);

    Map<Long, Long> countByWalkwayIds(List<Long> walkwayIds);

    Set<Long> getLikedWalkways(Long memberId, List<Long> walkwayIds);
}
