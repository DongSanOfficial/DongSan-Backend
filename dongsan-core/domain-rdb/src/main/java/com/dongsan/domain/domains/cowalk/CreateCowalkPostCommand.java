package com.dongsan.domain.domains.cowalk;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateCowalkPostCommand(
        Long crewId,
        Long memberId,
        LocalDate date,
        LocalTime time,
        Integer capacity
) {
}
