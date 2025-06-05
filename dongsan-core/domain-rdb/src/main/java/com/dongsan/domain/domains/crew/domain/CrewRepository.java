package com.dongsan.domain.domains.crew.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewRepository {
    Optional<Crew> findById(Long crewId);

    Optional<Crew> findByIdWithLock(Long crewId);

    Long save(Crew crew);

    boolean existsByName(String name);
}

