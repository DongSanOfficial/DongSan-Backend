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

    public void validateReviewWritable(Long walkwayId) {
        boolean isReviewed = walkwayLogRepository.isReviewed(walkwayId);
        if (isReviewed) {
            throw new CoreException(CoreErrorCode.ALREADY_REVIEWED);
        }
    }


}
