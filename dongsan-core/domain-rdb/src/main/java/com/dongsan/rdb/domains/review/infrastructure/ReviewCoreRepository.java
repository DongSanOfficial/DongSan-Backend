package com.dongsan.rdb.domains.review.infrastructure;

import com.dongsan.rdb.domains.member.QMember;
import com.dongsan.rdb.domains.review.domain.QReview;
import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
import com.dongsan.rdb.domains.walkway.domain.QWalkway;
import com.dongsan.rdb.domains.walkwayLog.QWalkwayLog;
import com.dongsan.rdb.support.util.CursorPage;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
public class ReviewCoreRepository implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final JPAQueryFactory queryFactory;

    private final QReview review = QReview.review;
    private final QWalkway walkway = QWalkway.walkway;
    private final QMember member = QMember.member;
    private final QWalkwayLog walkwayLog = QWalkwayLog.walkwayLog;

    @Autowired
    public ReviewCoreRepository(ReviewJpaRepository reviewJpaRepository, JPAQueryFactory queryFactory) {
        this.reviewJpaRepository = reviewJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(Review review) {
        return reviewJpaRepository.save(review).getId();
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        return reviewJpaRepository.findById(reviewId);
    }

    /**
     * 사용자가 작성한 리뷰를 리뷰 작성시간 기준 내림차순으로 조회한다.
     * <p>
     * 1. 사용자가 작성한 리뷰를 조회한다. <br>
     * 2. 조회된 마지막 리뷰보다 더 일찍 작성된 리뷰를 조회한다. <br>
     * 3. 내가 등록한 산책로의 리뷰인 경우에는 산책로의 공개/비공개 여부 상관없이 리뷰를 조회한다. <br>
     * 4. 타인이 등록한 산책로의 리뷰인 경우에는 Public(공개) 상태의 산책로의 리뷰만 조회한다. <br>
     * </p>
     *
     * @param size          최대 조회 갯수
     * @param lastCreatedAt 마지막으로 조회된 Review의 작성 시간
     * @param memberId      사용자 id
     * @return 사용자가 작성한 리뷰들
     */
    @Override
    public CursorPage<ReviewWithWalkwayQuery> getUserReviews(Long memberId, LocalDateTime lastCreatedAt, int size) {
        List<ReviewWithWalkwayQuery> result = queryFactory.select(Projections.constructor(ReviewWithWalkwayQuery.class,
                        review.id,
                        walkway.id,
                        walkway.name,
                        review.createdAt,
                        review.rating,
                        review.content))
                .from(review)
                .join(walkwayLog).on(review.walkwayLogId.eq(walkwayLog.id))
                .join(walkway).on(walkwayLog.walkwayId.eq(walkway.id))
                .join(member).on(walkwayLog.memberId.eq(member.id))
                .where(member.id.eq(memberId),
                        createdAtLt(lastCreatedAt),
                        walkway.memberId.eq(memberId)
                                .or(walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
                .limit(size + 1)
                .orderBy(review.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    /**
     * createdAt보다 작은 createdAt를 검색하는 조건
     *
     * @param createdAt 마지막으로 가져온 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime createdAt) {
        return createdAt != null ? review.createdAt.lt(createdAt) : null;
    }

    // 여기 member 조인하는거 취소 하고, 상단 레이어에서 쿼리 추가로 날려서 매핑 가능 (성능 테스트 해보고 분리할지 말지 고민)
    @Override
    public CursorPage<ReviewWithMemberQuery> getWalkwayReviewsLatest(Long walkwayId, LocalDateTime lastCreatedAt, int size) {
        List<ReviewWithMemberQuery> result = queryFactory.select(Projections.constructor(ReviewWithMemberQuery.class,
                        review.id,
                        member.id,
                        member.nickname,
                        review.createdAt,
                        review.rating,
                        review.content
                ))
                .from(review)
                .join(walkwayLog).on(review.walkwayLogId.eq(walkwayLog.id))
                .join(member).on(walkwayLog.memberId.eq(member.id))
                .where(walkwayLog.walkwayId.eq(walkwayId),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size + 1)
                .orderBy(review.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    // 여기 member 조인하는거 취소 하고, 상단 레이어에서 쿼리 추가로 날려서 매핑 가능 (성능 테스트 해보고 분리할지 말지 고민)
    // (rating DESC, createdAt DESC)
    @Override
    public CursorPage<ReviewWithMemberQuery> getWalkwayReviewsRating(Long walkwayId, Integer lastRating,
                                                                     LocalDateTime lastCreatedAt, int size) {
        List<ReviewWithMemberQuery> result = queryFactory.select(Projections.constructor(ReviewWithMemberQuery.class,
                        review.id,
                        member.id,
                        member.nickname,
                        review.createdAt,
                        review.rating,
                        review.content
                ))
                .from(review)
                .join(walkwayLog).on(review.walkwayLogId.eq(walkwayLog.id))
                .join(member).on(walkwayLog.memberId.eq(member.id))
                .where(walkwayLog.walkwayId.eq(walkwayId),
                        ratingLtCreatedAtLt(lastRating, lastCreatedAt)
                )
                .limit(size + 1)
                .orderBy(review.rating.desc(), review.createdAt.desc())
                .fetch();

        return new CursorPage<>(result, size);
    }

    /**
     * (rating DESC, createdAt DESC)
     */
    private BooleanExpression ratingLtCreatedAtLt(Integer lastRating, LocalDateTime lastCreatedAt) {
        if (lastCreatedAt == null) {
            return null;
        }
        return review.rating.lt(lastRating)
                .or(review.rating.eq(lastRating).and(createdAtLt(lastCreatedAt)));
    }

    @Override
    public Map<Rating, Long> getWalkwayRating(Long walkwayId) {
        Map<Integer, Long> rawResult = queryFactory.from(review)
                .join(walkwayLog).on(review.walkwayLogId.eq(walkwayLog.id))
                .where(walkwayLog.walkwayId.eq(walkwayId))
                .groupBy(review.rating)
                .transform(groupBy(review.rating).as(review.rating.count()));

        return rawResult.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> Rating.numOf(entry.getKey()), Map.Entry::getValue));
    }

}
