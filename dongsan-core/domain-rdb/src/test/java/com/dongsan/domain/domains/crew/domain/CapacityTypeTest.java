package com.dongsan.domain.domains.crew.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CapacityTypeTest {
    @Test
    @DisplayName("LIMITED는 isLimitedCrew() = true 이다")
    void shouldReturnTrue_whenLIMITED() {
        CapacityType limitedType = CapacityType.LIMITED;

        assertThat(limitedType.isLimitedCrew()).isTrue();
    }

    @Test
    @DisplayName("UNLIMITED는 isLimitedCrew() = false 이다")
    void shouldReturnFalse_whenUNLIMITED() {
        CapacityType unlimitedType = CapacityType.UNLIMITED;

        assertThat(unlimitedType.isLimitedCrew()).isFalse();
    }

}
