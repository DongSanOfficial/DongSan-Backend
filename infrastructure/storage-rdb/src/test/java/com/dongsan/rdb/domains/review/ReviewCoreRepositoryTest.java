package com.dongsan.rdb.domains.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.dongsan.common.support.RepositoryTest;
import com.dongsan.core.domains.review.Rating;
import com.dongsan.core.domains.review.Review;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayHistoryEntity;
import com.dongsan.rdb.domains.walkway.WalkwayHistoryJpaRepository;
import com.dongsan.rdb.domains.walkway.WalkwayJpaRepository;
import fixture.MemberEntityFixture;
import fixture.ReviewEntityFixture;
import fixture.WalkwayEntityFixture;
import fixture.WalkwayHistoryFixture;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("ReviewCoreRepository Unit Test")
class ReviewCoreRepositoryTest extends RepositoryTest {
    @Autowired
    ReviewCoreRepository reviewCoreRepository;
    @Autowired
    TestEntityManager em;
    @MockBean
    ReviewJpaRepository reviewJpaRepository;
    @MockBean
    MemberJpaRepository memberJpaRepository;
    @MockBean
    WalkwayJpaRepository walkwayJpaRepository;
    @MockBean
    WalkwayHistoryJpaRepository walkwayHistoryJpaRepository;

    @Nested
    @DisplayName("getUserReviews 메소드는")
    class Describe_getUserReviews {
        MemberEntity memberEntity;
        WalkwayEntity walkwayEntity;
        List<ReviewEntity> reviewEntities = new ArrayList<>();

        @BeforeEach
        void setUp() {
            memberEntity = MemberEntityFixture.createMember();
            walkwayEntity = WalkwayEntityFixture.createWalkway(memberEntity);
            em.persist(memberEntity);
            em.persist(walkwayEntity);
            for (int i = 0; i < 6; i++) {
                WalkwayHistoryEntity walkwayHistoryEntity = WalkwayHistoryFixture.createWalkwayHistory(memberEntity,
                        walkwayEntity);
                em.persist(walkwayHistoryEntity);
                ReviewEntity reviewEntity = ReviewEntityFixture.createReview(memberEntity, walkwayEntity,
                        walkwayHistoryEntity);
                reviewEntities.add(reviewEntity);
                em.persist(reviewEntity);
            }
        }

        @Test
        @DisplayName("타인이 등록한 산책로이면 공개 상태의 산책로의 리뷰만 조회한다.")
        void it_returns_others_public_walkway_review() {
            // given
            MemberEntity other = MemberEntityFixture.createMember();
            WalkwayEntity otherPublicWalkwayEntity = WalkwayEntityFixture.createWalkway(other);
            WalkwayEntity otherPrivateWalkwayEntity = WalkwayEntityFixture.createPrivateWalkway(other);
            WalkwayHistoryEntity publicWalkwayHistory = WalkwayHistoryFixture.createWalkwayHistory(other,
                    otherPublicWalkwayEntity);
            WalkwayHistoryEntity privateWalkwayHistory = WalkwayHistoryFixture.createWalkwayHistory(other,
                    otherPrivateWalkwayEntity);

            em.persist(other);
            em.persist(otherPublicWalkwayEntity);
            em.persist(otherPrivateWalkwayEntity);
            em.persist(publicWalkwayHistory);
            em.persist(privateWalkwayHistory);
            em.persist(ReviewEntityFixture.createReview(memberEntity, otherPublicWalkwayEntity, publicWalkwayHistory));
            em.persist(ReviewEntityFixture.createReview(memberEntity, otherPrivateWalkwayEntity, privateWalkwayHistory));
            Integer size = 10;
            LocalDateTime lastCreateAt = null;
            Long memberId = memberEntity.getId();

            // when
            List<Review> result = reviewCoreRepository.getUserReviews(size, lastCreateAt, memberId);

            // then
            assertThat(result).hasSize(reviewEntities.size() + 1);
        }


        @Test
        @DisplayName("lastCreateAt가 null이면 가장 최근의 review들을 내림차순으로 가져온다.")
        void it_returns_most_recent_reviews() {
            // given
            Integer size = 5;
            LocalDateTime lastCreateAt = null;
            Long memberId = memberEntity.getId();

            // when
            List<Review> result = reviewCoreRepository.getUserReviews(size, lastCreateAt, memberId);

            // then
            assertThat(result).hasSize(5);
            for (int i = 1; i < result.size(); i++) {
                LocalDateTime after = result.get(i - 1).createdAt();
                LocalDateTime prev = result.get(i).createdAt();
                assertThat(prev).isBeforeOrEqualTo(after);
            }
        }
    }

    @Nested
    @DisplayName("getWalkwayReviewsLatest 메서드는")
    class Describe_getWalkwayReviewsLatest {
        MemberEntity memberEntity;
        WalkwayEntity walkwayEntity;
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        @BeforeEach
        void setUp() {
            memberEntity = MemberEntityFixture.createMember();
            walkwayEntity = WalkwayEntityFixture.createWalkway(memberEntity);
            em.persist(memberEntity);
            em.persist(walkwayEntity);
            for (int i = 0; i < 6; i++) {
                WalkwayHistoryEntity walkwayHistoryEntity = WalkwayHistoryFixture.createWalkwayHistory(memberEntity,
                        walkwayEntity);
                em.persist(walkwayHistoryEntity);
                ReviewEntity reviewEntity = ReviewEntityFixture.createReview(memberEntity, walkwayEntity,
                        walkwayHistoryEntity);
                reviewEntities.add(reviewEntity);
                em.persist(reviewEntity);
            }
        }

        @Test
        @DisplayName("리뷰 리스트를 시간순으로 반환한다.")
        void it_returns_review_list_latest() {
            // Given
            Integer limit = 5;
            Long walkwayId = walkwayEntity.getId();

            // When
            List<Review> result = reviewCoreRepository.getWalkwayReviewsLatest(limit, walkwayId, null);

            // Then
            LocalDateTime beforeId = result.get(0).createdAt();
            for(int count = 1; count < 5; count++) {
                LocalDateTime currentId = result.get(count).createdAt();
                assertThat(currentId).isBeforeOrEqualTo(beforeId);
                beforeId = currentId;
            }
        }
    }

    @Nested
    @DisplayName("getWalkwayReviewsRating 메서드는")
    class Describe_getWalkwayReviewsRatingEntity {
        MemberEntity memberEntity;
        WalkwayEntity walkwayEntity;
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        @BeforeEach
        void setUp() {
            memberEntity = MemberEntityFixture.createMember();
            walkwayEntity = WalkwayEntityFixture.createWalkway(memberEntity);
            em.persist(memberEntity);
            em.persist(walkwayEntity);
            for (int i = 0; i < 6; i++) {
                WalkwayHistoryEntity walkwayHistoryEntity = WalkwayHistoryFixture.createWalkwayHistory(memberEntity,
                        walkwayEntity);
                em.persist(walkwayHistoryEntity);
                ReviewEntity reviewEntity = ReviewEntityFixture.createReview(memberEntity, walkwayEntity,
                        walkwayHistoryEntity);
                reviewEntities.add(reviewEntity);
                em.persist(reviewEntity);
            }
        }

        @Test
        @DisplayName("리뷰 리스트를 별점순으로 반환한다.")
        void it_returns_review_list_rating() {
            // Given
            Integer size = 5;
            Long walkwayId = walkwayEntity.getId();

            // When
            List<Review> result = reviewCoreRepository.getWalkwayReviewsRating(size, walkwayId, null, null);

            // Then
            Integer beforeRating = result.get(0).rating().getNum();
            for(int count = 1; count < 5; count++) {
                Integer currentRating = result.get(count).rating().getNum();
                assertThat(currentRating).isLessThanOrEqualTo(beforeRating);
                beforeRating = currentRating;
            }
        }
    }

    @Nested
    @DisplayName("getWalkwaysRating 메서드는")
    class Describe_getWalkwaysRating {
        MemberEntity memberEntity;
        WalkwayEntity walkwayEntity;
        List<ReviewEntity> reviewEntities = new ArrayList<>();
        @BeforeEach
        void setUp() {
            memberEntity = MemberEntityFixture.createMember();
            walkwayEntity = WalkwayEntityFixture.createWalkway(memberEntity);
            em.persist(memberEntity);
            em.persist(walkwayEntity);
            for (int i = 1; i <= 5; i++) {
                WalkwayHistoryEntity walkwayHistoryEntity = WalkwayHistoryFixture.createWalkwayHistory(memberEntity,
                        walkwayEntity);
                em.persist(walkwayHistoryEntity);
                ReviewEntity reviewEntity = ReviewEntityFixture.createReview(memberEntity, walkwayEntity, walkwayHistoryEntity, Rating.numOf(i), "test");
                reviewEntities.add(reviewEntity);
                em.persist(reviewEntity);
            }
        }

        @Test
        @DisplayName("각 별점의 개수를 저장한 튜플 리스트를 반환한다.")
        void it_returns_rating_tuple_list() {
            // When
            Map<Rating, Long> result = reviewCoreRepository.getWalkwayRating(walkwayEntity.getId());

            // Then
            for(Rating rating : result.keySet()) {
                assertThat(result).containsEntry(rating, 1L);
            }
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class Describe_findById {
        @Test
        @DisplayName("리뷰를 조회한다.")
        void it_returns_review() {
            // given
            Long reviewId = 1L;
            MemberEntity memberEntity = MemberEntityFixture.createMember();
            WalkwayEntity walkwayEntity = WalkwayEntityFixture.createWalkway(memberEntity);
            WalkwayHistoryEntity walkwayHistoryEntity = WalkwayHistoryFixture.createWalkwayHistory(memberEntity, walkwayEntity);
            ReviewEntity reviewEntity = ReviewEntityFixture.createReview(memberEntity, walkwayEntity, walkwayHistoryEntity);
            when(reviewJpaRepository.findById(reviewId)).thenReturn(Optional.of(reviewEntity));

            // when
            Optional<Review> result = reviewCoreRepository.findById(reviewId);

            // then
            assertThat(result).isPresent();
        }
    }

    @Nested
    @DisplayName("existsById 메서드는")
    class Describe_existsById {
        @Test
        @DisplayName("리뷰 존재 여부를 반환한다.")
        void it_returns_exists() {
            // given
            Long reviewId = 1L;
            boolean exists = true;
            when(reviewJpaRepository.existsById(reviewId)).thenReturn(exists);

            // when
            boolean result = reviewCoreRepository.existsById(reviewId);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("existsByIdAndMemberId 메서드는")
    class Describe_existsByIdAndMemberId {
        @Test
        @DisplayName("리뷰 존재 여부를 반환한다.")
        void it_returns_exists() {
            // given
            Long reviewId = 1L;
            Long memberId = 1L;
            boolean exists = true;
            when(reviewJpaRepository.existsByIdAndMemberId(reviewId, memberId)).thenReturn(exists);

            // when
            boolean result = reviewCoreRepository.existsByIdAndMemberId(reviewId, memberId);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {
        @Test
        @DisplayName("리뷰를 저장하고 id를 반환한다.")
        void it_returns_exists() {
            // given
            Long reviewId = 1L;
            Long memberId = 1L;
            boolean exists = true;
            when(reviewJpaRepository.existsByIdAndMemberId(reviewId, memberId)).thenReturn(exists);

            // when
            boolean result = reviewCoreRepository.existsByIdAndMemberId(reviewId, memberId);

            // then
            assertThat(result).isTrue();
        }
    }
}