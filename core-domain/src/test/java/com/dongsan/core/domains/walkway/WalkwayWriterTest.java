package com.dongsan.core.domains.walkway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayWriter Unit Test")
class WalkwayWriterTest {
    @InjectMocks
    WalkwayWriter walkwayWriter;
    @Mock
    WalkwayRepository walkwayRepository;

    @Nested
    @DisplayName("saveWalkway 메서드는")
    class Describe_saveWalkway {
        @Test
        @DisplayName("산책로를 저장하고 id를 반환한다.")
        void it_returns_id() {
            // given
            CreateWalkway createWalkway
                    = new CreateWalkway("name", 2.5, 30, ExposeLevel.PUBLIC, null, null, "memo", null, null, null, null);
            Long walkwayId = 1L;

            when(walkwayRepository.saveWalkway(createWalkway)).thenReturn(walkwayId);

            // when
            Long result = walkwayWriter.saveWalkway(createWalkway);

            // then
            assertThat(result).isEqualTo(walkwayId);
        }
    }

    @Nested
    @DisplayName("updateWalkway 메서드는")
    class Describe_updateWalkway {
        @Test
        @DisplayName("산책로를 수정한다.")
        void it_returns_void() {
            // given
            UpdateWalkway updateWalkway = new UpdateWalkway(1L, "name", "memmo", ExposeLevel.PUBLIC, List.of("tag"));

            // when
            walkwayWriter.updateWalkway(updateWalkway);

            // then
            verify(walkwayRepository).updateWalkway(updateWalkway);
        }
    }

    @Nested
    @DisplayName("saveLikedWalkway 메서드는")
    class Describe_saveLikedWalkway {
        @Test
        @DisplayName("산책로 좋아요를 저장한다.")
        void it_returns_void() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;

            // when
            walkwayWriter.saveLikedWalkway(memberId, walkwayId);

            // then
            verify(walkwayRepository).saveLikedWalkway(memberId, walkwayId);
        }
    }

    @Nested
    @DisplayName("deleteLikedWalkway 메서드는")
    class Describe_deleteLikedWalkway {
        @Test
        @DisplayName("산책로 좋아요를 저장한다.")
        void it_returns_void() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;

            // when
            walkwayWriter.deleteLikedWalkway(memberId, walkwayId);

            // then
            verify(walkwayRepository).deleteLikedWalkway(memberId, walkwayId);
        }
    }

    @Nested
    @DisplayName("saveWalkwayHistory 메서드는")
    class Describe_saveWalkwayHistory {
        @Test
        @DisplayName("산책 기록을 저장하고 id를 반환한다.")
        void it_returns_id() {
            // given
            CreateWalkwayHistory createWalkwayHistory = new CreateWalkwayHistory(1L, 1L, 2.5, 100);
            Long walkwayHistoryId = 1L;

            when(walkwayRepository.saveWalkwayHistory(createWalkwayHistory)).thenReturn(walkwayHistoryId);

            // when
            Long result = walkwayWriter.saveWalkwayHistory(createWalkwayHistory);

            // then
            assertThat(result).isEqualTo(walkwayHistoryId);
        }
    }

    @Nested
    @DisplayName("updateWalkwayHistoryIsReviewed 메서드는")
    class Describe_updateWalkwayHistoryIsReviewed {
        @Test
        @DisplayName("산책 기록의 리뷰 유무를 수정한다.")
        void it_returns_void() {
            // given
            Long walkwayHistoryId = 1L;
            boolean isReviewed = true;

            // when
            walkwayWriter.updateWalkwayHistoryIsReviewed(walkwayHistoryId, isReviewed);

            // then
            verify(walkwayRepository).updateWalkwayHistoryIsReviewed(walkwayHistoryId, isReviewed);
        }
    }

    @Nested
    @DisplayName("updateWalkwayRating 메서드는")
    class Describe_updateWalkwayRating {
        @Test
        @DisplayName("산책로의 별점을 수정한다.")
        void it_returns_void() {
            // given
            Integer reviewCount = 10;
            Double rating = 3.8;
            Long walkwayId = 1L;

            // when
            walkwayWriter.updateWalkwayRating(reviewCount, rating, walkwayId);

            // then
            verify(walkwayRepository).updateWalkwayRating(reviewCount, rating, walkwayId);
        }
    }

    @Nested
    @DisplayName("deleteWalkway 메서드는")
    class Describe_deleteWalkway {
        @Test
        @DisplayName("산책로를 삭제한다.")
        void it_returns_void() {
            // given
            Long walkwayId = 1L;

            // when
            walkwayWriter.deleteWalkway(walkwayId);

            // then
            verify(walkwayRepository).deleteWalkway(walkwayId);
        }
    }
}