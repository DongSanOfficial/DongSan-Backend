package com.dongsan.domain.domains.crew.service;

import com.dongsan.domain.domains.crew.domain.CrewExposeLevel;

public record CrewInfoCommand(
        String name,
        String description,
        String rule,
        CrewExposeLevel exposeLevel,
        String password,
        boolean limitEnable,
        Integer memberLimit,
        String crewImageUrl
) {
}
