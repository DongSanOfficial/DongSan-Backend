package com.dongsan.rdb.domains.walkway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayJpaRepository extends JpaRepository<WalkwayEntity, Long> {
    boolean existsByIdAndMemberEntityId(Long walkwayId, Long memberId);
}
