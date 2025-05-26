package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.support.util.CursorPage;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WalkwayRepository {
    Long save(Walkway walkway);

    Optional<Walkway> getWalkway(Long walkwayId);

    boolean existsWalkway(Long walkwayId);

    boolean existsWalkway(Long walkwayId, Long memberId);

    CursorPage<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery, Long lastWalkwayId, int size);

    CursorPage<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery, Long lastWalkwayId, int size);

    CursorPage<Walkway> getUserLikedWalkway(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorPage<Walkway> getUserWalkway(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorPage<Walkway> getWalkwaysByLatest(Long memberId, Long lastWalkwayId, int size);

    CursorPage<Walkway> getWalkwaysByLiked(Long memberId, Long lastWalkwayId, int size);

    CursorPage<Walkway> getWalkwaysByRating(Long memberId, Long lastWalkwayId, int size);

    Map<Long, Walkway> getWalkways(List<Long> walkwayIds);

    //void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId);

    void delete(Walkway walkway);

}
