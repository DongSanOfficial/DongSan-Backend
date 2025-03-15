package com.dongsan.rdb.domains.walkway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedWalkwayJpaRepository extends JpaRepository<LikedWalkwayEntity, Long> {

    void deleteByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    Boolean existsByMemberIdAndWalkwayId(Long memberId, Long walkwayId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);
}
