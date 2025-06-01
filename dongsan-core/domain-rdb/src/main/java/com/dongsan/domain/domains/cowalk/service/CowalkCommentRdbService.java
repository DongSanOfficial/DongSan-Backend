package com.dongsan.domain.domains.cowalk.service;

import java.util.List;
import java.util.Map;

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

    public Map<Long, Integer> countByCowalkPostIds(List<Long> cowalkPostIds) {
        return cowalkCommentRepository.countByCowalkPostIds(cowalkPostIds);
    }
}
