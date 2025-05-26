package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayLiked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayLikedJpaRepository extends JpaRepository<MetaWalkwayLiked, Long> {
    Optional<MetaWalkwayLiked> findByWalkwayId(Long walkwayId);
}
