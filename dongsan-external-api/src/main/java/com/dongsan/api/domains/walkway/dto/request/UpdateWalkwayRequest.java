package com.dongsan.api.domains.walkway.dto.request;

import java.util.List;

import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.core.domains.walkway.UpdateWalkway;

import jakarta.validation.constraints.NotNull;

public record UpdateWalkwayRequest(
	@NotNull
	String name,
	@NotNull
	String memo,
	@NotNull
	List<String> hashtags,
	ExposeLevel exposeLevel
) {
	public UpdateWalkway toUpdateWalkway(Long walkwayId) {
		return new UpdateWalkway(
			walkwayId,
			this.name(),
			this.memo(),
			this.exposeLevel(),
			this.hashtags()
		);
	}
}
