package com.dongsan.api.domains.walkway.dto.request;

import com.dongsan.domain.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateWalkwayRequest(
        @NotNull
        String name,
        @NotNull
        String memo,
        @NotNull
        List<String> hashtags,
        @NotNull
        WalkwayExposeLevel exposeLevel
) {
    public UpdateWalkwayCommand toUpdateWalkway(Long walkwayId) {
        return new UpdateWalkwayCommand(
                walkwayId,
                this.name(),
                this.memo(),
                this.exposeLevel(),
                this.hashtags()
        );
    }
}
