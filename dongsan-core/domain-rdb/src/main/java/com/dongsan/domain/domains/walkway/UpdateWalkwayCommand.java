package com.dongsan.domain.domains.walkway;

import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;

import java.util.List;

public record UpdateWalkwayCommand(
        Long walkwayId,
        String name,
        String memo,
        WalkwayExposeLevel walkwayExposeLevel,
        List<String> hashtags
) {
}
