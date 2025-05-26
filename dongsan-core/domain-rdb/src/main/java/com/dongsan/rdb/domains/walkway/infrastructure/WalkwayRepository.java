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

    List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery);

    List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery);

    CursorPage<Walkway> getUserLikedWalkway(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorPage<Walkway> getUserWalkway(Long memberId, LocalDateTime lastCreatedAt, int size);

    List<Walkway> getWalkwaysLatest(Integer size, Long lastWalkwayId, Long memberId);

    List<Walkway> getWalkwaysLiked(Integer size, Long lastWalkwayId, Long memberId);

    List<Walkway> getWalkwaysRating(Integer size, Long lastWalkwayId, Long memberId);

    Map<Long, Walkway> getWalkways(List<Long> walkwayIds);

    //void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId);

    void delete(Walkway walkway);

}
