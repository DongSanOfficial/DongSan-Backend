package com.dongsan.domain.domains.cowalk.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;

@Service
@Transactional
public class CowalkPostRdbService {
	private final CowalkPostRepository cowalkPostRepository;

	public CowalkPostRdbService(CowalkPostRepository cowalkPostRepository) {
		this.cowalkPostRepository = cowalkPostRepository;
	}

	public Long save(CreateCowalkPostCommand command) {
		CowalkPost cowalkPost = new CowalkPost(command);
		cowalkPostRepository.save(cowalkPost);
		return cowalkPost.getId();
	}
}
