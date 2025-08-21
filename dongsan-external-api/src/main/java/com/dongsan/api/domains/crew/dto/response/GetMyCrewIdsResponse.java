package com.dongsan.api.domains.crew.dto.response;

import java.util.List;

public record GetMyCrewIdsResponse(
        List<Long> crewIds
) {
}
