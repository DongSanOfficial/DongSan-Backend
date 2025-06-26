package com.dongsan.domain.domains.cowalk.domain;

import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CowalkPostRepository {
    void save(CowalkPost cowalkPost);

    Optional<CowalkPost> findById(Long id);

    CursorResponse<CowalkPost> getCowalkPosts(Integer size, Long lastId, Long crewId);

    CursorResponse<CowalkPost> getJoinedCowalkPost(Long memberId, LocalDateTime twentyFourHoursAgo, Long lastId, int size);
}
