package com.dongsan.domain.domains.walkway.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayRatingRepository {

    void save(MetaWalkwayRating metaWalkwayRating);

    Optional<MetaWalkwayRating> findByWalkwayIdForUpdate(Long walkwayId);
}
