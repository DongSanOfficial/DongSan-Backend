package com.dongsan.domain.domains.cowalk.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface CowalkCommentRepository {
	Integer countByCowalkPostId(Long cowalkPostId);
}
