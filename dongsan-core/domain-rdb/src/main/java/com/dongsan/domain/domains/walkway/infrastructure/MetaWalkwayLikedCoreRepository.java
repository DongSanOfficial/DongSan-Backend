package com.dongsan.domain.domains.walkway.infrastructure;

import com.dongsan.domain.domains.walkway.domain.MetaWalkwayLiked;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayLikedRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MetaWalkwayLikedCoreRepository implements MetaWalkwayLikedRepository {
    private final MetaWalkwayLikedJpaRepository metaWalkwayLikedJpaRepository;

    public MetaWalkwayLikedCoreRepository(MetaWalkwayLikedJpaRepository metaWalkwayLikedJpaRepository) {
        this.metaWalkwayLikedJpaRepository = metaWalkwayLikedJpaRepository;
    }

    @Override
    public void save(MetaWalkwayLiked metaWalkwayLiked) {
        metaWalkwayLikedJpaRepository.save(metaWalkwayLiked);
    }

    @Override
    public Optional<MetaWalkwayLiked> findByWalkwayIdForUpdate(Long walkwayId) {
        return metaWalkwayLikedJpaRepository.findByWalkwayIdForUpdate(walkwayId);
    }
}
