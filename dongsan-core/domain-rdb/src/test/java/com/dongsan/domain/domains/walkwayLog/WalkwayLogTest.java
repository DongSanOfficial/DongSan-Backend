package com.dongsan.domain.domains.walkwayLog;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WalkwayLogTest {
    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    @DisplayName("시간이 0초보다 작으면 예외가 발생한다")
    void shouldThrowException_whenTimeIsInvalid(int invalidTime) {
        Long memberId = 1L;
        Long walkwayId = 10L;
        Double distance = 1.5;

        assertThatThrownBy(() -> new WalkwayLog(memberId, walkwayId, invalidTime, distance))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.WALKWAY_HISTORY_TIME_NOT_ENOUGH);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -2.0})
    @DisplayName("거리가 0.0보다 작으면 예외가 발생한다")
    void shouldThrowException_whenDistanceIsInvalid(double invalidDistance) {
        Long memberId = 1L;
        Long walkwayId = 10L;
        Integer time = 100;

        assertThatThrownBy(() -> new WalkwayLog(memberId, walkwayId, time, invalidDistance))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.WALKWAY_HISTORY_DISTANCE_NOT_ENOUGH);
    }

    @Test
    @DisplayName("memberId가 다르면 validateRelation() 호출 시 예외가 발생한다")
    void shouldThrowException_whenMemberIdMismatch() {
        Long memberId = 1L;
        Long walkwayId = 10L;
        WalkwayLog log = new WalkwayLog(memberId, walkwayId, 100, 1.5);
        Long anotherMemberId = 2L;

        assertThatThrownBy(() -> log.validateRelation(anotherMemberId, walkwayId))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.INVALID_OWNER_AND_WALKWAY);
    }

    @Test
    @DisplayName("walkwayId가 다르면 validateRelation() 호출 시 예외가 발생한다")
    void shouldThrowException_whenWalkwayIdMismatch() {
        Long memberId = 1L;
        Long walkwayId = 10L;
        WalkwayLog log = new WalkwayLog(memberId, walkwayId, 100, 1.5);
        Long anotherWalkwayId = 20L;

        assertThatThrownBy(() -> log.validateRelation(memberId, anotherWalkwayId))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.INVALID_OWNER_AND_WALKWAY);
    }

    @ParameterizedTest
    @CsvSource(value = {"3.0, 2.0, true", "3.0, 1.99, false"})
    @DisplayName("산책로 거리의 2/3 이상을 걸었는지 확인할 수 있다")
    void shouldReturnCorrectBoolean_whenCheckingSufficientDistance(double walkwayDistance, double logDistance, boolean expected) {
        WalkwayLog log = new WalkwayLog(1L, 10L, 100, logDistance);

        assertThat(log.isSufficientDistance(walkwayDistance)).isEqualTo(expected);
    }

    @Test
    @DisplayName("기록한 거리가 산책로 거리의 2/3 미만이면 예외가 발생한다")
    void shouldThrowException_whenDistanceIsNotSufficient() {
        WalkwayLog log = new WalkwayLog(1L, 10L, 100, 1.99);
        double walkwayDistance = 3.0;

        assertThatThrownBy(() -> log.validateSufficientDistance(walkwayDistance))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.NOT_ENOUGH_DISTANCE);
    }

}
