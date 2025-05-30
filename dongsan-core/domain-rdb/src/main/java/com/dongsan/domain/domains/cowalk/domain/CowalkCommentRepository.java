package com.dongsan.domain.domains.cowalk.domain;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CowalkCommentRepository {
	Integer countByCowalkPostId(Long cowalkPostId);

	Map<Long, Integer> countByCowalkPostIds(List<Long> cowalkPostIds);
}
