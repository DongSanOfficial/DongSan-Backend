package com.dongsan.domain.domains.walkway.infrastructure;

import com.dongsan.domain.domains.walkway.domain.LikedWalkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikedWalkwayJpaRepository extends JpaRepository<LikedWalkway, Long> {
    Optional<LikedWalkway> findByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    int countByWalkwayId(Long walkwayId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);
}
