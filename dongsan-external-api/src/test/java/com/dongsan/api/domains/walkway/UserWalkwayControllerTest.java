//package com.dongsan.api.domains.walkway;
//
//import com.dongsan.api.domains.auth.AuthUserDto;
//import com.dongsan.api.domains.auth.CustomAuthUser;
//import com.dongsan.api.domains.auth.oauth2.CustomRequestEntityConverter;
//import com.dongsan.core.domains.member.Member;
//import com.dongsan.core.domains.walkway.Walkway;
//import com.dongsan.core.domains.walkway.WalkwayHistory;
//import com.dongsan.core.domains.walkway.WalkwayService;
//import com.dongsan.core.support.util.PagingResponse;
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
//import java.util.List;
//
//import static member.MemberFixture.createMember;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static walkway.WalkwayFixture.createWalkwayHistory;
//import static walkway.WalkwayFixture.createWalkwayWithId;
//
//@WebMvcTest(UserWalkwayController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//@DisplayName("UserWalkwayController Unit Test")
//class UserWalkwayControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//    @MockBean
//    CustomRequestEntityConverter customRequestEntityConverter;
//    @MockBean
//    WalkwayService walkwayService;
//
//    final Member member = createMember();
//    final CustomAuthUser customOAuth2User = new CustomAuthUser(new AuthUserDto(member));
//
//    @BeforeEach
//    void setUp_Authentication() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
//        context.setAuthentication(authentication);
//    }
//
//    @Nested
//    @DisplayName("getUserUploadWalkway 메서드는")
//    class Describe_getUserUploadWalkway {
//        @Test
//        @DisplayName("산책로가 존재하면 산책로를 반환한다.")
//        void it_returns_walkways() throws Exception {
//            // given
//            Integer size = 5;
//            Long walkwayId = 1L;
//            List<Walkway> walkways = List.of(
//                    createWalkwayWithId(1L),
//                    createWalkwayWithId(2L),
//                    createWalkwayWithId(3L));
//            PagingResponse<Walkway> walkwayPagingResponse = PagingResponse.from(walkways, size);
//            when(walkwayService.getUserWalkway(customOAuth2User.getMemberId(), size, walkwayId)).thenReturn(
//                    walkwayPagingResponse);
//
//            // when & then
//            mockMvc.perform(get("/users/walkways/upload")
//                            .param("size", String.valueOf(size))
//                            .param("lastId", String.valueOf(walkwayId))
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data", hasSize(walkways.size())))
//                    .andExpect(jsonPath("$.data[0].name", is(walkways.get(0)
//                            .name())))
//                    .andExpect(jsonPath("$.data[0].hashtags", hasSize(walkways.get(0)
//                            .hashtags()
//                            .size())))
//                    .andReturn();
//        }
//
//    }
//
//    @Nested
//    @DisplayName("getUserLikedWalkway 메서드는")
//    class Describe_getUserLikedWalkway {
//        @Test
//        @DisplayName("walkwayId가 존재하면 산책로를 반환한다.")
//        void it_returns_walkways() throws Exception {
//            // given
//            Integer size = 5;
//            Long walkwayId = 1L;
//            List<Walkway> walkways = List.of(
//                    createWalkwayWithId(1L),
//                    createWalkwayWithId(2L),
//                    createWalkwayWithId(3L));
//            PagingResponse<Walkway> walkwayPagingResponse = PagingResponse.from(walkways, size);
//            when(walkwayService.getUserLikedWalkway(customOAuth2User.getMemberId(), size, walkwayId)).thenReturn(
//                    walkwayPagingResponse);
//
//            // when & then
//            mockMvc.perform(get("/users/walkways/like")
//                            .param("size", String.valueOf(size))
//                            .param("lastId", String.valueOf(walkwayId))
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data", hasSize(walkways.size())))
//                    .andExpect(jsonPath("$.data[0].name", is(walkways.get(0)
//                            .name())))
//                    .andExpect(jsonPath("$.data[0].hashtags", hasSize(walkways.get(0)
//                            .hashtags()
//                            .size())))
//                    .andReturn();
//        }
//
//    }
//
//    @Nested
//    @DisplayName("getUserWalkwayHistory 메서드는")
//    class Describe_getUserWalkwayHistory {
//
//        @Test
//        @DisplayName("리뷰 가능한 산책로 이용 기록을 반환한다.")
//        void it_returns_walkway_histories() throws Exception {
//            // Given
//            int size = 10;
//            Long lastId = 1L;
//            Long memberId = member.id();
//
//            List<WalkwayHistory> histories = List.of(createWalkwayHistory());
//            PagingResponse<WalkwayHistory> walkwayHistoryPagingResponse = PagingResponse.from(histories, size);
//            when(walkwayService.getUserCanReviewWalkwayHistory(memberId, lastId, size)).thenReturn(
//                    walkwayHistoryPagingResponse);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/users/walkways/history")
//                    .param("size", String.valueOf(size))
//                    .param("lastId", String.valueOf(lastId))
//                    .contentType(MediaType.APPLICATION_JSON));
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.size()").value(histories.size()));
//        }
//    }
//}
