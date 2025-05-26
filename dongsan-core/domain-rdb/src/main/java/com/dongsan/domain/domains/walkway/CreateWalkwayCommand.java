package com.dongsan.domain.domains.walkway;

import com.dongsan.domain.domains.walkway.domain.ExposeLevel;

import java.util.List;

public record CreateWalkwayCommand(
        String name,
        String memo,
        Double distance,
        Integer time,
        List<String> hashtags,
        ExposeLevel exposeLevel,
        List<WalkwayCoordinate> course,
        String imageUrl,
        Long memberId
) {
}
