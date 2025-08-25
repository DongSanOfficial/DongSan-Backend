package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

    @ParameterizedTest
    @CsvSource(value = {"2", "50", "100"})
    @DisplayName("limitEnable=true 이면 memberLimit이 2 이상 100 이하이다")
    void shouldCreateLimitedCrew_whenValidMemberLimit(int memberLimit) {
        Capacity capacity = new Capacity(true, memberLimit);

        assertThat(capacity.isLimitedCrew()).isTrue();
        assertThat(capacity.getMemberLimit()).isEqualTo(memberLimit);
    }

    @Test
    @DisplayName("제한이 없는 크루(UNLIMITED)를 생성할 수 있다")
    void shouldCreateUnlimitedCrew() {
        Capacity capacity = new Capacity(false, null);

        assertThat(capacity.isLimitedCrew()).isFalse();
        assertThat(capacity.getMemberLimit()).isNull();
    }

    @Test
    @DisplayName("크루 정원이 가득 찼을 때 예외가 발생한다")
    void shouldThrowException_whenCrewIsFull() {
        Capacity capacity = new Capacity(true, 5);

        assertThatThrownBy(() -> capacity.validateNotFull(5))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.CREW_MEMBER_FULL);
    }

    @Test
    @DisplayName("정원이 다 차지 않은 상태일 경우 예외가 발생하지 않는다")
    void shouldPassValidation_whenCrewIsNotFull() {
        Capacity capacity = new Capacity(true, 5);

        capacity.validateNotFull(4);
    }

    @Test
    @DisplayName("제한이 있는 크루는 true를 반환한다")
    void shouldReturnTrue_whenCrewIsLimited() {
        Capacity capacity = new Capacity(true, 50);

        assertThat(capacity.isLimitedCrew()).isTrue();
    }

    @Test
    @DisplayName("제한이 없는 크루는 false를 반환한다")
    void shouldReturnFalse_whenCrewIsUnlimited() {
        Capacity capacity = new Capacity(false, null);

        assertThat(capacity.isLimitedCrew()).isFalse();
    }

    @Test
    @DisplayName("제한이 있는 크루는 설정된 memberLimit을 반환한다")
    void shouldReturnMemberLimit_whenCrewIsLimited() {
        int expectedLimit = 50;
        Capacity capacity = new Capacity(true, expectedLimit);

        Integer actualLimit = capacity.getMemberLimit();

        assertThat(actualLimit).isEqualTo(expectedLimit);
    }

    @Test
    @DisplayName("제한이 없는 크루는 null을 반환한다")
    void shouldReturnNull_whenCrewIsUnlimited() {
        Capacity capacity = new Capacity(false, null);

        Integer actualLimit = capacity.getMemberLimit();

        assertThat(actualLimit).isNull();
    }

}
