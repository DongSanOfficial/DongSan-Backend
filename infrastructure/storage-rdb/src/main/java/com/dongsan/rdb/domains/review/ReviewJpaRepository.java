package com.dongsan.rdb.domains.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long>{
    boolean existsByIdAndMemberId(Long reviewId, Long memberId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);
}
