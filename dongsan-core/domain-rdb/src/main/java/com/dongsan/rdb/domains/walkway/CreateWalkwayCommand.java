package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;

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
