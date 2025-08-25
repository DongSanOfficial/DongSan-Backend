package com.dongsan.domain.domains.auth;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProviderTest {

    @Test
    @DisplayName("존재하지 않는 registrationId로 Provider를 찾으면 예외가 발생한다.")
    void shouldThrowException_whenProviderNotFound() {
        String invalidRegistrationId = "notValidRegistrationI";

        // when & then
        assertThatThrownBy(() -> Provider.of(invalidRegistrationId))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.PROVIDER_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(strings = {"kakaO", "Naver", "apPle"})
    @DisplayName("대소문자 무관하게 registrationId로 Provider를 찾을 수 있다")
    void shouldReturnProvider_whenValidRegistrationIdIsGiven(String registrationId) {
        // when
        Provider provider = Provider.of(registrationId);

        // then
        assertThat(provider.getRegistrationId()).isEqualTo(registrationId.toLowerCase());
    }

}
