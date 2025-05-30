package com.dongsan.domain.domains.cowalk.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CowalkPostRepository {
	void save(CowalkPost cowalkPost);
}
