package com.dongsan.rdb.domains.review;

import static com.querydsl.core.group.GroupBy.groupBy;

import com.dongsan.core.domains.review.CreateReview;
import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.ReviewRepository;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.walkway.entity.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.entity.WalkwayHistoryEntity;
import com.dongsan.rdb.domains.walkway.repository.WalkwayHistoryJpaRepository;
import com.dongsan.rdb.domains.walkway.repository.WalkwayJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewCoreRepository implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final WalkwayJpaRepository walkwayJpaRepository;
    private final WalkwayHistoryJpaRepository walkwayHistoryJpaRepository;
    private final JPAQueryFactory queryFactory;

    private QReviewEntity review = QReviewEntity.reviewEntity;
    @Autowired
    public ReviewCoreRepository(ReviewJpaRepository reviewJpaRepository, MemberJpaRepository memberJpaRepository,
                                WalkwayJpaRepository walkwayJpaRepository,
                                WalkwayHistoryJpaRepository walkwayHistoryJpaRepository, JPAQueryFactory queryFactory) {
        this.reviewJpaRepository = reviewJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
        this.walkwayJpaRepository = walkwayJpaRepository;
        this.walkwayHistoryJpaRepository = walkwayHistoryJpaRepository;
        this.queryFactory = queryFactory;
    }

    /**
     * 사용자가 작성한 리뷰를 리뷰 작성시간 기준 내림차순으로 조회한다.
     * <p>
     *     1. 사용자가 작성한 리뷰를 조회한다. <br>
     *     2. 조회된 마지막 리뷰보다 더 일찍 작성된 리뷰를 조회한다. <br>
     *     3. 내가 등록한 산책로의 리뷰인 경우에는 산책로의 공개/비공개 여부 상관없이 리뷰를 조회한다. <br>
     *     4. 타인이 등록한 산책로의 리뷰인 경우에는 Public(공개) 상태의 산책로의 리뷰만 조회한다. <br>
     * </p>
     * @param size          최대 조회 갯수
     * @param lastCreatedAt 마지막으로 조회된 Review의 작성 시간
     * @param memberId      사용자 id
     * @return              사용자가 작성한 리뷰들
     */
    @Override
    public List<Review> getUserReviews(Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        List<ReviewEntity> reviewEntities = queryFactory.selectFrom(review)
                .join(review.walkwayEntity).fetchJoin()
                .join(review.memberEntity).fetchJoin()
                .where(review.memberEntity.id.eq(memberId),
                        createdAtLt(lastCreatedAt),
                        review.walkwayEntity.memberEntity.id.eq(memberId)
                                .or(review.walkwayEntity.exposeLevel.eq(ExposeLevel.PUBLIC)))
                .limit(size)
                .orderBy(review.createdAt.desc())
                .fetch();

        return reviewEntities.stream()
                .map(ReviewEntity::toReview)
                .toList();
    }

    /**
     * reviewId보다 작은 reviewId를 검색하는 조건
     * @param reviewId 마지막으로 가져온 reviewId
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression reviewIdLt(Long reviewId){
        return reviewId != null ? review.id.lt(reviewId) : null;
    }

    /**
     * createdAt보다 작은 createdAt를 검색하는 조건
     * @param createdAt 마지막으로 가져온 createdAt
     * @return 조건 만족 안하면 null 반환, where 절에서 null은 무시된다.
     */
    private BooleanExpression createdAtLt(LocalDateTime createdAt){
        return createdAt != null ? review.createdAt.lt(createdAt) : null;
    }
    @Override
    public List<Review> getWalkwayReviewsLatest(Integer size, Long walkwayId, LocalDateTime lastCreatedAt) {
        List<ReviewEntity> reviewEntities = queryFactory.selectFrom(review)
                .join(review.walkwayEntity).fetchJoin()
                .join(review.memberEntity).fetchJoin()
                .where(review.walkwayEntity.id.eq(walkwayId),
                        createdAtLt(lastCreatedAt)
                )
                .limit(size)
                .orderBy(review.createdAt.desc())
                .fetch();

        return reviewEntities.stream()
                .map(ReviewEntity::toReview)
                .toList();
    }
    @Override
    public List<Review> getWalkwayReviewsRating(Integer size, Long walkwayId, LocalDateTime lastCreatedAt, Integer lastRating) {
        List<ReviewEntity> reviewEntities = queryFactory.selectFrom(review)
                .join(review.walkwayEntity).fetchJoin()
                .join(review.memberEntity).fetchJoin()
                .where(review.walkwayEntity.id.eq(walkwayId),
                        lastRating == null
                                ? null
                                : review.rating.lt(lastRating)
                                        .or(review.rating.eq(lastRating)
                                                .and(createdAtLt(lastCreatedAt))
                                        )
                )
                .limit(size)
                .orderBy(review.rating.desc(), review.createdAt.desc())
                .fetch();

        return reviewEntities.stream()
                .map(ReviewEntity::toReview)
                .toList();
    }

    @Override
    public Map<Integer, Long> getWalkwayRating(Long walkwayId) {
        return queryFactory.from(review)
                .where(review.walkwayEntity.id.eq(walkwayId))
                .groupBy(review.rating)
                .transform(groupBy(review.rating).as(review.rating.count()));
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        return reviewJpaRepository.findById(reviewId)
                .map(ReviewEntity::toReview);
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
        MemberEntity memberEntity = memberJpaRepository.getReferenceById(createReview.memberId());
        WalkwayEntity walkwayEntity = walkwayJpaRepository.getReferenceById(createReview.walkwayId());
        WalkwayHistoryEntity walkwayHistoryEntity = walkwayHistoryJpaRepository.getReferenceById(createReview.walkwayHistoryId());

        ReviewEntity reviewEntity = new ReviewEntity(createReview.rating(), createReview.content(), memberEntity, walkwayEntity, walkwayHistoryEntity);
        reviewJpaRepository.save(reviewEntity);

        return reviewEntity.getId();
    }
}
