package com.dongsan.api.domains.auth.security.oauth2;

import java.util.Map;

public record OAuth2Attributes(
        String email,
        String nickname,
        String profileImage) {

    public static OAuth2Attributes of(SocialType socialType, Map<String, Object> attributes) {
        return switch (socialType) {
            case KAKAO -> ofKakao(attributes);
            case NAVER -> ofNaver(attributes);
        };
    }

    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");
        return new OAuth2Attributes((String) kakaoAccount.get("email"), profile.get("nickname"),
                profile.get("profile_image_url"));
    }

    private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {
        Map<String, String> response = (Map<String, String>) attributes.get("response");
        return new OAuth2Attributes(response.get("email"), response.get("nickname"), response.get("profile_image"));
    }
}
