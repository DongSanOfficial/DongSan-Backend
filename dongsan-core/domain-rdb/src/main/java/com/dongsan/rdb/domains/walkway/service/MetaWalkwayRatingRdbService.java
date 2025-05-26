package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayRating;
import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayRatingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MetaWalkwayRatingRdbService {
    private final MetaWalkwayRatingRepository metaWalkwayRatingRepository;

    public MetaWalkwayRatingRdbService(MetaWalkwayRatingRepository metaWalkwayRatingRepository) {
        this.metaWalkwayRatingRepository = metaWalkwayRatingRepository;
    }

    private Optional<MetaWalkwayRating> findByWalkwayId(Long walkwayId) {
        return metaWalkwayRatingRepository.findByWalkwayId(walkwayId);
    }

    @Async
    @Transactional
    public void addRating(Long walkwayId, int rating) {
        MetaWalkwayRating metaRating = findByWalkwayId(walkwayId)
                .orElseGet(() -> new MetaWalkwayRating(walkwayId));
        metaRating.addRating(rating);
        metaWalkwayRatingRepository.save(metaRating);
    }

    @Async
    @Transactional
    public void removeRating(Long walkwayId, int rating) {
        findByWalkwayId(walkwayId).ifPresent(
                metaRating -> {
                    metaRating.removeRating(rating);
                }
        );
    }
}
