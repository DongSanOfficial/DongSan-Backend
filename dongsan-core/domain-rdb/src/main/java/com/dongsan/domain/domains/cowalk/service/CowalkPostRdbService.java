package com.dongsan.domain.domains.cowalk.service;

import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.domain.CowalkPostRepository;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.dongsan.domain.support.error.CoreErrorCode.COWALK_NOT_FOUND;

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

    public CursorResponse<CowalkPost> getCowalkPosts(Integer size, Long lastId, Long crewId) {
        return cowalkPostRepository.getCowalkPosts(size, lastId, crewId);
    }

    public void validCapacity(Long cowalkPostId, Integer participantCount) {
        CowalkPost cowalkPost = getCowalkPost(cowalkPostId);
        cowalkPost.validCapacity(participantCount);
    }

    public CursorResponse<CowalkPost> getJoinedCowalkPost(Long memberId, Long lastId, int size) {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        return cowalkPostRepository.getJoinedCowalkPost(memberId, twentyFourHoursAgo, lastId, size);
    }
}
