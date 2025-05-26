package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayRatingJpaRepository extends JpaRepository<MetaWalkwayRating, Long> {
    Optional<MetaWalkwayRating> findByWalkwayId(Long walkwayId);
}
