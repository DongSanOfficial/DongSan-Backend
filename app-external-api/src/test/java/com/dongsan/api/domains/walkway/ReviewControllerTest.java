//package com.dongsan.api.domains.walkway;
//
//import static fixture.MemberFixture.createMemberWithId;
//import static fixture.ReviewFixture.createReviewWithId;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.domains.review.dto.RatingCount;
//import com.dongsan.domains.review.entity.Review;
//import com.dongsan.domains.walkway.controller.dto.request.CreateReviewRequest;
//import com.dongsan.domains.walkway.controller.dto.response.CreateReviewResponse;
//import com.dongsan.domains.walkway.controller.dto.response.GetWalkwayRatingResponse;
//import com.dongsan.domains.walkway.controller.dto.response.GetWalkwayReviewsResponse;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.core.domains.review.ReviewMapper;
//import com.dongsan.core.domains.walkway.WalkwayReader;
//import com.dongsan.core.domains.review.ReviewService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import fixture.WalkwayFixture;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//@WebMvcTest(ReviewController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ReviewController Unit Test")
//class ReviewControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    ObjectMapper objectMapper;
//    @MockBean
//    WalkwayReader walkwayQueryService;
//    @MockBean
//    ReviewService reviewService;
//    final Member member = createMemberWithId(1L);
//    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);
//
//    @BeforeEach
//    void setUp_Authentication(){
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
//        context.setAuthentication(authentication);
//    }
//
//    @Nested
//    @DisplayName("createReview 메서드는")
//    class Describe_createReviewEntity {
//        @Test
//        @DisplayName("리뷰 작성에 성공하면 작성한 리뷰의 ID를 반환한다.")
//        void it_returns_reviewId() throws Exception {
//            // Given
//            Long walkwayId = 1L;
//            Integer rating = 5;
//            CreateReviewRequest createReviewRequest = new CreateReviewRequest(1L, rating, "test content");
//
//            CreateReviewResponse createReviewResponse = CreateReviewResponse.builder()
//                    .reviewId(1L)
//                    .build();
//
//            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
//            when(reviewService.createReview(customOAuth2User.getMemberId(), walkwayId, createReviewRequest))
//                    .thenReturn(createReviewResponse);
//
//            // When
//            ResultActions response = mockMvc.perform(post("/walkways/1/review")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(createReviewRequest)));
//
//            // Then
//            response.andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.data.reviewId").value(1L));
//        }
//
//    }
//
//    @Nested
//    @DisplayName("getWalkwayReviews 메서드는")
//    class Describe_getWalkwayReviewsEntity {
//        @Test
//        @DisplayName("리뷰 리스트를 반환한다.")
//        void it_returns_review_list() throws Exception {
//            // Given
//            String type = "latest";
//            Long walkwayId = 1L;
//            Long lastId = null;
//            Integer size = 10;
//
//            Walkway walkway = WalkwayFixture.createWalkway(member);
//            List<Review> reviews = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                reviews.add(createReviewWithId(1L, member, walkway));
//            }
//
//            GetWalkwayReviewsResponse getWalkwayReviewsResponse = ReviewMapper.toGetWalkwayReviewsResponse(reviews, size);
//            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
//            when(reviewService.getWalkwayReviews(type, lastId, walkwayId, size, member.getId()))
//                    .thenReturn(getWalkwayReviewsResponse);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/walkways/1/review/content")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .param("sort", type));
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.reviews").isNotEmpty())
//                    .andExpect(jsonPath("$.data.reviews").isArray())
//                    .andExpect(jsonPath("$.data.reviews.size()").value(5));
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkwaysRating 메서드는")
//    class Describe_getWalkwaysRating {
//
//        @Test
//        @DisplayName("산책로 별점 내용을 반환한다.")
//        void it_returns_walkway_rating_info() throws Exception {
//            // Given
//            Long walkwayId = 1L;
//            Walkway walkway = WalkwayFixture.createWalkway(null);
//
//            List<RatingCount> ratingCounts = new ArrayList<>();
//            for(Integer i = 1; i <= 5; i++) {
//                ratingCounts.add(new RatingCount(i, 10L));
//            }
//
//            GetWalkwayRatingResponse getWalkwayRatingResponse = ReviewMapper.toGetWalkwayRatingResponse(ratingCounts, walkway);
//            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
//            when(reviewService.getWalkwayRating(walkwayId, member.getId())).thenReturn(getWalkwayRatingResponse);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/walkways/1/review/rating")
//                    .contentType(MediaType.APPLICATION_JSON));
//
//            response.andExpect(jsonPath("$.data.five").value(20L))
//                    .andExpect(jsonPath("$.data.four").value(20L))
//                    .andExpect(jsonPath("$.data.three").value(20L))
//                    .andExpect(jsonPath("$.data.two").value(20L))
//                    .andExpect(jsonPath("$.data.one").value(20L));
//        }
//    }
//}