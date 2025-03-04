package com.dongsan.api.domains.review;

import static review.ReviewFixture.createReviewWithId;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.review.CreateReview;
import com.dongsan.core.domains.review.Rating;
import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.ReviewService;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import member.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewController Unit Test")
class ReviewControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ReviewService reviewService;
    final Member member = MemberFixture.createMember();
    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

    @BeforeEach
    void setUp_Authentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        context.setAuthentication(authentication);
    }

    @Nested
    @DisplayName("createReview 메서드는")
    class Describe_createReviewEntity {
        @Test
        @DisplayName("리뷰 작성에 성공하면 작성한 리뷰의 ID를 반환한다.")
        void it_returns_reviewId() throws Exception {
            // Given
            Long walkwayId = 1L;
            Long reviewId = 1L;
            Integer rating = 5;
            CreateReviewRequest request = new CreateReviewRequest(1L, rating, "test content");
            CreateReview createReview = new CreateReview(customOAuth2User.getMemberId(), walkwayId, request.walkwayHistoryId(), Rating.numOf(request.rating()), request.content());

            when(reviewService.createReview(createReview)).thenReturn(reviewId);

            // When
            ResultActions response = mockMvc.perform(post("/walkways/1/review")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.reviewId").value(reviewId));
        }
    }

    @Nested
    @DisplayName("getWalkwayReviews 메서드는")
    class Describe_getWalkwayReviewsEntity {
        @Test
        @DisplayName("리뷰 리스트를 반환한다.")
        void it_returns_review_list() throws Exception {
            // Given
            String type = "latest";
            Long walkwayId = 1L;
            Long lastId = null;
            Integer size = 10;

            List<Review> reviews = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                reviews.add(createReviewWithId(1L, member.id(), walkwayId));
            }
            PagingResponse<Review> cursorPagingResponse = PagingResponse.from(reviews, size);

            when(reviewService.getWalkwayReviews(type, walkwayId, member.id(), new CursorRequest(lastId, size)))
                    .thenReturn(cursorPagingResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/1/review/content")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("sort", type));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.size()").value(5));
        }
    }

    @Nested
    @DisplayName("getWalkwaysRating 메서드는")
    class Describe_getWalkwaysRating {
        @Test
        @DisplayName("산책로 별점 내용을 반환한다.")
        void it_returns_walkway_rating_info() throws Exception {
            // Given
            Long walkwayId = 1L;

            Map<Rating, Long> ratingCounts = new EnumMap<>(Rating.class);
            for(Integer i = 1; i <= 5; i++) {
                ratingCounts.put(Rating.numOf(i), 10L);
            }

            when(reviewService.getWalkwayRating(walkwayId, member.id())).thenReturn(ratingCounts);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/1/review/rating")
                    .contentType(MediaType.APPLICATION_JSON));

            response.andExpect(jsonPath("$.five").value(20L))
                    .andExpect(jsonPath("$.four").value(20L))
                    .andExpect(jsonPath("$.three").value(20L))
                    .andExpect(jsonPath("$.two").value(20L))
                    .andExpect(jsonPath("$.one").value(20L));
        }
    }
}