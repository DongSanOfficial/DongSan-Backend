package com.dongsan.domain.domains.cowalk.domain;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dongsan.domain.support.paging.CursorResponse;

@Repository
public interface CowalkCommentRepository {
    Integer countByCowalkPostId(Long cowalkPostId);

    Map<Long, Integer> countByCowalkPostIds(List<Long> cowalkPostIds);

    CowalkComment save(CowalkComment cowalkComment);

    CursorResponse<CowalkComment> getCowalkComments(Integer size, Long lastId, Long cowalkPostId);
}
