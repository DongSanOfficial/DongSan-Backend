package com.dongsan.domain.domains.walkwayLog;

import com.dongsan.domain.domains.crew.domain.CrewWeeklyStatistic;
import com.dongsan.domain.support.util.CursorResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WalkwayLogRepository {
    Optional<WalkwayLog> findById(Long walkwayHistoryId);

    Long save(WalkwayLog walkwayLog);

    CursorResponse<WalkwayLog> getUserWalkwayLog(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorResponse<WalkwayLog> getCrewWalkwayLog(Long crewId, LocalDateTime lastCreatedAt, int size);

    void deleteAllInBatchByWalkwayId(Long walkwayId);

    CrewWeeklyStatistic getCrewWeeklyStat(Long crewId, LocalDate startOfWeek, LocalDate endOfWeek);
}
