package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.LikedWalkway;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikedWalkwayRepository {
    Optional<LikedWalkway> findByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    void save(LikedWalkway likedWalkway);

    void delete(LikedWalkway likedWalkway);
}
