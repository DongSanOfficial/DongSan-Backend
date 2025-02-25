package com.dongsan.core.domains.walkway;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.dongsan.core.support.error.CoreException;
import fixture.WalkwayFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayValidator Unit Test")
class WalkwayValidatorTest {
    @InjectMocks
    WalkwayValidator walkwayValidator;
    @Mock
    WalkwayRepository walkwayRepository;

    @Nested
    @DisplayName("isOwnerOfWalkway 메서드는")
    class Describe_isOwnerOfWalkway {
        @Test
        @DisplayName("산책로의 작성자가 아니면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            boolean isOwner = false;

            when(walkwayRepository.existsWalkway(walkwayId, memberId)).thenReturn(isOwner);

            // when & then
            assertThatThrownBy(() -> walkwayValidator.isOwnerOfWalkway(walkwayId, memberId))
                    .isInstanceOf(CoreException.class);
        }
    }

    @Nested
    @DisplayName("validateWalkwayExists 메서드는")
    class Describe_validateWalkwayExists {
        @Test
        @DisplayName("산책로의 존재하지 않으면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            Long walkwayId = 1L;
            boolean exists = false;

            when(walkwayRepository.existsWalkway(walkwayId)).thenReturn(exists);

            // when & then
            assertThatThrownBy(() -> walkwayValidator.validateWalkwayExists(walkwayId))
                    .isInstanceOf(CoreException.class);
        }
    }

    @Nested
    @DisplayName("validateWalkwayPrivate 메서드는")
    class Describe_validateWalkwayPrivate {
        @Test
        @DisplayName("산책로가 PRIVATE면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            Long walkwayId = 1L;
            Walkway walkway = WalkwayFixture.createWalkwayPrivate();

            when(walkwayRepository.getWalkway(walkwayId)).thenReturn(Optional.of(walkway));

            // when & then
            assertThatThrownBy(() -> walkwayValidator.validateWalkwayPrivate(walkwayId))
                    .isInstanceOf(CoreException.class);
        }
    }

    @Nested
    @DisplayName("validateWalkwayAccess 메서드는")
    class Describe_validateWalkwayAccess {
        @Test
        @DisplayName("산책로가 PRIVATE면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            Walkway walkway = WalkwayFixture.createWalkwayPrivate();
            Long memberId = 999L;

            // when & then
            assertThatThrownBy(() -> walkwayValidator.validateWalkwayAccess(walkway, memberId))
                    .isInstanceOf(CoreException.class);
        }
    }
}