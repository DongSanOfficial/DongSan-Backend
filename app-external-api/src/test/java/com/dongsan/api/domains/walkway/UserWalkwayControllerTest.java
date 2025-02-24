//package com.dongsan.api.domains.walkway;
//
//import static fixture.MemberFixture.createMemberWithId;
//import static fixture.WalkwayFixture.createWalkwayWithId;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.dongsan.domains.auth.security.oauth2.dto.CustomOAuth2User;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.domains.user.response.WalkwayListResponse;
//import com.dongsan.domains.user.usecase.UserWalkwayUseCase;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.domains.walkway.entity.WalkwayHistory;
//import com.dongsan.domains.walkway.service.WalkwayQueryService;
//import com.dongsan.domains.walkway.usecase.WalkwayHistoryUseCase;
//import fixture.WalkwayFixture;
//import fixture.WalkwayHistoryFixture;
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
//@WebMvcTest(UserWalkwayController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//@DisplayName("UserWalkwayController Unit Test")
//class UserWalkwayControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//    @MockBean
//    UserWalkwayUseCase userWalkwayUseCase;
//    @MockBean
//    WalkwayQueryService walkwayQueryService;
//    @MockBean
//    WalkwayHistoryUseCase walkwayHistoryUseCase;
//
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
//    @DisplayName("getUserUploadWalkway 메서드는")
//    class Describe_getUserUploadWalkway{
//        @Test
//        @DisplayName("산책로가 존재하면 산책로를 반환한다.")
//        void it_returns_walkways() throws Exception{
//            // given
//            Integer size = 5;
//            Long walkwayId = 1L;
//            List<Walkway> walkways = List.of(
//                    createWalkwayWithId(1L, null),
//                    createWalkwayWithId(2L, null),
//                    createWalkwayWithId(3L, null));
//            WalkwayListResponse response = WalkwayListResponse.from(walkways, false);
//            when(userWalkwayUseCase.getUserUploadWalkway(customOAuth2User.getMemberId(), size, walkwayId)).thenReturn(response);
//
//            // when & then
//            mockMvc.perform(get("/users/walkways/upload")
//                            .param("size", String.valueOf(size))
//                            .param("lastId", String.valueOf(walkwayId))
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.walkways", hasSize(walkways.size())))
//                    .andExpect(jsonPath("$.data.walkways[0].name", is(walkways.get(0).getName())))
//                    .andExpect(jsonPath("$.data.walkways[0].hashtags", hasSize(walkways.get(0).getHashtagWalkways().size())))
//                    .andReturn();
//        }
//
//    }
//
//    @Nested
//    @DisplayName("getUserLikedWalkway 메서드는")
//    class Describe_getUserLikedWalkway{
//        @Test
//        @DisplayName("walkwayId가 존재하면 산책로를 반환한다.")
//        void it_returns_walkways() throws Exception{
//            // given
//            Integer size = 5;
//            Long walkwayId = 1L;
//            List<Walkway> walkways = List.of(
//                    createWalkwayWithId(1L, null),
//                    createWalkwayWithId(2L, null),
//                    createWalkwayWithId(3L, null));
//            WalkwayListResponse response = WalkwayListResponse.from(walkways, false);
//            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
//            when(userWalkwayUseCase.getUserLikedWalkway(customOAuth2User.getMemberId(), size, walkwayId)).thenReturn(response);
//
//            // when & then
//            mockMvc.perform(get("/users/walkways/like")
//                            .param("size", String.valueOf(size))
//                            .param("lastId", String.valueOf(walkwayId))
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.walkways", hasSize(walkways.size())))
//                    .andExpect(jsonPath("$.data.walkways[0].name", is(walkways.get(0).getName())))
//                    .andExpect(jsonPath("$.data.walkways[0].hashtags", hasSize(walkways.get(0).getHashtagWalkways().size())))
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
//            Long memberId = member.getId();
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, null);
//
//            List<WalkwayHistory> histories = List.of(
//                    WalkwayHistoryFixture.createWalkwayHistoryWithId(1L, null, walkway),
//                    WalkwayHistoryFixture.createWalkwayHistoryWithId(2L, null, walkway)
//            );
//
//            when(walkwayHistoryUseCase.getUserWalkwayHistories(memberId, lastId, size)).thenReturn(histories);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/users/walkways/history")
//                            .param("size", String.valueOf(size))
//                            .param("lastId", String.valueOf(lastId))
//                    .contentType(MediaType.APPLICATION_JSON));
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.walkwayHistories.size()").value(2));
//        }
//    }
//}