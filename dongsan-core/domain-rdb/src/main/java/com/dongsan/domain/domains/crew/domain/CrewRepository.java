package com.dongsan.domain.domains.crew.domain;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.support.paging.CursorResponse;

@Repository
public interface CrewRepository {
    Optional<Crew> findById(Long crewId);

    Long save(Crew crew);

    boolean existsByName(String name);

    CursorResponse<Crew> getMyCrews(Long memberId, Integer size, Long lastId);

    CursorResponse<Crew> searchCrews(String name, Integer size, Long lastId);
}

