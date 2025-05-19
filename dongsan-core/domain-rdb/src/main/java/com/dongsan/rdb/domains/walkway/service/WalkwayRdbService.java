package com.dongsan.rdb.domains.walkway.service;

import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.infrastructure.WalkwayRepository;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.springframework.stereotype.Service;

@Service
public class WalkwayRdbService {
    private final WalkwayRepository walkwayRepository;

    public WalkwayRdbService(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    public Walkway getWalkway(Long walkwayId) {
        return walkwayRepository.getWalkway(walkwayId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_NOT_FOUND));
    }
}
