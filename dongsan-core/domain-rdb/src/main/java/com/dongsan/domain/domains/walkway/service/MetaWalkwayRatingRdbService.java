package com.dongsan.domain.domains.walkway.service;

import com.dongsan.domain.domains.walkway.domain.MetaWalkwayRating;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayRatingRepository;
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

    private Optional<MetaWalkwayRating> findByWalkwayIdForUpdate(Long walkwayId) {
        return metaWalkwayRatingRepository.findByWalkwayIdForUpdate(walkwayId);
    }

    @Async
    @Transactional
    public void addRating(Long walkwayId, int rating) {
        MetaWalkwayRating metaRating = findByWalkwayIdForUpdate(walkwayId)
                .orElseGet(() -> new MetaWalkwayRating(walkwayId));
        metaRating.addRating(rating);
        metaWalkwayRatingRepository.save(metaRating);
    }

    public void saveByWalkwayId(Long walkwayId) {
        MetaWalkwayRating metaRating = new MetaWalkwayRating(walkwayId);
        metaWalkwayRatingRepository.save(metaRating);
    }
}
