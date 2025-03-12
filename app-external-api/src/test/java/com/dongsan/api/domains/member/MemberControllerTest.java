package com.dongsan.api.domains.member;

import static member.MemberFixture.createMember;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.api.domains.auth.AuthUserDto;
import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserProfileController Unit Test")
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    Member member;

    @BeforeEach
    void setUp(){
        member = createMember();
        CustomAuthUser customOAuth2User = new CustomAuthUser(new AuthUserDto(member));
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Nested
    @DisplayName("getUserProfile 메서드는")
    class Describe_getUserProfile {

        @Test
        @DisplayName("유저가 존재하면 프로필을 조회한다.")
        void it_returns_userProfile() throws Exception {
            // given
            when(memberService.getMember(member.id())).thenReturn(member);
            MemberProfileResponse response = new MemberProfileResponse(member);

            // when & then
            mockMvc.perform(get("/users/profile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.profileImageUrl").value(response.profileImageUrl()))
                    .andExpect(jsonPath("$.email").value(response.email()))
                    .andExpect(jsonPath("$.nickname").value(response.nickname()))
                    .andReturn();
        }

    }
}