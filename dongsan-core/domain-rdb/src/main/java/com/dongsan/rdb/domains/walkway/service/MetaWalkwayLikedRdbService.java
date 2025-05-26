package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayLiked;
import com.dongsan.rdb.domains.walkway.domain.MetaWalkwayLikedRepository;
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

    @Async
    @Transactional
    public void increaseLikeCount(Long walkwayId) {
        MetaWalkwayLiked metaLiked = findByWalkwayId(walkwayId)
                .orElseGet(() -> new MetaWalkwayLiked(walkwayId));
        metaLiked.increaseLikeCount();
        metaWalkwayLikedRepository.save(metaLiked);
    }

    @Async
    @Transactional
    public void decreaseLikeCount(Long walkwayId) {
        findByWalkwayId(walkwayId).ifPresent(
                MetaWalkwayLiked::decreaseLikeCount
        );
    }
}
