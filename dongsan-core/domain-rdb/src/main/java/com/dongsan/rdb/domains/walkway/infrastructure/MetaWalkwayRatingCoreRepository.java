package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayRating;
import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayRatingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MetaWalkwayRatingCoreRepository implements MetaWalkwayRatingRepository {
    private final MetaWalkwayRatingJpaRepository metaWalkwayRatingJpaRepository;

    public MetaWalkwayRatingCoreRepository(MetaWalkwayRatingJpaRepository metaWalkwayRatingJpaRepository) {
        this.metaWalkwayRatingJpaRepository = metaWalkwayRatingJpaRepository;
    }

    @Override
    public Optional<MetaWalkwayRating> findByWalkwayId(Long walkwayId) {
        return metaWalkwayRatingJpaRepository.findByWalkwayId(walkwayId);
    }

    @Override
    public void save(MetaWalkwayRating metaWalkwayRating) {
        metaWalkwayRatingJpaRepository.save(metaWalkwayRating);
    }
}
