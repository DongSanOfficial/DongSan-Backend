package com.dongsan.domain.domains.cowalk.domain;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface CowalkPostRepository {
	void save(CowalkPost cowalkPost);

	Optional<CowalkPost> findById(Long id);
}
