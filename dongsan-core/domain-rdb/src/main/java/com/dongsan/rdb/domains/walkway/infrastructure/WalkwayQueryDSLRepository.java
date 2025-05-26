package com.dongsan.rdb.domains.walkway.infrastructure;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.dongsan.rdb.domains.walkway.QLikedWalkwayEntity.*;

@Repository
public class WalkwayQueryDSLRepository {
    @Autowired
    public WalkwayQueryDSLRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;
    private QWalkwayEntity walkwayEntity = QWalkwayEntity.walkwayEntity;


    // 좋아요 순 검색
    public List<Walkway> searchWalkwaysLiked(SearchWalkwayQuery query) {
        Walkway lastWalkway = this.getLastWalkwayEntity(query.lastWalkwayId());

        return queryFactory.selectFrom(walkwayEntity)
                .where(
                        this.getDistanceCondition(query.longitude(), query.latitude(), query.distance()),
                        this.getExposeCondition(query.userId()),
                        this.getLikedCondition(lastWalkway)
                )
                .limit(query.size())
                .orderBy(walkwayEntity.likeCount.desc(), walkwayEntity.createdAt.desc())
                .fetch();
    }

    // 별점 순 검색
    public List<Walkway> searchWalkwaysRating(SearchWalkwayQuery query) {
        Walkway lastWalkway = getLastWalkwayEntity(query.lastWalkwayId());

        return queryFactory.selectFrom(walkwayEntity)
                .where(
                        this.getDistanceCondition(query.longitude(), query.latitude(), query.distance()),
                        this.getExposeCondition(query.userId()),
                        this.getRatingCondition(lastWalkway)
                )
                .limit(query.size())
                .orderBy(walkwayEntity.rating.desc(), walkwayEntity.createdAt.desc())
                .fetch();
    }

    // 전체 조회 (거리계산 X)
    public List<Walkway> getWalkwaysLatest(Integer size, Long lastWalkwayId, Long memberId) {
        Walkway lastWalkway = this.getLastWalkwayEntity(lastWalkwayId);

        return queryFactory.selectFrom(walkwayEntity)
                .where(
                        this.getExposeCondition(memberId),
                        this.getLatestCondition(lastWalkway, lastWalkwayId)
                )
                .limit(size)
                .orderBy(walkwayEntity.createdAt.desc())
                .fetch();
    }

    // 좋아요한 산책로 조회 (거리계산 X)
    public List<Walkway> getWalkwaysLiked(Integer size, Long lastWalkwayId, Long memberId) {
        Walkway lastWalkway = this.getLastWalkwayEntity(lastWalkwayId);

        return queryFactory.selectFrom(walkwayEntity)
                .where(
                        this.getExposeCondition(memberId),
                        this.getLikedCondition(lastWalkway)
                )
                .limit(size)
                .orderBy(walkwayEntity.likeCount.desc(), walkwayEntity.createdAt.desc())
                .fetch();
    }

    // 별점순 산책로 조회 (거리계산 X)
    public List<Walkway> getWalkwaysRating(Integer size, Long lastWalkwayId, Long memberId) {
        Walkway lastWalkway = this.getLastWalkwayEntity(lastWalkwayId);

        return queryFactory.selectFrom(walkwayEntity)
                .where(
                        this.getExposeCondition(memberId),
                        this.getRatingCondition(lastWalkway)
                )
                .limit(size)
                .orderBy(walkwayEntity.rating.desc(), walkwayEntity.createdAt.desc())
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


    // 검색 산책로 시작지점 거리 계산
    private BooleanExpression getDistanceCondition(Double longitude, Double latitude, Double distance) {
        return Expressions.booleanTemplate(
                "ST_Distance_Sphere({0}, ST_GeomFromText(concat('POINT(', {1}, ' ', {2}, ')'), 4326)) <= {3}",
                walkwayEntity.startLocation,
                latitude,
                longitude,
                distance * 1000
        );
    }

    // 마지막 산책로 엔티티 조회
    private Walkway getLastWalkwayEntity(Long walkwayId) {
        if (walkwayId == null) {
            return null;
        }
        return queryFactory.selectFrom(walkwayEntity)
                .where(walkwayEntity.id.eq(walkwayId))
                .fetchOne();
    }

    // 사용자에게 보여질 수 있는 산책로 조건 (공개 또는 본인이 작성한 산책로)
    private BooleanExpression getExposeCondition(Long memberId) {
        if (memberId == null) {
            return walkwayEntity.exposeLevel.eq(ExposeLevel.PUBLIC);
        }
        return walkwayEntity.exposeLevel.eq(ExposeLevel.PUBLIC)
                .or(walkwayEntity.member.id.eq(memberId));
    }

    // 최신순 조건
    private BooleanExpression getLatestCondition(Walkway lastEntity, Long lastWalkwayId) {
        if (lastEntity == null) {
            return null;
        }
        return walkwayEntity.createdAt.lt(lastEntity.getCreatedAt())
                .or(walkwayEntity.createdAt.eq(lastEntity.getCreatedAt())
                        .and(walkwayIdLt(lastWalkwayId)));
    }

    // 좋아요순 조건
    private BooleanExpression getLikedCondition(Walkway lastEntity) {
        if (lastEntity == null) {
            return null;
        }
        return walkwayEntity.likeCount.lt(lastEntity.getLikeCount())
                .or(walkwayEntity.likeCount.eq(lastEntity.getLikeCount())
                        .and(createdAtLt(lastEntity.getCreatedAt())));
    }

    // 별점순 조건
    private BooleanExpression getRatingCondition(Walkway lastEntity) {
        if (lastEntity == null) {
            return null;
        }
        return walkwayEntity.rating.lt(lastEntity.getRating())
                .or(walkwayEntity.rating.eq(lastEntity.getRating())
                        .and(createdAtLt(lastEntity.getCreatedAt())));
    }

}
