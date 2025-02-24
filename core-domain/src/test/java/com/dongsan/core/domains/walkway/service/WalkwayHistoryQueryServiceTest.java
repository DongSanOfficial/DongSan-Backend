//package com.dongsan.core.domains.walkway.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.common.error.exception.CustomException;
//import com.dongsan.domains.walkway.entity.WalkwayHistory;
//import com.dongsan.domains.walkway.repository.WalkwayHistoryQueryDSLRepository;
//import com.dongsan.domains.walkway.repository.WalkwayHistoryRepository;
//import com.dongsan.domains.walkway.service.WalkwayHistoryQueryService;
//import fixture.WalkwayHistoryFixture;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("WalkwayHistoryQueryService Unit Test")
//class WalkwayHistoryQueryServiceTest {
//    @InjectMocks
//    private WalkwayHistoryQueryService walkwayHistoryQueryService;
//
//    @Mock
//    private WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository;
//
//    @Mock
//    private WalkwayHistoryRepository walkwayHistoryRepository;
//
//    @Nested
//    @DisplayName("getCanReviewWalkwayHistories 메서드는")
//    class Describe_getCanReviewWalkwayHistories {
//
//        @Test
//        @DisplayName("리뷰 가능한 산책로 이용 기록 목록을 반환한다.")
//        void it_returns_can_review_walkway_histories() {
//            // given
//            Long walkwayId = 1L;
//            Long memberId = 1L;
//
//            List<WalkwayHistory> histories = new ArrayList<>();
//            for (int i = 0; i < 3; i++) {
//                histories.add(WalkwayHistoryFixture.createWalkwayHistory(null, null));
//            }
//
//            when(walkwayHistoryQueryDSLRepository.getCanReviewWalkwayHistories(walkwayId, memberId))
//                    .thenReturn(histories);
//
//            // when
//            List<WalkwayHistory> result = walkwayHistoryQueryService.getCanReviewWalkwayHistories(walkwayId, memberId);
//
//            // then
//            assertThat(result).hasSize(3);
//        }
//    }
//
//    @Nested
//    @DisplayName("getById 메서드는")
//    class Describe_getById {
//
//        @Test
//        @DisplayName("존재하는 이용 기록 ID가 주어지면 해당 기록을 반환한다.")
//        void it_returns_walkway_history() {
//            // given
//            Long walkwayHistoryId = 1L;
//            WalkwayHistory expectedHistory = WalkwayHistoryFixture.createWalkwayHistory(null, null);
//
//            when(walkwayHistoryRepository.findById(walkwayHistoryId))
//                    .thenReturn(Optional.of(expectedHistory));
//
//            // when
//            WalkwayHistory result = walkwayHistoryQueryService.getById(walkwayHistoryId);
//
//            // then
//            assertThat(result).isEqualTo(expectedHistory);
//        }
//
//        @Test
//        @DisplayName("존재하지 않는 이용 기록 ID가 주어지면 예외를 던진다.")
//        void it_throws_exception_when_history_not_found() {
//            // given
//            Long walkwayHistoryId = 99L;
//
//            when(walkwayHistoryRepository.findById(walkwayHistoryId))
//                    .thenReturn(Optional.empty());
//
//            // when & then
//            assertThatThrownBy(() -> walkwayHistoryQueryService.getById(walkwayHistoryId))
//                    .isInstanceOf(CustomException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("getUserCanReviewWalkwayHistories 메서드는")
//    class Describe_getUserCanReviewWalkwayHistories {
//
//        @Test
//        @DisplayName("리뷰 가능한 산책로 이용 기록 목록을 반환한다.")
//        void it_returns_can_review_walkway_histories() {
//            // given
//            Long memberId = 1L;
//            int size = 10;
//            LocalDateTime lastCreatedAt = LocalDateTime.now();
//
//            List<WalkwayHistory> histories = new ArrayList<>();
//            for (int i = 0; i < 3; i++) {
//                histories.add(WalkwayHistoryFixture.createWalkwayHistory(null, null));
//            }
//
//            when(walkwayHistoryQueryDSLRepository.getUserCanReviewWalkwayHistories(memberId, size, lastCreatedAt))
//                    .thenReturn(histories);
//
//            // when
//            List<WalkwayHistory> result = walkwayHistoryQueryService.getUserCanReviewWalkwayHistories(memberId, size, lastCreatedAt);
//
//            // then
//            assertThat(result).hasSize(3);
//        }
//    }
//}