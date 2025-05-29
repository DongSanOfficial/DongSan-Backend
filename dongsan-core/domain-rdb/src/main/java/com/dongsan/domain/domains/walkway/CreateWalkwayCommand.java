package com.dongsan.domain.domains.walkway;

import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;

import java.util.List;

public record CreateWalkwayCommand(
        String name,
        String memo,
        Double distance,
        Integer time,
        List<String> hashtags,
        WalkwayExposeLevel walkwayExposeLevel,
        List<WalkwayCoordinate> course,
        String imageUrl,
        Long memberId
) {
}
