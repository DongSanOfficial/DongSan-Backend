package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CrewAccessPolicyTest {
    @Test
    @DisplayName("비밀번호가 null이면 예외가 발생한다")
    void shouldThrowException_whenPasswordIsNull() {
        assertThatThrownBy(() -> CrewAccessPolicy.privateCrew(null))
                .isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("비밀번호가 빈 문자열이면 예외가 발생한다")
    void shouldThrowException_whenPasswordIsBlank() {
        assertThatThrownBy(() -> CrewAccessPolicy.privateCrew("    "))
                .isInstanceOf(CoreException.class);
    }

}
