package com.dongsan.domain.domains.cowalk.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.domain.domains.cowalk.domain.CowalkCommentRepository;

@Service
@Transactional
public class CowalkCommentRdbService {
	private final CowalkCommentRepository cowalkCommentRepository;

	public CowalkCommentRdbService(CowalkCommentRepository cowalkCommentRepository) {
		this.cowalkCommentRepository = cowalkCommentRepository;
	}

	public Integer countByCowalkPostId(Long cowalkPostId) {
		return cowalkCommentRepository.countByCowalkPostId(cowalkPostId);
	}
}
