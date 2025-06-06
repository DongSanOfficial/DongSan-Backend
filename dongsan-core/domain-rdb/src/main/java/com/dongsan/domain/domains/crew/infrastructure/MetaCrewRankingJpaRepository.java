package com.dongsan.domain.domains.crew.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.crew.domain.MetaCrewRanking;

@Repository
public interface MetaCrewRankingJpaRepository extends JpaRepository<MetaCrewRanking, Long> {
}
