//package com.dongsan.domains.review.repository;
//
//import static fixture.MemberFixture.createMember;
//import static fixture.ReviewFixture.createReview;
//import static fixture.WalkwayFixture.createPrivateWalkway;
//import static fixture.WalkwayFixture.createWalkway;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.member.MemberEntity;
//import com.dongsan.rdb.domains.review.RatingCount;
//import com.dongsan.rdb.domains.review.ReviewEntity;
//import com.dongsan.rdb.domains.walkway.WalkwayEntity;
//import com.dongsan.rdb.domains.walkway.enums.ExposeLevel;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//
//@DisplayName("ReviewQueryDSLRepository Unit Test")
//class ReviewEntityQueryDSLRepositoryTest extends RepositoryTest {
//    @Autowired
//    TestEntityManager em;
//
//    @Autowired
//    ReviewQueryDSLRepository reviewQueryDSLRepository;
//
//    @Nested
//    @DisplayName("getUserReviews 메소드는")
//    class Describe_getUserReviews{
//        MemberEntity memberEntity;
//        WalkwayEntity walkwayEntity;
//        List<ReviewEntity> reviewEntities = new ArrayList<>();
//
//        @BeforeEach
//        void setUp(){
//            memberEntity = createMember();
//            walkwayEntity = createWalkway(memberEntity);
//            em.persist(memberEntity);
//            em.persist(walkwayEntity);
//            for(int i =0; i<6; i++){
//                ReviewEntity reviewEntity = createReview(memberEntity, walkwayEntity);
//                reviewEntities.add(reviewEntity);
//                em.persist(reviewEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("타인이 등록한 산책로이면 공개 상태의 산책로의 리뷰만 조회한다.")
//        void it_returns_others_public_walkway_review(){
//            // given
//            MemberEntity other = createMember();
//            WalkwayEntity otherPublicWalkwayEntity = createWalkway(other);
//            WalkwayEntity otherPrivateWalkwayEntity = createPrivateWalkway(other);
//            em.persist(other);
//            em.persist(otherPublicWalkwayEntity);
//            em.persist(otherPrivateWalkwayEntity);
//            em.persist(createReview(memberEntity, otherPublicWalkwayEntity));
//            em.persist(createReview(memberEntity, otherPrivateWalkwayEntity));
//            Integer size = 10;
//            LocalDateTime lastCreateAt = null;
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<ReviewEntity> result = reviewQueryDSLRepository.getUserReviews(size, lastCreateAt, memberId);
//
//            // then
//            assertThat(result).hasSize(reviewEntities.size() + 1);
//            for(int i=0; i< result.size(); i++){
//                ReviewEntity reviewEntity = result.get(i);
//                // 타인이 등록한 산책로이면
//                if(!reviewEntity.getWalkwayEntity().getMemberEntity().getId().equals(memberEntity.getId())){
//                    assertThat(reviewEntity.getWalkwayEntity().getExposeLevel().equals(ExposeLevel.PUBLIC));
//                }
//            }
//        }
//
//
//        @Test
//        @DisplayName("lastCreateAt가 null이면 가장 최근의 review들을 내림차순으로 가져온다.")
//        void it_returns_most_recent_reviews(){
//            // given
//            Integer limit = 5;
//            LocalDateTime lastCreateAt = null;
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<ReviewEntity> result = reviewQueryDSLRepository.getUserReviews(limit, lastCreateAt, memberId);
//
//            // then
//            assertThat(result).hasSize(5);
//            for(int i=1; i<result.size(); i++){
//                LocalDateTime after = result.get(i - 1).getCreatedAt();
//                LocalDateTime prev = result.get(i).getCreatedAt();
//                assertThat(prev).isBeforeOrEqualTo(after);
//            }
//        }
//
//        @Test
//        @DisplayName("lastCreateAt가 null이 아니면 lastCreateAt 보다 createdAt가 더 작은(더 일찍 작성함) review를 작성날짜 기준 내림차순으로 가져온다.")
//        void it_returns_next_reviews(){
//            // given
//            Integer limit = 5;
//            LocalDateTime lastCreateAt = LocalDateTime.now().minusSeconds(1L);
//            Long memberId = memberEntity.getId();
//
//            // when
//            List<ReviewEntity> result = reviewQueryDSLRepository.getUserReviews(limit, lastCreateAt, memberId);
//
//            // then
//            for (ReviewEntity reviewEntity : result) {
//                assertThat(reviewEntity.getMemberEntity()
//                        .getId()).isEqualTo(memberId);
//                assertThat(reviewEntity.getCreatedAt()).isBefore(lastCreateAt);
//            }
//            for(int i=1; i<result.size(); i++){
//                LocalDateTime after = result.get(i - 1).getCreatedAt();
//                LocalDateTime prev = result.get(i).getCreatedAt();
//                assertThat(prev).isBeforeOrEqualTo(after);
//            }
//
//        }
//
//
//    }
//
//    @Nested
//    @DisplayName("getWalkwayReviewsLatest 메서드는")
//    class Describe_getWalkwayReviewsLatestEntity {
//        MemberEntity memberEntity;
//        WalkwayEntity walkwayEntity;
//        List<ReviewEntity> reviewEntities = new ArrayList<>();
//        @BeforeEach
//        void setUp(){
//            memberEntity = createMember();
//            walkwayEntity = createWalkway(memberEntity);
//            em.persist(memberEntity);
//            em.persist(walkwayEntity);
//            for(int i =0; i<6; i++){
//                ReviewEntity reviewEntity = createReview(memberEntity, walkwayEntity);
//                reviewEntities.add(reviewEntity);
//                em.persist(reviewEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("리뷰 리스트를 시간순으로 반환한다.")
//        void it_returns_review_list_latest() {
//            // Given
//            Integer limit = 5;
//            Long walkwayId = walkwayEntity.getId();
//
//            // When
//            List<ReviewEntity> result = reviewQueryDSLRepository.getWalkwayReviewsLatest(limit, walkwayId, null);
//
//            // Then
//            Long beforeId = result.get(0).getId();
//            for(int count = 1; count < 5; count++) {
//                Long currentId = result.get(count).getId();
//                assertThat(currentId).isLessThan(beforeId);
//                beforeId = currentId;
//            }
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkwayReviewsRating 메서드는")
//    class Describe_getWalkwayReviewsRatingEntity {
//        MemberEntity memberEntity;
//        WalkwayEntity walkwayEntity;
//        List<ReviewEntity> reviewEntities = new ArrayList<>();
//        @BeforeEach
//        void setUp(){
//            memberEntity = createMember();
//            walkwayEntity = createWalkway(memberEntity);
//            em.persist(memberEntity);
//            em.persist(walkwayEntity);
//            for(Integer i = 0; i<6; i++){
//                ReviewEntity reviewEntity = createReview(memberEntity, walkwayEntity, i, "test");
//                reviewEntities.add(reviewEntity);
//                em.persist(reviewEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("리뷰 리스트를 별점순으로 반환한다.")
//        void it_returns_review_list_rating() {
//            // Given
//            Integer limit = 5;
//            Long walkwayId = walkwayEntity.getId();
//
//            // When
//            List<ReviewEntity> result = reviewQueryDSLRepository.getWalkwayReviewsRating(limit, walkwayId, null);
//
//            // Then
//            Integer beforeRating = result.get(0).getRating();
//            for(int count = 1; count < 5; count++) {
//                Integer currentRating = result.get(count).getRating();
//                assertThat(currentRating).isLessThan(beforeRating);
//                beforeRating = currentRating;
//            }
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkwaysRating 메서드는")
//    class Describe_getWalkwaysRating {
//        MemberEntity memberEntity;
//        WalkwayEntity walkwayEntity;
//        List<ReviewEntity> reviewEntities = new ArrayList<>();
//        @BeforeEach
//        void setUp(){
//            memberEntity = createMember();
//            walkwayEntity = createWalkway(memberEntity);
//            em.persist(memberEntity);
//            em.persist(walkwayEntity);
//            for(Integer i = 1; i<=5; i++){
//                ReviewEntity reviewEntity = createReview(memberEntity, walkwayEntity, i, "test");
//                reviewEntities.add(reviewEntity);
//                em.persist(reviewEntity);
//            }
//        }
//
//        @Test
//        @DisplayName("각 별점의 개수를 저장한 튜플 리스트를 반환한다.")
//        void it_returns_rating_tuple_list() {
//            // When
//            List<RatingCount> result = reviewQueryDSLRepository.getWalkwaysRating(walkwayEntity.getId());
//
//            // Then
//            for(RatingCount ratingCount : result) {
//                assertThat(ratingCount.count()).isEqualTo(1);
//            }
//        }
//    }
//
//}