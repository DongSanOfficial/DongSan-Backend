package com.dongsan.domain.domains.walkway.infrastructure;

import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.domains.walkway.domain.*;
import com.dongsan.domain.support.util.CursorPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
    private final JPAQueryFactory queryFactory;
    private final QWalkway walkway = QWalkway.walkway;
    private final QLikedWalkway likedWalkway = QLikedWalkway.likedWalkway;
    private final QMetaWalkwayLiked metaWalkwayLiked = QMetaWalkwayLiked.metaWalkwayLiked;
    private final QMetaWalkwayRating metaWalkwayRating = QMetaWalkwayRating.metaWalkwayRating;

    @Autowired
    public WalkwayCoreRepository(WalkwayJpaRepository walkwayJpaRepository,
                                 JPAQueryFactory queryFactory) {
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(Walkway walkway) {
        return walkwayJpaRepository.save(walkway)
                .getId();
    }

    @Override
    public Optional<Walkway> findById(Long walkwayId) {
        return walkwayJpaRepository.findById(walkwayId);
    }

    // 좋아요 순 검색
    @Override
    public CursorPage<Walkway> searchWalkwaysLiked(SearchWalkwayQuery query, Long lastWalkwayId, int size) {
        MetaWalkwayLiked metaLiked = getMetaWalkwayLiked(lastWalkwayId);

        List<Walkway> result = queryFactory.selectFrom(walkway)
                .join(metaWalkwayLiked).on(metaWalkwayLiked.walkwayId.eq(walkway.id))
                .where(
                        distanceCondition(query.longitude(), query.latitude(), query.distance()),
                        exposeCondition(query.memberId()),
                        likedCondition(metaLiked)
                )
                .limit(size + 1)
                .orderBy(metaWalkwayLiked.likeCount.desc(), walkway.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    // 별점 순 검색
    @Override
    public CursorPage<Walkway> searchWalkwaysRating(SearchWalkwayQuery query, Long lastWalkwayId, int size) {
        MetaWalkwayRating metaRating = getMetaWalkwayRating(lastWalkwayId);

        List<Walkway> result = queryFactory.selectFrom(walkway)
                .join(metaWalkwayRating).on(metaWalkwayRating.walkwayId.eq(walkway.id))
                .where(
                        distanceCondition(query.longitude(), query.latitude(), query.distance()),
                        exposeCondition(query.memberId()),
                        ratingCondition(metaRating)
                )
                .limit(size + 1)
                .orderBy(metaWalkwayRating.rating.desc(), walkway.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    @Override
    public CursorPage<Walkway> getUserLikedWalkway(Long memberId, LocalDateTime lastCreatedAt, int size) {
        List<Walkway> result = queryFactory.selectFrom(walkway)
                .join(likedWalkway).on(likedWalkway.walkwayId.eq(walkway.id))
                .where(
                        likedWalkway.memberId.eq(memberId),
                        exposeCondition(memberId),
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

    // 거리 계산 X
    @Override
    public CursorPage<Walkway> getWalkwaysByLatest(Long memberId, Long lastWalkwayId, int size) {
        Walkway lastWalkway = this.getWalkwayEntity(lastWalkwayId);

        List<Walkway> result = queryFactory.selectFrom(walkway)
                .where(
                        exposeCondition(memberId),
                        latestCondition(lastWalkway)
                )
                .limit(size + 1)
                .orderBy(walkway.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    // 거리 계산 X
    @Override
    public CursorPage<Walkway> getWalkwaysByLiked(Long memberId, Long lastWalkwayId, int size) {
        MetaWalkwayLiked metaLiked = getMetaWalkwayLiked(lastWalkwayId);

        List<Walkway> result = queryFactory.selectFrom(walkway)
                .join(metaWalkwayLiked).on(metaWalkwayLiked.walkwayId.eq(walkway.id))
                .where(
                        exposeCondition(memberId),
                        likedCondition(metaLiked)
                )
                .limit(size + 1)
                .orderBy(metaWalkwayLiked.likeCount.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    // 거리 계산 X
    @Override
    public CursorPage<Walkway> getWalkwaysByRating(Long memberId, Long lastWalkwayId, int size) {
        MetaWalkwayRating metaRating = getMetaWalkwayRating(lastWalkwayId);

        List<Walkway> result = queryFactory.selectFrom(walkway)
                .join(metaWalkwayRating).on(metaWalkwayRating.walkwayId.eq(walkway.id))
                .where(
                        exposeCondition(memberId),
                        ratingCondition(metaRating)
                )
                .limit(size + 1)
                .orderBy(metaWalkwayRating.rating.desc())
                .fetch();

        return new CursorPage<>(result, size);
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

    // 마지막 산책로 엔티티 조회
    private Walkway getWalkwayEntity(Long walkwayId) {
        if (walkwayId == null) {
            return null;
        }
        return queryFactory.selectFrom(walkway)
                .where(walkway.id.eq(walkwayId))
                .fetchOne();
    }

    private MetaWalkwayLiked getMetaWalkwayLiked(Long lastWalkwayId) {
        if (lastWalkwayId == null) {
            return null;
        }
        return queryFactory.selectFrom(metaWalkwayLiked)
                .where(metaWalkwayLiked.walkwayId.eq(lastWalkwayId))
                .fetchOne();
    }

    private MetaWalkwayRating getMetaWalkwayRating(Long lastWalkwayId) {
        if (lastWalkwayId == null) {
            return null;
        }
        return queryFactory.selectFrom(metaWalkwayRating)
                .where(metaWalkwayRating.walkwayId.eq(lastWalkwayId))
                .fetchOne();
    }

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
    private BooleanExpression exposeCondition(Long memberId) {
        if (memberId == null) {
            return walkway.walkwayInfo.exposeLevel.eq(ExposeLevel.PUBLIC);
        }
        return walkway.walkwayInfo.exposeLevel.eq(ExposeLevel.PUBLIC)
                .or(walkway.memberId.eq(memberId));
    }

    // 검색 산책로 시작지점 거리 계산
    private BooleanExpression distanceCondition(Double longitude, Double latitude, Double distance) {
        return Expressions.booleanTemplate(
                "ST_Distance_Sphere({0}, ST_GeomFromText(concat('POINT(', {1}, ' ', {2}, ')'), 4326)) <= {3}",
                walkway.geometry.startLocation,
                latitude,
                longitude,
                distance * 1000
        );
    }

    // 최신순 조건
    private BooleanExpression latestCondition(Walkway lastEntity) {
        if (lastEntity == null) {
            return null;
        }
        return walkway.createdAt.lt(lastEntity.getCreatedAt())
                .or(walkway.createdAt.eq(lastEntity.getCreatedAt())
                        .and(walkway.id.gt(lastEntity.getId())));
    }

    // 좋아요 순 정렬 (meta table 사용)
    private BooleanExpression likedCondition(MetaWalkwayLiked lastMetaLiked) {
        if (lastMetaLiked == null) {
            return null;
        }
        return metaWalkwayLiked.likeCount.lt(lastMetaLiked.getLikeCount())
                .or(metaWalkwayLiked.likeCount.eq(lastMetaLiked.getLikeCount())
                        .and(metaWalkwayLiked.id.gt(lastMetaLiked.getId())));
    }

    // 별점순 조건
    private BooleanExpression ratingCondition(MetaWalkwayRating lastMetaRating) {
        if (lastMetaRating == null) {
            return null;
        }
        return metaWalkwayRating.rating.lt(lastMetaRating.getRating())
                .or(metaWalkwayRating.rating.eq(lastMetaRating.getRating())
                        .and(metaWalkwayRating.id.gt(lastMetaRating.getId())));
    }
}
