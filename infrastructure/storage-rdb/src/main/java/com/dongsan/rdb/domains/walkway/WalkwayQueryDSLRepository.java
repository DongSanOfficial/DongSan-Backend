package com.dongsan.rdb.domains.walkway;

import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WalkwayQueryDSLRepository {
    @Autowired
    public WalkwayQueryDSLRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;
    private QWalkwayEntity walkwayEntity = QWalkwayEntity.walkwayEntity;

    public List<WalkwayEntity> getUserLikedWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayEntity)
                .join(likedWalkwayEntity)
                .on(likedWalkwayEntity.walkwayEntity.eq(walkwayEntity))
                .where(
                        likedWalkwayEntity.memberEntity.id.eq(memberId),
                        walkwayEntity.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkwayEntity.memberEntity.id.eq(memberId)),
                        createdAtLt(lastCreatedAt)
                )
                .orderBy(walkwayEntity.createdAt.desc())
                .limit(size)
                .fetch();
    }

    public List<WalkwayEntity> getUserWalkway(Long memberId, Integer size, LocalDateTime lastCreatedAt) {
        return queryFactory.selectFrom(walkwayEntity)
                .where(walkwayEntity.memberEntity.id.eq(memberId), createdAtLt(lastCreatedAt))
                .orderBy(walkwayEntity.createdAt.desc())
                .limit(size)
                .fetch();
    }

    /**
     * walkwayId보다 작은 Id를 가진 walkway를 조회하는 조건 (즉, createdAt이 더 작은 walkway를 조회)
     *
     * @param walkwayId 마지막으로 가져온 walkwayId
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression walkwayIdLt(Long walkwayId) {
        return walkwayId != null ? walkwayEntity.id.lt(walkwayId) : null;
    }

    /**
     * lastCreatedAt 보다 작은 createdAt 를 가진 walkway를 조회하는 조건
     *
     * @param createdAt 마지막으로 가져온 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime createdAt) {
        return createdAt != null ? walkwayEntity.createdAt.lt(createdAt) : null;
    }

    // 검색 산책로 시작지점 거리 계산
    private BooleanExpression searchFilterDistance(Double longitude, Double latitude, Double distance) {
        return Expressions.booleanTemplate(
                "ST_Distance_Sphere({0}, ST_GeomFromText(concat('POINT(', {1}, ' ', {2}, ')'), 4326)) <= {3}",
                walkwayEntity.startLocation,
                latitude,
                longitude,
                distance * 1000
        );
    }

    // 좋아요 순 검색
    public List<WalkwayEntity> searchWalkwaysLiked(SearchWalkwayQuery query) {
        WalkwayEntity lastWalkwayEntity = queryFactory.selectFrom(walkwayEntity)
                .where(walkwayEntity.id.eq(query.lastWalkwayId()))
                .fetchOne();

        return queryFactory.select(walkwayEntity)
                .from(walkwayEntity)
                .where(
                        this.searchFilterDistance(query.longitude(), query.latitude(), query.distance()),
                        walkwayEntity.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkwayEntity.memberEntity.id.eq(query.userId())),
                        lastWalkwayEntity == null
                                ? null
                                : walkwayEntity.likeCount.lt(lastWalkwayEntity.getLikeCount())
                                        .or(walkwayEntity.likeCount.eq(lastWalkwayEntity.getLikeCount())
                                                .and(createdAtLt(lastWalkwayEntity.getCreatedAt()))
                                        )
                )
                .limit(query.size())
                .orderBy(walkwayEntity.likeCount.desc(), walkwayEntity.createdAt.desc())
                .fetch();
    }

    // 별점 순 검색
    public List<WalkwayEntity> searchWalkwaysRating(SearchWalkwayQuery query) {
        WalkwayEntity lastWalkwayEntity = queryFactory.selectFrom(walkwayEntity)
                .where(walkwayEntity.id.eq(query.lastWalkwayId()))
                .fetchOne();

        return queryFactory.select(walkwayEntity)
                .from(walkwayEntity)
                .where(
                        this.searchFilterDistance(query.longitude(), query.latitude(), query.distance()),
                        walkwayEntity.exposeLevel.eq(ExposeLevel.PUBLIC)
                                .or(walkwayEntity.memberEntity.id.eq(query.userId())),
                        lastWalkwayEntity == null
                                ? null
                                : walkwayEntity.rating.lt(lastWalkwayEntity.getRating())
                                        .or(walkwayEntity.rating.eq(lastWalkwayEntity.getRating())
                                                .and(createdAtLt(lastWalkwayEntity.getCreatedAt()))
                                        )
                )
                .limit(query.size())
                .orderBy(walkwayEntity.rating.desc(), walkwayEntity.createdAt.desc())
                .fetch();
    }

}
