package com.dongsan.rdb.domains.auth;

import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;

public enum Provider {
    KAKAO("kakao"),
    NAVER("naver"),
    APPLE("apple");

    private final String registrationId;

    Provider(String registrationId) {
        this.registrationId = registrationId;
    }

    public static Provider of(String registrationId) {
        registrationId = registrationId.toLowerCase();
        for (Provider provider : Provider.values()) {
            if (provider.getRegistrationId()
                    .equals(registrationId)) {
                return provider;
            }
        }
        throw new CoreException(CoreErrorCode.PROVIDER_NOT_FOUND);
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
