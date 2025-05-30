package com.dongsan.domain.domains.cowalk.domain;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.support.util.CursorPage;

@Repository
public interface CowalkPostRepository {
	void save(CowalkPost cowalkPost);

	Optional<CowalkPost> findById(Long id);

	CursorPage<CowalkPost> getCowalkPosts(Integer size, Long lastId, Long crewId);
}
