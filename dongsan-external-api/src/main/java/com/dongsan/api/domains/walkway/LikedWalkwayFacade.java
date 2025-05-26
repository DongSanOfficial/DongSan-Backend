package com.dongsan.api.domains.walkway;

import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.domain.domains.walkway.service.MetaWalkwayLikedRdbService;
import com.dongsan.domain.domains.walkway.service.WalkwayRdbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikedWalkwayFacade {
    private final LikedWalkwayRdbService likedWalkwayRdbService;
    private final WalkwayRdbService walkwayRdbService;
    private final MetaWalkwayLikedRdbService metaWalkwayLikedRdbService;

    public LikedWalkwayFacade(LikedWalkwayRdbService likedWalkwayRdbService, WalkwayRdbService walkwayRdbService, MetaWalkwayLikedRdbService metaWalkwayLikedRdbService) {
        this.likedWalkwayRdbService = likedWalkwayRdbService;
        this.walkwayRdbService = walkwayRdbService;
        this.metaWalkwayLikedRdbService = metaWalkwayLikedRdbService;
    }

    @Transactional
    public void createLikedWalkway(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        likedWalkwayRdbService.save(memberId, walkwayId);
        metaWalkwayLikedRdbService.increaseLikeCount(walkwayId);
    }

    @Transactional
    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        likedWalkwayRdbService.delete(memberId, walkwayId);
        metaWalkwayLikedRdbService.decreaseLikeCount(walkwayId);
    }
}
