package com.dongsan.domain.domains.walkway;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class WalkwaySortTest {
    
    @ParameterizedTest
    @ValueSource(strings = {"views", "time", "distance"})
    @DisplayName("유효하지 않은 타입으로 WalkwaySort를 찾으려 할 때 예외가 발생한다")
    void shouldThrowException_whenInvalidTypeIsGiven(String invalidType) {
        assertThatThrownBy(() -> WalkwaySort.typeOf(invalidType))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.INVALID_SEARCH_TYPE);
    }

}
