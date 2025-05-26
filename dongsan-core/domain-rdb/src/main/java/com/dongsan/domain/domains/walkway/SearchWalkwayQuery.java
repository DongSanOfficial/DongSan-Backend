package com.dongsan.domain.domains.walkway;

public record SearchWalkwayQuery(
        Long memberId,
        WalkwaySort sort,
        Double longitude,
        Double latitude,
        Double distance
) {
    public SearchWalkwayQuery(Long memberId, String sortType, Double longitude, Double latitude, Double distance) {
        this(
                memberId,
                WalkwaySort.typeOf(sortType),
                longitude,
                latitude,
                distance
        );
    }

}
