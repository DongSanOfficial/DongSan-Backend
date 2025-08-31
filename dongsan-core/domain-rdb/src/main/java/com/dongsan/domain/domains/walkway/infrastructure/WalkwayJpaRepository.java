package com.dongsan.domain.domains.walkway.infrastructure;

import com.dongsan.domain.domains.walkway.domain.Walkway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayJpaRepository extends JpaRepository<Walkway, Long> {
}
