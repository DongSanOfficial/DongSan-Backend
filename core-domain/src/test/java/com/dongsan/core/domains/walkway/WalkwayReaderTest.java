package com.dongsan.core.domains.walkway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.dongsan.core.support.error.CoreException;
import fixture.WalkwayFixture;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayReader Unit Test")
class WalkwayReaderTest {
    @InjectMocks
    WalkwayReader walkwayReader;
    @Mock
    WalkwayRepository walkwayRepository;
    @Mock
    SearchWalkwayFactory searchWalkwayFactory;
    @Mock
    SearchWalkway searchWalkway;

    @Nested
    @DisplayName("getWalkway 메서드는")
    class Describe_getWalkway {
        @Test
        @DisplayName("산책로를 조회한다.")
        void it_returns_walkway() {
            // given
            Long walkwayId = 1L;
            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId);

            when(walkwayRepository.getWalkway(walkwayId)).thenReturn(Optional.of(walkway));

            // when
            Walkway result = walkwayReader.getWalkway(walkwayId);

            // then
            assertThat(result.walkwayId()).isEqualTo(walkway.walkwayId());
        }

        @Test
        @DisplayName("존재하지 않는 산책로면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            Long walkwayId = 999999999L;

            when(walkwayRepository.getWalkway(walkwayId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> walkwayReader.getWalkway(walkwayId))
                    .isInstanceOf(CoreException.class);
        }
    }

    @Nested
    @DisplayName("getUserWalkWay 메서드는")
    class Describe_getUserWalkWay {
        @Test
        @DisplayName("회원의 산책로를 조회한다.")
        void it_returns_walkway_list() {
            // given
            Long memberId = 1L;
            Integer size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            List<Walkway> walkways = List.of(WalkwayFixture.createWalkway());

            when(walkwayRepository.getUserWalkway(memberId, size, lastCreatedAt)).thenReturn(walkways);

            // when
            List<Walkway> result = walkwayReader.getUserWalkWay(memberId, size, lastCreatedAt);

            // then
            assertThat(result).hasSize(walkways.size());
        }
    }

    @Nested
    @DisplayName("existsWalkway 메서드는")
    class Describe_existsWalkway {
        @Test
        @DisplayName("산책로의 유무를 조회한다.")
        void it_returns_exists() {
            // given
            Long walkwayId = 1L;
            boolean exists = true;

            when(walkwayRepository.existsWalkway(walkwayId)).thenReturn(exists);

            // when
            boolean result = walkwayReader.existsWalkway(walkwayId);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("searchWalkway 메서드는")
    class Describe_searchWalkway {
        @Test
        @DisplayName("검색한 산책로 리스트를 반환한다.")
        void it_returns_walkway_list() {
            // given
            SearchWalkwayQuery searchWalkwayQuery = new SearchWalkwayQuery(1L, 123.0, 123.0, 10.0, 1L, 10);
            WalkwaySort walkwaySort = WalkwaySort.LIKED;
            List<Walkway> walkways = List.of(WalkwayFixture.createWalkway());

            when(searchWalkwayFactory.getService(walkwaySort)).thenReturn(searchWalkway);
            when(searchWalkway.search(searchWalkwayQuery)).thenReturn(walkways);

            // when
            List<Walkway> result = walkwayReader.searchWalkway(searchWalkwayQuery, walkwaySort);

            // then
            assertThat(result).hasSize(walkways.size());
        }
    }

    @Nested
    @DisplayName("existsLikedWalkway 메서드는")
    class Describe_existsLikedWalkway {
        @Test
        @DisplayName("산책로의 좋아요 여부를 반환한다.")
        void it_returns_exists() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            boolean exists = true;

            when(walkwayRepository.existsLikedWalkway(memberId, walkwayId)).thenReturn(exists);

            // when
            boolean result = walkwayReader.existsLikedWalkway(memberId, walkwayId);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("existsLikedWalkways 메서드는")
    class Describe_existsLikedWalkways {
        @Test
        @DisplayName("산책로의 좋아요 여부를 반환한다.")
        void it_returns_exists() {
            // given
            Long memberId = 1L;
            List<Long> walkwayIds = List.of(1L, 2L);
            Map<Long, Boolean> likedWalkways = new HashMap<>();
            likedWalkways.put(1L, true);
            likedWalkways.put(2L, false);

            when(walkwayRepository.existsLikedWalkways(memberId, walkwayIds)).thenReturn(likedWalkways);

            // when
            Map<Long, Boolean> result = walkwayReader.existsLikedWalkways(memberId, walkwayIds);

            // then
            assertThat(result.get(1L)).isTrue();
            assertThat(result.get(2L)).isFalse();
        }
    }

    @Nested
    @DisplayName("getUserLikedWalkway 메서드는")
    class Describe_getUserLikedWalkway {
        @Test
        @DisplayName("좋아요한 산책로 리스트를 반환한다.")
        void it_returns_walkway_list() {
            // given
            Long memberId = 1L;
            Integer size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            List<Walkway> walkways = List.of(WalkwayFixture.createWalkway());

            when(walkwayRepository.getUserLikedWalkway(memberId, size, lastCreatedAt)).thenReturn(walkways);

            // when
            List<Walkway> result = walkwayReader.getUserLikedWalkway(memberId, size, lastCreatedAt);

            // then
            assertThat(result).hasSize(walkways.size());
        }
    }

    @Nested
    @DisplayName("getWalkwayHistory 메서드는")
    class Describe_getWalkwayHistory {
        @Test
        @DisplayName("산책 기록을 반환한다.")
        void it_returns_walkway_history() {
            // given
            Long walkwayHistoryId = 1L;
            WalkwayHistory walkwayHistory = WalkwayFixture.createWalkwayHistory();

            when(walkwayRepository.getWalkwayHistory(walkwayHistoryId)).thenReturn(Optional.of(walkwayHistory));

            // when
            WalkwayHistory result = walkwayReader.getWalkwayHistory(walkwayHistoryId);

            // then
            assertThat(result).isEqualTo(walkwayHistory);
        }

        @Test
        @DisplayName("산책 기록이 존재하지 않으면 예외처리한다.")
        void it_returns_exception() {
            // given
            Long walkwayHistoryId = 99999L;

            when(walkwayRepository.getWalkwayHistory(walkwayHistoryId)).thenReturn(Optional.empty());

            // when && then
            assertThatThrownBy(() -> walkwayReader.getWalkwayHistory(walkwayHistoryId))
                    .isInstanceOf(CoreException.class);
        }
    }

    @Nested
    @DisplayName("getCanReviewWalkwayHistory 메서드는")
    class Describe_getCanReviewWalkwayHistory {
        @Test
        @DisplayName("산책 기록을 반환한다.")
        void it_returns_walkway_history() {
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            List<WalkwayHistory> walkwayHistories = List.of(WalkwayFixture.createWalkwayHistory());

            when(walkwayRepository.getCanReviewWalkwayHistory(walkwayId, memberId)).thenReturn(walkwayHistories);

            // when
            List<WalkwayHistory> result = walkwayReader.getCanReviewWalkwayHistory(walkwayId, memberId);

            // then
            assertThat(result).hasSize(walkwayHistories.size());
        }
    }

    @Nested
    @DisplayName("getUserCanReviewWalkwayHistory 메서드는")
    class Describe_getUserCanReviewWalkwayHistory {
        @Test
        @DisplayName("산책 기록을 반환한다.")
        void it_returns_walkway_history() {
            // given
            Long memberId = 1L;
            int size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            List<WalkwayHistory> walkwayHistories = List.of(WalkwayFixture.createWalkwayHistory());

            when(walkwayRepository.getUserCanReviewWalkwayHistory(memberId, size, lastCreatedAt)).thenReturn(walkwayHistories);

            // when
            List<WalkwayHistory> result = walkwayReader.getUserCanReviewWalkwayHistory(memberId, size, lastCreatedAt);

            // then
            assertThat(result).hasSize(walkwayHistories.size());
        }
    }
}