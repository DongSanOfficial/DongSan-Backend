package com.dongsan.core.domains.auth;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;

public enum Provider {
    KAKAO("kakao"),
    NAVER("naver"),
    ;

    private final String registrationId;

    Provider(String registrationId) {
        this.registrationId = registrationId;
    }

    public static Provider of(String registrationId){
        registrationId = registrationId.toLowerCase();
        for(Provider provider: Provider.values()){
            if(provider.getRegistrationId().equals(registrationId)){
                return provider;
            }
        }
        throw new CoreException(CoreErrorCode.PROVIDER_NOT_FOUND);
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
