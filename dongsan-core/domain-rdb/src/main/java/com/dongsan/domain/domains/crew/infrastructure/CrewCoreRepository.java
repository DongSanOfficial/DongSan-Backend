package com.dongsan.domain.domains.crew.infrastructure;

import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CrewCoreRepository implements CrewRepository {
    private final CrewJpaRepository crewJpaRepository;

    public CrewCoreRepository(CrewJpaRepository crewJpaRepository) {
        this.crewJpaRepository = crewJpaRepository;
    }

    @Override
    public Optional<Crew> findById(Long crewId) {
        return crewJpaRepository.findById(crewId);
    }

    @Override
    public Optional<Crew> findByIdWithLock(Long crewId) {
        return crewJpaRepository.findByIdWithLock(crewId);
    }

    @Override
    public Long save(Crew crew) {
        return crewJpaRepository.save(crew).getId();
    }

    @Override
    public boolean existsByName(String name) {
        return crewJpaRepository.existsByName(name.trim());
    }

}
