package com.dongsan.domain.domains.walkway.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import fixture.WalkwayInfoTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class WalkwayInfoTest {
    @Test
    @DisplayName("name이 null 또는 공백이면 예외가 발생한다")
    void shouldThrowException_whenNameIsNullOrBlank() {
        String nullName = null;
        String blankName = "   ";

        assertThatThrownBy(() -> new WalkwayInfoTestBuilder().name(nullName).build())
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.WALKWAY_NAME_NOT_BLANK);

        assertThatThrownBy(() -> new WalkwayInfoTestBuilder().name(blankName).build())
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.WALKWAY_NAME_NOT_BLANK);
    }

    @Test
    @DisplayName("distanceKm가 0.2km 미만이면 예외가 발생한다")
    void shouldThrowException_whenDistanceIsTooShort() {
        Double invalidDistance = 0.19;

        assertThatThrownBy(() -> new WalkwayInfoTestBuilder().distanceKm(invalidDistance).build())
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.WALKWAY_DISTANCE_NOT_ENOUGH);
    }

    @Test
    @DisplayName("timeSec가 300초 미만이면 예외가 발생한다")
    void shouldThrowException_whenTimeIsTooShort() {
        Integer invalidTime = 299;

        assertThatThrownBy(() -> new WalkwayInfoTestBuilder().timeSec(invalidTime).build())
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.WALKWAY_TIME_NOT_ENOUGH);
    }

    @ParameterizedTest
    @ValueSource(strings = {"  새로운 이름 ", "   다른 이름"})
    @DisplayName("업데이트 시 이름의 앞뒤 공백이 제거된다")
    void shouldTrimName_whenUpdating(String nameWithSpaces) {
        // given
        WalkwayInfo info = new WalkwayInfoTestBuilder().build();

        // when
        info.updateWalkwayInfo(nameWithSpaces, info.getMemo(), info.getExposeLevel(), info.getHashtags());

        // then
        assertThat(info.getName()).isEqualTo(nameWithSpaces.trim());
    }

}
