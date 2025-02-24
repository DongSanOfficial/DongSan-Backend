//package com.dongsan.domains.user.controller;
//
//import static fixture.MemberFixture.createMemberWithId;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
//import com.dongsan.core.domains.review.ReviewReader;
//import com.dongsan.api.domains.review.GetReviewResponse;
//import com.dongsan.core.domains.review.UserReviewService;
//import java.util.Collections;
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
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(UserReviewController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//@DisplayName("UserReviewController Unit Test")
//class ReviewControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//    @MockBean
//    UserReviewService userReviewService;
//    @MockBean
//    ReviewReader reviewReader;
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
//
//    @Nested
//    @DisplayName("getReviews 메소드는")
//    class Describe_getReviews{
//
//        @Test
//        @DisplayName("리뷰를 생성 날짜를 기준으로 내림차순으로 조회한다.")
//        void it_returns_reviews() throws Exception{
//            // given
//            int size = 5;
//            long lastId = 10L;
//
//            GetReviewResponse.ReviewInfo reviewInfo = new GetReviewResponse.ReviewInfo(
//                    1L, 10L, "Sample Walkway", "2024-10-10", 5, "This is a great walkway!");
//            GetReviewResponse response = GetReviewResponse.builder()
//                    .reviews(Collections.singletonList(reviewInfo))
//                    .hasNext(false)
//                    .build();
//            when(reviewReader.existsByReviewId(lastId)).thenReturn(true);
//            when(userReviewService.getReviews(size, lastId, customOAuth2User.getMemberId())).thenReturn(response);
//
//            // when & then
//            mockMvc.perform(get("/users/reviews")
//                            .param("size", String.valueOf(size))
//                            .param("lastId", String.valueOf(lastId))
//                            .contentType("application/json;charset=UTF-8")
//                    )
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.reviews", hasSize(1)))
//                    .andExpect(jsonPath("$.data.hasNext", is(false)))
//                    .andDo(print())
//                    .andReturn();
//        }
//
//    }
//
//}