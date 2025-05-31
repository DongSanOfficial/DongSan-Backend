package com.dongsan.domain.domains.cowalk.service;

import static com.dongsan.domain.support.error.CoreErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.util.CursorPage;

@Service
@Transactional
public class CowalkPostRdbService {
	private final CowalkPostRepository cowalkPostRepository;

	public CowalkPostRdbService(CowalkPostRepository cowalkPostRepository) {
		this.cowalkPostRepository = cowalkPostRepository;
	}

	public CowalkPost getCowalkPost(Long id) {
		return cowalkPostRepository.findById(id)
			.orElseThrow(() -> new CoreException(COWALK_NOT_FOUND));
	}

	public Long save(CreateCowalkPostCommand command) {
		CowalkPost cowalkPost = new CowalkPost(command);
		cowalkPostRepository.save(cowalkPost);
		return cowalkPost.getId();
	}

	public CursorPage<CowalkPost> getCowalkPosts(Integer size, Long lastId, Long crewId) {
		return cowalkPostRepository.getCowalkPosts(size, lastId, crewId);
	}

	public void validCapacity(Long cowalkPostId, Integer participantCount) {
		CowalkPost cowalkPost = getCowalkPost(cowalkPostId);
		cowalkPost.validCapacity(participantCount);
	}
}
