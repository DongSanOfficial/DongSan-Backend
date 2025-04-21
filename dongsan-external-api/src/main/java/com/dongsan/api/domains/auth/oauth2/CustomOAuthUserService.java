package com.dongsan.api.domains.auth.oauth2;

import com.dongsan.api.domains.auth.AuthUserDto;
import com.dongsan.api.domains.auth.CustomAccessDeniedHandler;
import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.core.domains.auth.Provider;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberRole;
import com.dongsan.core.domains.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
     *
     * @param userRequest the user request
     * @return OAuth2User : SecurityContext 에 사용자 정보 저장
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2Attributes oAuth2Attributes;
        OAuth2User oAuth2User;

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();
        Provider provider = Provider.of(registrationId);
        if (registrationId.equals("apple")) {
            String idToken = userRequest.getAdditionalParameters().get("id_token").toString();
            oAuth2Attributes = OAuth2Attributes.of(provider, decodeJwtTokenPayload(idToken));
        } else {
            oAuth2User = super.loadUser(userRequest);
            Map<String, Object> attributes = oAuth2User.getAttributes();
            oAuth2Attributes = OAuth2Attributes.of(provider, attributes);
        }

        Member member = memberService.getOptionalMemberByEmailAndProvider(oAuth2Attributes.email(), provider)
                .orElseGet(() -> memberService.save(oAuth2Attributes.email(), oAuth2Attributes.nickname(),
                        null, MemberRole.ROLE_USER, provider));
        log.info("[AUTH] 로그인 이메일 : %s".formatted(member.email()));
        AuthUserDto user = new AuthUserDto(member);
        return new CustomAuthUser(user);
    }

    public Map<String, Object> decodeJwtTokenPayload(String jwtToken) {
        Map<String, Object> jwtClaims = new HashMap<>();
        try {
            String[] parts = jwtToken.split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();

            byte[] decodedBytes = decoder.decode(parts[1].getBytes(StandardCharsets.UTF_8));
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(decodedString, Map.class);
            jwtClaims.putAll(map);
            log.info("[APPLE] token : {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jwtClaims));

        } catch (JsonProcessingException e) {
            log.error("[APPLE] decodeJwtToken: {}-{} / jwtToken : {}", e.getMessage(), e.getCause(), jwtToken);
        }
        return jwtClaims;
    }
}
