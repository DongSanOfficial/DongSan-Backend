package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CapacityTest {
    @Test
    @DisplayName("limitEnable=true 이면서 memberLimit이 null이면 예외가 발생한다")
    void shouldThrowException_whenLimitEnabledAndMemberLimitIsNull() {
        assertThatThrownBy(() -> new Capacity(true, null))
                .isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("limitEnable=true 이면서 memberLimit이 2보다 작으면 예외가 발생한다")
    void shouldThrowException_whenMemberLimitLt2() {
        assertThatThrownBy(() -> new Capacity(true, 1))
                .isInstanceOf(CoreException.class);
    }

    @Test
    @DisplayName("limitEnable=true 이면서 memberLimit이 100보다 크면 예외가 발생한다")
    void shouldThrowException_whenMemberLimitGt100() {
        assertThatThrownBy(() -> new Capacity(true, 101))
                .isInstanceOf(CoreException.class);
    }
    
}
