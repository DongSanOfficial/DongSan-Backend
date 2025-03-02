package com.dongsan.core.domains.walkway;

import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.core.domains.walkway.WalkwayRepository;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WalkwayHistoryValidator {
    private final WalkwayRepository walkwayRepository;

    @Autowired
    public WalkwayHistoryValidator(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    // 이용 기록 검증
    public void validateWalkwayAndMember(WalkwayHistory walkwayHistory, Long walkwayId, Long memberId) {
        if (!walkwayHistory.walkway().walkwayId().equals(walkwayId) || !walkwayHistory.memberId().equals(memberId)) {
            throw new CoreException(CoreErrorCode.INVALID_ACCESS);
        }
    }

    public void validateDistance(WalkwayHistory walkwayHistory) {
        if (walkwayHistory.walkway().courseInfo().distance() * 2/3 > walkwayHistory.distance()) {
            throw new CoreException(CoreErrorCode.NOT_ENOUGH_DISTANCE);
        }
    }

    public void validateIsReviewed(WalkwayHistory walkwayHistory) {
        if(walkwayHistory.isReviewed()) {
            throw new CoreException(CoreErrorCode.ALREADY_REVIEWED);
        }
    }
}



