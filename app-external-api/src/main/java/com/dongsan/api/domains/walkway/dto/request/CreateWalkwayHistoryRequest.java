package com.dongsan.api.domains.walkway.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateWalkwayHistoryRequest(
        @NotNull
        Integer time,
        @NotNull
        Double distance
) {
}
