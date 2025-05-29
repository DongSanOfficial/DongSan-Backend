package com.dongsan.domain.domains.crew.infrastructure;

import com.dongsan.domain.domains.crew.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewJpaRepository extends JpaRepository<Crew, Long> {
    boolean existsByName(String name);
}
