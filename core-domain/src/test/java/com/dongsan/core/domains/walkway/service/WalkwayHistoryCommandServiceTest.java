//package com.dongsan.core.domains.walkway.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.domains.walkway.entity.WalkwayHistory;
//import com.dongsan.domains.walkway.repository.WalkwayHistoryRepository;
//import com.dongsan.domains.walkway.service.WalkwayHistoryCommandService;
//import fixture.WalkwayHistoryFixture;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("WalkwayHistoryCommandService Unit Test")
//class WalkwayHistoryCommandServiceTest {
//    @InjectMocks
//    WalkwayHistoryCommandService walkwayHistoryCommandService;
//
//    @Mock
//    WalkwayHistoryRepository walkwayHistoryRepository;
//
//    @Nested
//    @DisplayName("createWalkwayHistory 메서드는")
//    class Describe_createWalkwayHistory {
//
//        @Test
//        @DisplayName("walkwayHistory 객체를 저장하고 반환한다.")
//        void it_saves_and_returns_walkway_history() {
//            // given
//            WalkwayHistory walkwayHistory = WalkwayHistoryFixture.createWalkwayHistory(null, null);
//            when(walkwayHistoryRepository.save(any()))
//                    .thenReturn(walkwayHistory);
//
//            // when
//            WalkwayHistory result = walkwayHistoryCommandService.createWalkwayHistory(walkwayHistory);
//
//            // then
//            assertThat(result).isEqualTo(walkwayHistory);
//            verify(walkwayHistoryRepository).save(walkwayHistory); // save()가 1번 호출되었는지 검증
//        }
//    }
//}