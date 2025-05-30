package com.dongsan.domain.domains.cowalk.infrastructure;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkCommentRepository;

@Repository
public class CowalkCommentCoreRepository implements CowalkCommentRepository {
	private final CowalkCommentJpaRepository cowalkCommentJpaRepository;

	public CowalkCommentCoreRepository(CowalkCommentJpaRepository cowalkCommentJpaRepository) {
		this.cowalkCommentJpaRepository = cowalkCommentJpaRepository;
	}

	@Override
	public Integer countByCowalkPostId(Long cowalkPostId) {
		return cowalkCommentJpaRepository.countByCowalkPostId(cowalkPostId);
	}
}
