package com.dongsan.domain.domains.crew;

import com.dongsan.domain.domains.crew.service.BCryptPasswordHasher;
import com.dongsan.domain.support.error.CoreException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BCryptPasswordHasherTest {
    BCryptPasswordHasher hasher;

    @BeforeEach
    void setUp() {
        hasher = new BCryptPasswordHasher();
    }

    @Test
    @DisplayName("비밀번호가 null이면 예외가 발생한다.")
    void shouldThrowException_whenPasswordIsNull() {
        Assertions.assertThatThrownBy(() -> hasher.hash(null))
                .isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("비밀번호가 8자 미만이면 예외가 발생한다")
    void shouldThrowException_whenPasswordLt8() {
        String input = "a".repeat(7);
        Assertions.assertThatThrownBy(() -> hasher.hash(input))
                .isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("비밀번호가 20자 초과이면 예외가 발생한다")
    void shouldThrowException_whenPasswordGt20() {
        String input = "a".repeat(21);
        Assertions.assertThatThrownBy(() -> hasher.hash(input))
                .isInstanceOf(CoreException.class);
    }

}
