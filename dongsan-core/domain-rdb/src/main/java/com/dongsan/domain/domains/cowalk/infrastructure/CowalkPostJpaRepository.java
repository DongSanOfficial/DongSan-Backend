package com.dongsan.domain.domains.cowalk.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;

@Repository
public interface CowalkPostJpaRepository extends JpaRepository<CowalkPost, Long> {
}
