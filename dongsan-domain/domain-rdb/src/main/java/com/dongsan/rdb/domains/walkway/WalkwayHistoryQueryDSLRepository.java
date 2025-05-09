package com.dongsan.rdb.domains.walkway;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.dongsan.core.domains.walkway.ExposeLevel;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class WalkwayHistoryQueryDSLRepository {
	private final JPAQueryFactory queryFactory;

	public WalkwayHistoryQueryDSLRepository(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	private QWalkwayHistoryEntity walkwayHistory = QWalkwayHistoryEntity.walkwayHistoryEntity;

	// 산책로의 리뷰 가능한 회원의 기록 조회
	public List<WalkwayHistoryEntity> getCanReviewWalkwayHistories(Long walkwayId, Long memberId, int size,
		LocalDateTime lastCreatedAt) {
		return queryFactory.selectFrom(walkwayHistory)
			.join(walkwayHistory.walkway)
			.fetchJoin()
			.where(
				this.canReviewCondition(memberId),
				walkwayHistory.walkway.id.eq(walkwayId),
				createdAtLt(lastCreatedAt)
			)
			.limit(size)
			.orderBy(walkwayHistory.createdAt.desc())
			.fetch();
	}

	// 모든 산책로의 리뷰 가능한 회원의 기록 조회
	public List<WalkwayHistoryEntity> getUserCanReviewWalkwayHistories(Long memberId, int size,
		LocalDateTime lastCreatedAt) {
		return queryFactory.selectFrom(walkwayHistory)
			.join(walkwayHistory.walkway)
			.fetchJoin()
			.where(
				this.canReviewCondition(memberId),
				walkwayHistory.walkway.exposeLevel.eq(ExposeLevel.PUBLIC),
				createdAtLt(lastCreatedAt)
			)
			.limit(size)
			.orderBy(walkwayHistory.createdAt.desc())
			.fetch();
	}

	private BooleanExpression createdAtLt(LocalDateTime lastCreatedAt) {
		return lastCreatedAt != null ? walkwayHistory.createdAt.lt(lastCreatedAt) : null;
	}

	private BooleanExpression canReviewCondition(Long memberId) {
		return walkwayHistory.member.id.eq(memberId)
			.and(walkwayHistory.isReviewed.eq(false))
			.and(walkwayHistory.distance.goe(walkwayHistory.walkway.distance.multiply(2.0 / 3.0)));
	}
}
