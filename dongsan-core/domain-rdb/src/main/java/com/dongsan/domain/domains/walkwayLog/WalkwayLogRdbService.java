package com.dongsan.domain.domains.walkwayLog;

import com.dongsan.domain.domains.common.BaseEntity;
import com.dongsan.domain.domains.crew.domain.CrewMemberStatistic;
import com.dongsan.domain.domains.crew.domain.CrewWeeklyStatistic;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

    public LocalDateTime getWalkwayLogCreatedAt(Long walkwayLogId) {
        if (walkwayLogId == null) {
            return null;
        }

        Optional<WalkwayLog> optionalWalkwayLog = walkwayLogRepository.findById(walkwayLogId);
        return optionalWalkwayLog.map(BaseEntity::getCreatedAt).orElse(null);
    }

    public WalkwayLog save(Long walkwayId, Long memberId, Double distance, Integer time) {
        WalkwayLog walkwayLog = new WalkwayLog(memberId, walkwayId, time, distance);
        walkwayLogRepository.save(walkwayLog);
        return walkwayLog;
    }

    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        walkwayLogRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    public CursorResponse<WalkwayLog> getUserWalkwayLog(Long memberId, Long lastWalkwayLogId, int size) {
        LocalDateTime lastCreatedAt = getWalkwayLogCreatedAt(lastWalkwayLogId);
        return walkwayLogRepository.getUserWalkwayLog(memberId, lastCreatedAt, size);
    }

    public CursorResponse<WalkwayLog> getCrewFeed(Long crewId, Long lastWalkwayLogId, int size) {
        LocalDateTime lastCreatedAt = getWalkwayLogCreatedAt(lastWalkwayLogId);
        return walkwayLogRepository.getCrewWalkwayLog(crewId, lastCreatedAt, size);
    }

    public CrewWeeklyStatistic getCrewWeeklyStat(Long crewId, LocalDate startDay, LocalDate endDay) {
        return walkwayLogRepository.getCrewWeeklyStat(crewId, startDay, endDay);
    }

    public CursorResponse<CrewMemberStatistic> getCrewRankingByDistance(Long crewId, Long memberId, LocalDate startDay, LocalDate endDay, int size) {
        return walkwayLogRepository.getCrewRankingByDistance(crewId, memberId, startDay, endDay, size);
    }

    public CursorResponse<CrewMemberStatistic> getCrewRankingByTime(Long crewId, Long memberId, LocalDate startDay, LocalDate endDay, int size) {
        return walkwayLogRepository.getCrewRankingByTime(crewId, memberId, startDay, endDay, size);
    }
}
