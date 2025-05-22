package com.dongsan.api.domains.walkway;

import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.service.LikedWalkwayRdbService;
import com.dongsan.rdb.domains.walkway.service.WalkwayRdbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikedWalkwayFacade {
    private final LikedWalkwayRdbService likedWalkwayRdbService;
    private final WalkwayRdbService walkwayRdbService;

    public LikedWalkwayFacade(LikedWalkwayRdbService likedWalkwayRdbService, WalkwayRdbService walkwayRdbService) {
        this.likedWalkwayRdbService = likedWalkwayRdbService;
        this.walkwayRdbService = walkwayRdbService;
    }

    public void createLikedWalkway(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        likedWalkwayRdbService.save(memberId, walkwayId);
    }

    public void deleteLikedWalkway(Long memberId, Long walkwayId) {
        Walkway walkway = walkwayRdbService.getWalkway(walkwayId);
        walkway.validateAccess(memberId);
        likedWalkwayRdbService.delete(memberId, walkwayId);
    }
}
