package com.dongsan.domain.domains.cowalk.domain;

import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CowalkPostRepository {
    void save(CowalkPost cowalkPost);

    Optional<CowalkPost> findById(Long id);

    CursorResponse<CowalkPost> getCowalkPosts(Integer size, Long lastId, Long crewId);
}
