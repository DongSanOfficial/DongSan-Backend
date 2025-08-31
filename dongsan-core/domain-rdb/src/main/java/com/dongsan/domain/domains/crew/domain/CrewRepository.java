package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrewRepository {
    Optional<Crew> findById(Long crewId);

    Long save(Crew crew);

    boolean existsByName(String name);

    CursorResponse<Crew> getMyCrews(Long memberId, Integer size, Long lastId);

    CursorResponse<Crew> searchCrews(String name, Integer size, Long lastId);

    CursorResponse<Crew> findCrewsByLogThisWeek(int size, Long lastId, Long memberId);

    List<Long> getAllMyCrewIds(Long memberId);
}

