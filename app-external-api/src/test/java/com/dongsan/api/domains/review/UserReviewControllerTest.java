package com.dongsan.api.domains.review;

import static fixture.ReviewFixture.createReviewWithId;
import static member.MemberFixture.createMember;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.ReviewReader;
import com.dongsan.core.domains.review.UserReviewService;
import com.dongsan.core.support.util.CursorPagingRequest;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("UserReviewController Unit Test")
class UserReviewControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserReviewService userReviewService;
    @MockBean
    ReviewReader reviewReader;
    final Member member = createMember();
    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

    @BeforeEach
    void setUp_Authentication(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        context.setAuthentication(authentication);
    }

    @Nested
    @DisplayName("getReviews 메소드는")
    class Describe_getReviews{
        @Test
        @DisplayName("리뷰를 생성 날짜를 기준으로 내림차순으로 조회한다.")
        void it_returns_reviews() throws Exception{
            // given
            int size = 5;
            long lastId = 10L;
            long walkwayId = 1L;
            List<Review> reviews = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                reviews.add(createReviewWithId(1L, member.id(), walkwayId));
            }
            CursorPagingResponse<Review> cursorPagingResponse = CursorPagingResponse.from(reviews, size);

            when(userReviewService.getReviews(new CursorPagingRequest(lastId, size), customOAuth2User.getMemberId())).thenReturn(cursorPagingResponse);

            // when & then
            mockMvc.perform(get("/users/reviews")
                            .param("size", String.valueOf(size))
                            .param("lastId", String.valueOf(lastId))
                            .contentType("application/json;charset=UTF-8")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.reviews", hasSize(5)))
                    .andExpect(jsonPath("$.data.hasNext", is(false)))
                    .andDo(print())
                    .andReturn();
        }

    }

}