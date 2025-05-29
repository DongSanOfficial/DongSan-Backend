package com.dongsan.domain.domains.crew.infrastructure;

import com.dongsan.domain.domains.crew.domain.CrewRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CrewCoreRepository implements CrewRepository {
    private final CrewJpaRepository crewJpaRepository;

    public CrewCoreRepository(CrewJpaRepository crewJpaRepository) {
        this.crewJpaRepository = crewJpaRepository;
    }

    @Override
    public boolean existsByName(String name) {
        return crewJpaRepository.existsByName(name.trim());
    }
}
