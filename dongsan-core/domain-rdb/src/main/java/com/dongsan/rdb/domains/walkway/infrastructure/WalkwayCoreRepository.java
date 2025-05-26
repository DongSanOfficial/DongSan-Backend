package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
import com.dongsan.rdb.domains.walkway.domain.QLikedWalkway;
import com.dongsan.rdb.domains.walkway.domain.QWalkway;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.support.util.CursorPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class WalkwayCoreRepository implements WalkwayRepository {
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayQueryDSLRepository walkwayQueryDSLRepository;
    private final JPAQueryFactory queryFactory;
    private final QWalkway walkway = QWalkway.walkway;
    private final QLikedWalkway likedWalkway = QLikedWalkway.likedWalkway;

    @Autowired
    public WalkwayCoreRepository(WalkwayJpaRepository walkwayJpaRepository,
                                 WalkwayQueryDSLRepository walkwayQueryDSLRepository, JPAQueryFactory queryFactory) {
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.walkwayQueryDSLRepository = walkwayQueryDSLRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(Walkway walkway) {
        return walkwayJpaRepository.save(walkway)
                .getId();
    }

    @Override
    public Optional<Walkway> getWalkway(Long walkwayId) {
        return walkwayJpaRepository.findById(walkwayId);
    }

    @Override
    public boolean existsWalkway(Long walkwayId) {
        return walkwayJpaRepository.existsById(walkwayId);
    }

    @Override
    public boolean existsWalkway(Long walkwayId, Long memberId) {
        return walkwayJpaRepository.existsByIdAndMemberId(walkwayId, memberId);
    }

    @Override
    public List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery searchWalkwayQuery) {
        return walkwayQueryDSLRepository.searchWalkwaysLiked(searchWalkwayQuery);
    }

    @Override
    public List<Walkway> searchWalkwaysRating(SearchWalkwayQuery searchWalkwayQuery) {
        return walkwayQueryDSLRepository.searchWalkwaysRating(searchWalkwayQuery);
    }

    @Override
    public CursorPage<Walkway> getUserLikedWalkway(Long memberId, LocalDateTime lastCreatedAt, int size) {
        List<Walkway> result = queryFactory.selectFrom(walkway)
                .join(likedWalkway).on(likedWalkway.walkwayId.eq(walkway.id))
                .where(
                        likedWalkway.memberId.eq(memberId),
                        getExposeCondition(memberId),
                        createdAtLt(lastCreatedAt)
                )
                .orderBy(walkway.createdAt.desc())
                .limit(size + 1)
                .fetch();

        return new CursorPage<>(result, size);
    }

    @Override
    public CursorPage<Walkway> getUserWalkway(Long memberId, LocalDateTime lastCreatedAt, int size) {
        List<Walkway> result = queryFactory.selectFrom(walkway)
                .where(walkway.memberId.eq(memberId), createdAtLt(lastCreatedAt))
                .orderBy(walkway.createdAt.desc())
                .limit(size + 1)
                .fetch();

        return new CursorPage<>(result, size);
    }

    @Override
    public List<Walkway> getWalkwaysLatest(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayQueryDSLRepository.getWalkwaysLatest(size, lastWalkwayId, memberId);
    }

    @Override
    public List<Walkway> getWalkwaysLiked(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayQueryDSLRepository.getWalkwaysLiked(size, lastWalkwayId, memberId);
    }

    @Override
    public List<Walkway> getWalkwaysRating(Integer size, Long lastWalkwayId, Long memberId) {
        return walkwayQueryDSLRepository.getWalkwaysRating(size, lastWalkwayId, memberId);
    }

    @Override
    public Map<Long, Walkway> getWalkways(List<Long> walkwayIds) {
        List<Walkway> result = queryFactory.selectFrom(walkway)
                .where(walkway.id.in(walkwayIds))
                .fetch();
        
        return result.stream()
                .collect(Collectors.toMap(Walkway::getId, Function.identity()));
    }

    @Override
    public void delete(Walkway walkway) {
        walkwayJpaRepository.delete(walkway);
    }


    //    @Override
//    public void updateWalkwayRating(Integer reviewCount, Double rating, Long walkwayId) {
//        Walkway walkway = walkwayJpaRepository.getReferenceById(walkwayId);
//        walkway.updateRatingAndReviewCount(rating, reviewCount);
//        walkwayJpaRepository.save(walkway);
//    }

//    @Override
//    public Map<Long, Boolean> existsLikedWalkways(Long memberId, List<Long> walkwayIds) {
//        return likedWalkwayQueryDSLRepository.existsLikedWalkways(memberId, walkwayIds);
//    }

    /**
     * lastCreatedAt 보다 작은 createdAt 를 가진 walkway를 조회하는 조건
     *
     * @param createdAt 마지막으로 가져온 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime createdAt) {
        return createdAt == null ? null : walkway.createdAt.lt(createdAt);
    }

    // 사용자에게 보여질 수 있는 산책로 조건 (공개 또는 본인이 작성한 산책로)
    private BooleanExpression getExposeCondition(Long memberId) {
        if (memberId == null) {
            return walkway.walkwayInfo.exposeLevel.eq(ExposeLevel.PUBLIC);
        }
        return walkway.walkwayInfo.exposeLevel.eq(ExposeLevel.PUBLIC)
                .or(walkway.memberId.eq(memberId));
    }
}
