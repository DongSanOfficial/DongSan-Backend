package com.dongsan.domain.domains.walkway.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayLikedRepository {

    void save(MetaWalkwayLiked metaWalkwayLiked);

    Optional<MetaWalkwayLiked> findByWalkwayIdForUpdate(Long walkwayId);
}
