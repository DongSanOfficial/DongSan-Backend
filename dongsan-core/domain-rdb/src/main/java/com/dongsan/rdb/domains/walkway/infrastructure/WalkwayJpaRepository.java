package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.Walkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayJpaRepository extends JpaRepository<Walkway, Long> {
    boolean existsByIdAndMemberId(Long walkwayId, Long memberId);
}
