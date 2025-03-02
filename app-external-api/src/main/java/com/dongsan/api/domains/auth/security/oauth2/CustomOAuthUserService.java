package com.dongsan.api.domains.auth.security.oauth2;

import com.dongsan.api.domains.auth.security.handler.CustomAccessDeniedHandler;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberRole;
import com.dongsan.core.domains.member.MemberService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuthUserService extends DefaultOAuth2UserService {
    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    private final MemberService memberService;

    public CustomOAuthUserService(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 사용자 정보 조회 <br>
     * - 신규 사용자의 경우 데이터 베이스에 저장
     * @param userRequest the user request
     * @return OAuth2User : SecurityContext 에 사용자 정보 저장
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = SocialType.from(registrationId);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, attributes);

        Member member = memberService.getOptionalMemberByEmail(oAuth2Attributes.email()).orElseGet(() -> {
            log.info("[AUTH] 신규 회원 등록, 이메일 : %s".formatted(oAuth2Attributes.email()));
            return memberService.save(oAuth2Attributes.email(), oAuth2Attributes.nickname(), oAuth2Attributes.profileImage(), MemberRole.ROLE_USER);
        });
        log.info("[AUTH] 로그인 이메일 : %s".formatted(member.email()));

        return new CustomOAuth2User(member);
    }
}
