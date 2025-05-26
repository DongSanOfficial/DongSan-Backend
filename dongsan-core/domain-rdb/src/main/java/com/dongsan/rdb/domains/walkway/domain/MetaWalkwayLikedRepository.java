package com.dongsan.rdb.domains.walkway.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayLikedRepository {
    Optional<MetaWalkwayLiked> findByWalkwayId(Long walkwayId);

    void save(MetaWalkwayLiked metaWalkwayLiked);
}
