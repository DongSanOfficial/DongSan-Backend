package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;

import java.util.List;

public record UpdateWalkwayCommand(
        Long walkwayId,
        String name,
        String memo,
        ExposeLevel exposeLevel,
        List<String> hashtags
) {
}
