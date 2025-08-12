package com.dongsan.domain.domains.walkwayLog;

import com.dongsan.domain.domains.crew.domain.CrewMemberStatistic;
import com.dongsan.domain.domains.crew.domain.CrewWeeklyStatistic;
import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WalkwayLogRepository {
    Optional<WalkwayLog> findById(Long walkwayHistoryId);

    Long save(WalkwayLog walkwayLog);

    CursorResponse<WalkwayLog> getUserWalkwayLog(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorResponse<WalkwayLog> getCrewWalkwayLog(Long crewId, LocalDateTime crewCreatedAt, LocalDateTime lastCreatedAt, int size);

    CursorResponse<CrewMemberStatistic> getCrewRankingByDistance(Long crewId, Long lastMemberId, LocalDate startDay, LocalDate endDay, int size);

    CursorResponse<CrewMemberStatistic> getCrewRankingByTime(Long crewId, Long lastMemberId, LocalDate startDay, LocalDate endDay, int size);

    CrewWeeklyStatistic getCrewWeeklyStat(Long crewId, LocalDate startOfWeek, LocalDate endOfWeek);

    void deleteAllInBatchByWalkwayId(Long walkwayId);

}
