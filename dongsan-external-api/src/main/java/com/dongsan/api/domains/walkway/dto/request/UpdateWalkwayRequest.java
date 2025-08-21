package com.dongsan.api.domains.walkway.dto.request;

import java.util.List;

import com.dongsan.domain.domains.walkway.UpdateWalkwayCommand;
import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateWalkwayRequest(
        @NotNull
        @Size(max = 15, message = "산책로 제목은 15자 이내여야 합니다.")
        String name,
        @NotNull
        @Size(max = 100, message = "산책로 내용은 100자 이내여야 합니다.")
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
