package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayLiked;
import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayLikedRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MetaWalkwayLikedCoreRepository implements MetaWalkwayLikedRepository {
    private final MetaWalkwayLikedJpaRepository metaWalkwayLikedJpaRepository;

    public MetaWalkwayLikedCoreRepository(MetaWalkwayLikedJpaRepository metaWalkwayLikedJpaRepository) {
        this.metaWalkwayLikedJpaRepository = metaWalkwayLikedJpaRepository;
    }

    @Override
    public Optional<MetaWalkwayLiked> findByWalkwayId(Long walkwayId) {
        return metaWalkwayLikedJpaRepository.findByWalkwayId(walkwayId);
    }

    @Override
    public void save(MetaWalkwayLiked metaWalkwayLiked) {
        metaWalkwayLikedJpaRepository.save(metaWalkwayLiked);
    }
}
