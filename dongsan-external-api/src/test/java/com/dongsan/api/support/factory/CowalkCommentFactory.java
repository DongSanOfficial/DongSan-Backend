package com.dongsan.api.support.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.cowalk.domain.CowalkComment;
import com.dongsan.domain.domains.cowalk.domain.CowalkCommentRepository;

@Component
public class CowalkCommentFactory {
    @Autowired
    private CowalkCommentRepository cowalkCommentRepository;

    public void save(Long cowalkPostId, Long memberId) {
        cowalkCommentRepository.save(new CowalkComment(cowalkPostId, memberId, "test comment"));
    }
}
