package com.dongsan.api.domains.bookmark;

import jakarta.validation.constraints.NotNull;

public record WalkwayIdRequest(
        @NotNull
        Long walkwayId
) {
}
