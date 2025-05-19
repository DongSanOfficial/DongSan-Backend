package com.dongsan.rdb.domains.walkway;

public record SearchWalkwayQuery(
	Long userId,
	Double longitude,
	Double latitude,
	Double distance,
	Long lastWalkwayId,
	int size
) {
}
