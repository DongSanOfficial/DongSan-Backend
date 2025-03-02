package com.dongsan.api.domains.walkway.dto.request;

import com.dongsan.core.domains.walkway.ExposeLevel;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateWalkwayRequest(
        @NotNull
        String name,
        @NotNull
        String memo,
        @NotNull
        List<String> hashtags,
        ExposeLevel exposeLevel
) {
}
