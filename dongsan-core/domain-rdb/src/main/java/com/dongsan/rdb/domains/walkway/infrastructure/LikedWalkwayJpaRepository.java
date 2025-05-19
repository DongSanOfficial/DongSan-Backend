package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.LikedWalkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedWalkwayJpaRepository extends JpaRepository<LikedWalkway, Long> {

    void deleteByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    Boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);
}
