package com.dongsan.rdb.domains.review.infrastructure;

import com.dongsan.rdb.domains.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    boolean existsByWalkwayLogId(Long walkwayLogId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Review r where r.walkwayLogId in " +
            "(select wl.id from WalkwayLog wl where wl.walkwayId = :walkwayId)")
    void deleteAllInBatchByWalkwayId(@Param("walkwayId") Long walkwayId);
}
