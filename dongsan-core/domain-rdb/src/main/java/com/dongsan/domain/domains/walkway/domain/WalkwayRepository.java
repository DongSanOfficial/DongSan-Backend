package com.dongsan.domain.domains.walkway.domain;

import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.support.util.CursorResponse;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WalkwayRepository {
    Long save(Walkway walkway);

    Optional<Walkway> findById(Long walkwayId);

    CursorResponse<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery, Long lastWalkwayId, int size);

    CursorResponse<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery, Long lastWalkwayId, int size);

    CursorResponse<Walkway> getUserLikedWalkway(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorResponse<Walkway> getUserWalkway(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorResponse<Walkway> getWalkwaysByLatest(Long memberId, Long lastWalkwayId, int size);

    CursorResponse<Walkway> getWalkwaysByLiked(Long memberId, Long lastWalkwayId, int size);

    CursorResponse<Walkway> getWalkwaysByRating(Long memberId, Long lastWalkwayId, int size);

    Map<Long, Walkway> getWalkways(List<Long> walkwayIds);

    void delete(Walkway walkway);

}
