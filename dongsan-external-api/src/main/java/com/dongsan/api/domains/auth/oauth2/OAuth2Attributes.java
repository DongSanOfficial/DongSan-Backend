package com.dongsan.api.domains.auth.oauth2;

import com.dongsan.core.domains.auth.Provider;

import java.util.Map;

public record OAuth2Attributes(
        String email,
        String nickname
) {

    public static OAuth2Attributes of(Provider socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case KAKAO -> ofKakao(attributes);
            case NAVER -> ofNaver(attributes);
            case APPLE -> ofApple(attributes);
        };
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");
        return new OAuth2Attributes((String) kakaoAccount.get("email"), profile.get("nickname"));
    }

    private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {
        Map<String, String> response = (Map<String, String>) attributes.get("response");
        return new OAuth2Attributes(response.get("email"), response.get("nickname"));
    }

    private static OAuth2Attributes ofApple(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        return new OAuth2Attributes(email, email);
    }
}
