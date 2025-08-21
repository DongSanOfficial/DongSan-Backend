package com.dongsan.domain.domains.cowalk;

import java.time.LocalDateTime;

public record CreateCowalkPostCommand(
        Long crewId,
        Long memberId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Integer capacity,
        String memo
) {

}
