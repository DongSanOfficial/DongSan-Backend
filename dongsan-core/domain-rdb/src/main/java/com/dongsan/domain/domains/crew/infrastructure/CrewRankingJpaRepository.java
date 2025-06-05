package com.dongsan.domain.domains.crew.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.crew.domain.CrewRanking;

@Repository
public interface CrewRankingJpaRepository extends JpaRepository<CrewRanking, Long> {
}
