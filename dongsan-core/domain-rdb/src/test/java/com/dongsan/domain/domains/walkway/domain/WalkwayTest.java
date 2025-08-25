package com.dongsan.domain.domains.walkway.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import fixture.WalkwayInfoTestBuilder;
import fixture.WalkwayTestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class WalkwayTest {

    @Test
    @DisplayName("공개 산책로는 소유자가 아니어도 접근할 수 있다")
    void shouldAllowAccess_whenWalkwayIsPublic() {
        Walkway walkway = new WalkwayTestBuilder().build();

        assertThatNoException().isThrownBy(() -> walkway.validateAccess(2L));
    }

    @Test
    @DisplayName("비공개 산책로는 소유자만 접근할 수 있다")
    void shouldThrowException_whenWalkwayIsPrivateAndNotOwner() {
        WalkwayInfo privateInfo = new WalkwayInfoTestBuilder().exposeLevel(WalkwayExposeLevel.PRIVATE).build();
        Walkway walkway = new WalkwayTestBuilder().walkwayInfo(privateInfo).build();

        assertThatThrownBy(() -> walkway.validateAccess(2L))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.WALKWAY_CANT_ACCESS);
    }

    @Test
    @DisplayName("산책로 소유자 여부를 올바르게 확인할 수 있다")
    void shouldCorrectlyCheckOwner() {
        Walkway walkway = new WalkwayTestBuilder().build();

        assertThatNoException().isThrownBy(() -> walkway.isOwner(1L));
        assertThatThrownBy(() -> walkway.isOwner(2L))
                .isInstanceOf(CoreException.class)
                .hasFieldOrPropertyWithValue("errorCode", CoreErrorCode.NOT_WALKWAY_OWNER);
    }

    @Test
    @DisplayName("산책로의 거리를 반환한다")
    void shouldReturnWalkwayDistance() {
        double expectedDistance = 5.5;
        WalkwayInfo info = new WalkwayInfoTestBuilder().distanceKm(expectedDistance).build();
        Walkway walkway = new WalkwayTestBuilder().walkwayInfo(info).build();

        assertThat(walkway.getDistance()).isEqualTo(expectedDistance);
    }

}
