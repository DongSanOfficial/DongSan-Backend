package com.dongsan.api.domains.dev;

import com.dongsan.api.domains.auth.CookieService;
import com.dongsan.api.domains.auth.JwtService;
import com.dongsan.core.domains.auth.TokenWriter;
import com.dongsan.core.domains.member.MemberReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DevUseCaseTest Unit Test")
class DevUseCaseTest {
    @InjectMocks
    DevService devUseCase;
    @Mock
    MemberReader memberReader;
    @Mock
    JwtService jwtService;
    @Mock
    TokenWriter authWriter;
    @Mock
    CookieService cookieService;


//    @Nested
//    @DisplayName("generateToken 메소드는")
//    class Describe_generateToken{
//        @Test
//        @DisplayName("사용자가 존재하면 token을 DTO로 변환한다.")
//        void it_returns_responseDTO(){
//            // given
//            Long memberId = 1L;
//            String accessToken = "ac_t";
//            String refreshToken = "rf_t";
//            when(jwtService.createAccessToken(memberId)).thenReturn(accessToken);
//            when(jwtService.createRefreshToken(memberId)).thenReturn(refreshToken);
//            doNothing().when(authService).saveRefreshToken(memberId, refreshToken);
//            MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
//            MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
//            Cookie atCookie = new Cookie("accessToken", accessToken);
//            Cookie rtCookie = new Cookie("refreshToken", refreshToken);
//            when(cookieService.createAccessTokenCookie(accessToken, httpServletRequest)).thenReturn(atCookie);
//            when(cookieService.createRefreshTokenCookie(refreshToken, httpServletRequest)).thenReturn(rtCookie);
//
//            // when
//            devUseCase.generateToken(memberId, httpServletRequest, httpServletResponse);
//
//            // then
//            Cookie[] cookies = httpServletResponse.getCookies();
//            assertThat(cookies).hasSize(2);
//
//            Cookie storedAccessTokenCookie = cookies[0];
//            Cookie storedRefreshTokenCookie = cookies[1];
//
//            assertThat(storedAccessTokenCookie.getName()).isEqualTo("accessToken");
//            assertThat(storedAccessTokenCookie.getValue()).isEqualTo(accessToken);
//            assertThat(storedRefreshTokenCookie.getName()).isEqualTo("refreshToken");
//            assertThat(storedRefreshTokenCookie.getValue()).isEqualTo(refreshToken);
//        }
//    }
//
//    @Nested
//    @DisplayName("getMemberInfo 메소드는")
//    class Describe_getMemberInfoEntity {
//        @Test
//        @DisplayName("accessToken에 사용자가 존재하면 사용자 정보를 DTO로 변환한다.")
//        void it_returns_responseDTO(){
//            // given
//            String accessToken = "ac_t";
//            Member member = MemberFixture.createMemberWithId(1L);
//            when(jwtService.getMemberFromAccessToken(accessToken)).thenReturn(member);
//
//            // when
//            GetMemberInfoResponse response = devUseCase.getMemberInfo(accessToken);
//
//            // then
//            assertThat(response.email()).isEqualTo(member.getEmail());
//            assertThat(response.memberId()).isEqualTo(member.getId());
//        }
//    }

}
//package com.dongsan.domains.dev.usecase;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.domains.auth.AuthService;
//import com.dongsan.domains.auth.service.CookieService;
//import com.dongsan.domains.auth.service.JwtService;
//import com.dongsan.domains.dev.dto.response.GetMemberInfoResponse;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.domains.user.service.MemberQueryService;
//import fixture.MemberFixture;
//import jakarta.servlet.http.Cookie;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockHttpServletResponse;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("DevUseCaseTest Unit Test")
//class DevUseCaseTest {
//    @InjectMocks
//    DevUseCase devUseCase;
//    @Mock
//    MemberQueryService memberQueryService;
//    @Mock
//    JwtService jwtService;
//    @Mock
//    AuthService authService;
//    @Mock
//    CookieService cookieService;
//
//
//    @Nested
//    @DisplayName("generateToken 메소드는")
//    class Describe_generateToken{
//        @Test
//        @DisplayName("사용자가 존재하면 token을 DTO로 변환한다.")
//        void it_returns_responseDTO(){
//            // given
//            Long memberId = 1L;
//            String accessToken = "ac_t";
//            String refreshToken = "rf_t";
//            when(jwtService.createAccessToken(memberId)).thenReturn(accessToken);
//            when(jwtService.createRefreshToken(memberId)).thenReturn(refreshToken);
//            doNothing().when(authService).saveRefreshToken(memberId, refreshToken);
//            MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
//            Cookie atCookie = new Cookie("accessToken", accessToken);
//            Cookie rtCookie = new Cookie("refreshToken", refreshToken);
//            when(cookieService.createAccessTokenCookie(accessToken)).thenReturn(atCookie);
//            when(cookieService.createRefreshTokenCookie(refreshToken)).thenReturn(rtCookie);
//
//            // when
//            devUseCase.generateToken(memberId, httpServletResponse);
//
//            // then
//            Cookie[] cookies = httpServletResponse.getCookies();
//            assertThat(cookies).hasSize(2);
//
//            Cookie storedAccessTokenCookie = cookies[0];
//            Cookie storedRefreshTokenCookie = cookies[1];
//
//            assertThat(storedAccessTokenCookie.getName()).isEqualTo("accessToken");
//            assertThat(storedAccessTokenCookie.getValue()).isEqualTo(accessToken);
//            assertThat(storedRefreshTokenCookie.getName()).isEqualTo("refreshToken");
//            assertThat(storedRefreshTokenCookie.getValue()).isEqualTo(refreshToken);
//        }
//    }
//
//    @Nested
//    @DisplayName("getMemberInfo 메소드는")
//    class Describe_getMemberInfo{
//        @Test
//        @DisplayName("accessToken에 사용자가 존재하면 사용자 정보를 DTO로 변환한다.")
//        void it_returns_responseDTO(){
//            // given
//            String accessToken = "ac_t";
//            Member member = MemberFixture.createMemberWithId(1L);
//            when(jwtService.getMemberFromAccessToken(accessToken)).thenReturn(member);
//
//            // when
//            GetMemberInfoResponse response = devUseCase.getMemberInfo(accessToken);
//
//            // then
//            assertThat(response.email()).isEqualTo(member.getEmail());
//            assertThat(response.memberId()).isEqualTo(member.getId());
//        }
//    }
//
//}
