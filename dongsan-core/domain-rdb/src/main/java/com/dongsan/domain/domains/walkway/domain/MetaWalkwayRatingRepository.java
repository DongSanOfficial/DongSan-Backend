package com.dongsan.domain.domains.walkway.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayRatingRepository {
    Optional<MetaWalkwayRating> findByWalkwayId(Long walkwayId);

    void save(MetaWalkwayRating metaWalkwayRating);

    Optional<MetaWalkwayRating> findByWalkwayIdForUpdate(Long walkwayId);
}
