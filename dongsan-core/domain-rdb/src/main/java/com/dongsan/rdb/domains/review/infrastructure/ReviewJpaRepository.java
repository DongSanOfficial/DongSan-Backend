package com.dongsan.rdb.domains.review.infrastructure;

import com.dongsan.rdb.domains.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    boolean existsByIdAndMemberId(Long reviewId, Long memberId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);
}
