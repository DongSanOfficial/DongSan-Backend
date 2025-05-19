package com.dongsan.rdb.domains.review.infrastructure;

import com.dongsan.rdb.domains.member.Member;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.review.CreateReview;
import com.dongsan.rdb.domains.review.domain.QReview;
import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.infrastructure.WalkwayJpaRepository;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLog;
import com.dongsan.rdb.domains.walkwayLog.WalkwayLogJpaRepository;
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
    private final MemberJpaRepository memberJpaRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayLogJpaRepository walkwayLogJpaRepository;
    private final JPAQueryFactory queryFactory;

    private final QReview review = QReview.review;

    @Autowired
    public ReviewCoreRepository(ReviewJpaRepository reviewJpaRepository, MemberJpaRepository memberJpaRepository,
                                WalkwayJpaRepository walkwayJpaRepository,
                                WalkwayLogJpaRepository walkwayLogJpaRepository, JPAQueryFactory queryFactory) {
        this.reviewJpaRepository = reviewJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.walkwayLogJpaRepository = walkwayLogJpaRepository;
        this.queryFactory = queryFactory;
    }

    @Override
    public Long save(Review review) {
        return reviewJpaRepository.save(review).getId();
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
    public List<Review> getUserReviews(Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        List<Review> reviewEntities = queryFactory.selectFrom(review)
                .join(review.walkway)
                .fetchJoin()
                .join(review.member)
                .fetchJoin()
                .where(review.member.id.eq(memberId),
                        createdAtLt(lastCreatedAt),
                        review.walkway.member.id.eq(memberId)
                                .or(review.walkway.exposeLevel.eq(ExposeLevel.PUBLIC)))
                .limit(size)
                .orderBy(review.createdAt.desc())
                .fetch();

        return reviewEntities.stream()
                .map(Review::toReview)
                .toList();
    }

    /**
     * reviewId보다 작은 reviewId를 검색하는 조건
     *
     * @param reviewId 마지막으로 가져온 reviewId
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression reviewIdLt(Long reviewId) {
        return reviewId != null ? review.id.lt(reviewId) : null;
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

    @Override
    public List<Review> getWalkwayReviewsLatest(Integer size, Long walkwayId, LocalDateTime lastCreatedAt) {
        List<Review> reviewEntities = queryFactory.selectFrom(review)
                .join(review.walkway)
                .fetchJoin()
                .join(review.member)
                .fetchJoin()
                .where(review.walkway.id.eq(walkwayId),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(review.createdAt.desc())
                .fetch();

        return reviewEntities.stream()
                .map(Review::toReview)
                .toList();
    }

    @Override
    public List<Review> getWalkwayReviewsRating(Integer size, Long walkwayId, LocalDateTime lastCreatedAt,
                                                Rating lastRating) {
        List<Review> reviewEntities = queryFactory.selectFrom(review)
                .join(review.walkway)
                .fetchJoin()
                .join(review.member)
                .fetchJoin()
                .where(review.walkway.id.eq(walkwayId),
                        lastRating == null
                                ? null
                                : review.rating.lt(lastRating.getNum())
                                .or(review.rating.eq(lastRating.getNum())
                                        .and(createdAtLt(lastCreatedAt))
                                )
                )
                .limit(size)
                .orderBy(review.rating.desc(), review.createdAt.desc())
                .fetch();

        return reviewEntities.stream()
                .map(Review::toReview)
                .toList();
    }

    @Override
    public Map<Rating, Long> getWalkwayRating(Long walkwayId) {
        Map<Integer, Long> rawResult = queryFactory.from(review)
                .where(review.walkway.id.eq(walkwayId))
                .groupBy(review.rating)
                .transform(groupBy(review.rating).as(review.rating.count()));

        return rawResult.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> Rating.numOf(entry.getKey()), Map.Entry::getValue));
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        return reviewJpaRepository.findById(reviewId)
                .map(Review::toReview);
    }

    @Override
    public boolean existsById(Long reviewId) {
        return reviewJpaRepository.existsById(reviewId);
    }

    @Override
    public boolean existsByIdAndMemberId(Long reviewId, Long memberId) {
        return reviewJpaRepository.existsByIdAndMemberId(reviewId, memberId);
    }

    @Override
    public Long save(CreateReview createReview) {
        Member member = memberJpaRepository.getReferenceById(createReview.memberId());
        Walkway walkway = walkwayJpaRepository.getReferenceById(createReview.walkwayId());
        WalkwayLog walkwayLog = walkwayLogJpaRepository.getReferenceById(
                createReview.walkwayHistoryId());

        Review review = new Review(createReview.rating(), createReview.content(), member,
                walkway, walkwayLog);
        reviewJpaRepository.save(review);

        return review.getId();
    }
}
