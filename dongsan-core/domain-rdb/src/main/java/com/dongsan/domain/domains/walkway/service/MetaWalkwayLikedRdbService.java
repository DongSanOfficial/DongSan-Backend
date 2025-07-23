package com.dongsan.domain.domains.walkway.service;

import com.dongsan.domain.domains.walkway.domain.MetaWalkwayLiked;
import com.dongsan.domain.domains.walkway.domain.MetaWalkwayLikedRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MetaWalkwayLikedRdbService {
    private final MetaWalkwayLikedRepository metaWalkwayLikedRepository;

    public MetaWalkwayLikedRdbService(MetaWalkwayLikedRepository metaWalkwayLikedRepository) {
        this.metaWalkwayLikedRepository = metaWalkwayLikedRepository;
    }

    private Optional<MetaWalkwayLiked> findByWalkwayId(Long walkwayId) {
        return metaWalkwayLikedRepository.findByWalkwayId(walkwayId);
    }

    private Optional<MetaWalkwayLiked> findByWalkwayIdForUpdate(Long walkwayId) {
        return metaWalkwayLikedRepository.findByWalkwayIdForUpdate(walkwayId);
    }

    @Async
    @Transactional
    public void increaseLikeCount(Long walkwayId) {
        MetaWalkwayLiked metaLiked = findByWalkwayIdForUpdate(walkwayId)
                .orElseGet(() -> new MetaWalkwayLiked(walkwayId));
        metaLiked.increaseLikeCount();
        metaWalkwayLikedRepository.save(metaLiked);
    }

    @Async
    @Transactional
    public void decreaseLikeCount(Long walkwayId) {
        findByWalkwayIdForUpdate(walkwayId).ifPresent(
                MetaWalkwayLiked::decreaseLikeCount
        );
    }

    public void saveByWalkwayId(Long walkwayId) {
        MetaWalkwayLiked metaLiked = new MetaWalkwayLiked(walkwayId);
        metaWalkwayLikedRepository.save(metaLiked);
    }
}
