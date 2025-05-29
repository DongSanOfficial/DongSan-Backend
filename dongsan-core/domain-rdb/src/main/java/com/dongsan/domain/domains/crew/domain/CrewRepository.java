package com.dongsan.domain.domains.crew.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository {
    boolean existsByName(String name);
}
