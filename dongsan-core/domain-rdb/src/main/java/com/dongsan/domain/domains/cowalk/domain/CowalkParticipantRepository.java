package com.dongsan.domain.domains.cowalk.domain;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CowalkParticipantRepository {
    CowalkParticipant save(CowalkParticipant cowalkParticipant);

    Integer countByCowalkPostId(Long cowalkPostId);

    Boolean existsByMemberIdAndCowalkPostId(Long memberId, Long cowalkPostId);

    Map<Long, Integer> countByCowalkPostIds(List<Long> cowalkPostIds);
}
