package com.dongsan.core.domains.walkway;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static walkway.WalkwayFixture.createWalkwayHistory;

import com.dongsan.core.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayHistoryValidator Unit Test")
class WalkwayHistoryValidatorTest {
    @InjectMocks
    WalkwayHistoryValidator walkwayHistoryValidator;
    @Mock
    WalkwayRepository walkwayRepository;

    @Nested
    @DisplayName("validateWalkwayAndMember 메서드는")
    class Describe_validateWalkwayAndMember {
        @Test
        @DisplayName("산책기록의 산책로와 회원이 일치 하지 않으면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            WalkwayHistory walkwayHistory = createWalkwayHistory();
            Long walkwayId = 999L;
            Long memberId = 999L;

            // when & then
            assertThatThrownBy(() -> walkwayHistoryValidator.validateWalkwayAndMember(walkwayHistory, walkwayId, memberId))
                    .isInstanceOf(CoreException.class);
        }
    }

    @Nested
    @DisplayName("validateDistance 메서드는")
    class Describe_validateDistance {
        @Test
        @DisplayName("산책기록의 산책로와 회원이 일치 하지 않으면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            WalkwayHistory walkwayHistory = createWalkwayHistory(0.0);

            // when & then
            assertThatThrownBy(() -> walkwayHistoryValidator.validateDistance(walkwayHistory))
                    .isInstanceOf(CoreException.class);
        }
    }

    @Nested
    @DisplayName("validateIsReviewed 메서드는")
    class Describe_validateIsReviewed {
        @Test
        @DisplayName("산책 기록의 리뷰가 이미 작성되어 있으면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            WalkwayHistory walkwayHistory = createWalkwayHistory();

            // when & then
            assertThatThrownBy(() -> walkwayHistoryValidator.validateIsReviewed(walkwayHistory))
                    .isInstanceOf(CoreException.class);
        }
    }
}