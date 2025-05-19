package com.dongsan.rdb.domains.walkwayLog;

import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.springframework.stereotype.Service;

@Service
public class WalkwayLogRdbService {
    private final WalkwayLogRepository walkwayLogRepository;

    public WalkwayLogRdbService(WalkwayLogRepository walkwayLogRepository) {
        this.walkwayLogRepository = walkwayLogRepository;
    }

    public WalkwayLog getWalkwayLog(Long walkwayLogId) {
        return walkwayLogRepository.findById(walkwayLogId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.WALKWAY_LOG_NOT_FOUND));
    }

    // 이용 기록 검증
    public void validateWalkwayAndMember(WalkwayHistory walkwayHistory, Long walkwayId, Long memberId) {
        if (!walkwayHistory.walkway()
                .walkwayId()
                .equals(walkwayId) || !walkwayHistory.memberId()
                .equals(memberId)) {
            throw new CoreException(CoreErrorCode.INVALID_ACCESS);
        }
    }

    public void validateDistance(WalkwayHistory walkwayHistory) {
        if (walkwayHistory.walkway()
                .courseInfo()
                .distance() * 2 / 3 > walkwayHistory.distance()) {
            throw new CoreException(CoreErrorCode.NOT_ENOUGH_DISTANCE);
        }
    }

    public void validateReviewWritable(Long walkwayLogId) {
        boolean isReviewed = walkwayLogRepository.isReviewed(walkwayLogId);
        if (isReviewed) {
            throw new CoreException(CoreErrorCode.ALREADY_REVIEWED);
        }
    }
}
