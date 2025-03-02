package com.dongsan.api.domains.auth.security.oauth2;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public enum SocialType {
    KAKAO("kakao"),
    NAVER("naver"),
    //GOOGLE("google")
    ;

    private final String registrationId;

    SocialType(String registrationId) {
        this.registrationId = registrationId;
    }

    public static SocialType from(String registrationId){
        for(SocialType socialType:SocialType.values()){
            if(socialType.getRegistrationId().equals(registrationId)){
                return socialType;
            }
        }
        throw new OAuth2AuthenticationException("존재하지 않는 OAuth registrationId 입니다. : %s".formatted(registrationId));
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
