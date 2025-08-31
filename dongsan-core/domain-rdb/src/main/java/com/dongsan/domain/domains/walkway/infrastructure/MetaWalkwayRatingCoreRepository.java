package com.dongsan.domain.domains.walkway.infrastructure;

import com.dongsan.domain.domains.walkway.domain.MetaWalkwayRating;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayRatingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MetaWalkwayRatingCoreRepository implements MetaWalkwayRatingRepository {
    private final MetaWalkwayRatingJpaRepository metaWalkwayRatingJpaRepository;

    public MetaWalkwayRatingCoreRepository(MetaWalkwayRatingJpaRepository metaWalkwayRatingJpaRepository) {
        this.metaWalkwayRatingJpaRepository = metaWalkwayRatingJpaRepository;
    }

    @Override
    public void save(MetaWalkwayRating metaWalkwayRating) {
        metaWalkwayRatingJpaRepository.save(metaWalkwayRating);
    }

    @Override
    public Optional<MetaWalkwayRating> findByWalkwayIdForUpdate(Long walkwayId) {
        return metaWalkwayRatingJpaRepository.findByWalkwayIdForUpdate(walkwayId);
    }
}
