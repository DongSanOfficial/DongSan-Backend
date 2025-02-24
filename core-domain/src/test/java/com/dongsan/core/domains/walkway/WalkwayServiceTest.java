package com.dongsan.core.domains.walkway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.core.support.util.CursorPagingResponse;
import fixture.WalkwayFixture;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayService Unit Test")
class WalkwayServiceTest {
    @InjectMocks
    WalkwayService walkwayService;
    @Mock
    WalkwayReader walkwayReader;
    @Mock
    WalkwayWriter walkwayWriter;
    @Mock
    WalkwayValidator walkwayValidator;

    @Nested
    @DisplayName("createWalkway 메서드는")
    class Describe_createWalkway {
        @Test
        @DisplayName("산책로를 생성하고 id를 반환한다.")
        void it_returns_id() {
            // given
            CreateWalkway createWalkway
                    = new CreateWalkway("name", 2.5, 30, ExposeLevel.PUBLIC, null, null, "memo", null, null, null, null);
            Long walkwayId = 1L;

            when(walkwayWriter.saveWalkway(createWalkway)).thenReturn(walkwayId);

            // when
            Long result = walkwayService.createWalkway(createWalkway);

            // then
            assertThat(result).isEqualTo(walkwayId);
        }
    }

    @Nested
    @DisplayName("getWalkway 메서드는")
    class Describe_getWalkway {
        @Test
        @DisplayName("산책로를 반환한다.")
        void it_returns_walkway() {
            // given
            Long walkwayId = 1L;
            Walkway walkway = WalkwayFixture.createWalkway();

            when(walkwayReader.getWalkway(walkwayId)).thenReturn(walkway);

            // when
            Walkway result = walkwayService.getWalkway(walkwayId);

            // then
            assertThat(result).isEqualTo(walkway);
        }
    }

    @Nested
    @DisplayName("updateWalkway 메서드는")
    class Describe_updateWalkway {
        @Test
        @DisplayName("산책로를 수정한다")
        void it_returns_void() {
            // given
            UpdateWalkway updateWalkway
                    = new UpdateWalkway(1L, "name", "memmo", ExposeLevel.PUBLIC, List.of("tag"));
            Long memberId = 1L;

            // when
            walkwayService.updateWalkway(updateWalkway, memberId);

            // when & then
            verify(walkwayService).updateWalkway(updateWalkway, memberId);
        }

    }

    @Nested
    @DisplayName("searchWalkway 메서드는")
    class Describe_searchWalkway {
        @Test
        @DisplayName("산책로를 수정한다")
        void it_returns_walkway_list() {
            // given
            String sortType = "rating";
            SearchWalkwayQuery searchWalkwayQuery = new SearchWalkwayQuery(1L, 123.0, 123.0, 10.0, 1L, 10);
            WalkwaySort sort = WalkwaySort.typeOf(sortType);
            List<Walkway> walkways = List.of(WalkwayFixture.createWalkway());

            when(walkwayReader.searchWalkway(searchWalkwayQuery, sort)).thenReturn(walkways);

            // when
            CursorPagingResponse<Walkway> result = walkwayService.searchWalkway(sortType, searchWalkwayQuery);

            // when & then
            assertThat(result.data()).hasSize(walkways.size());
        }
    }

    @Nested
    @DisplayName("existsLikedWalkway")
    class Describe_existsLikedWalkway {
        @Test
        @DisplayName("산책로의 좋아요 여부를 반환한다.")
        void it_returns_exists() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            boolean exists = true;

            when(walkwayReader.existsLikedWalkway(memberId, walkwayId)).thenReturn(exists);

            // when
            boolean result = walkwayService.existsLikedWalkway(memberId, walkwayId);

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

            when(walkwayReader.existsLikedWalkways(memberId, walkwayIds)).thenReturn(likedWalkways);

            // when
            Map<Long, Boolean> result = walkwayService.existsLikedWalkways(memberId, walkwayIds);

            // then
            assertThat(result.get(1L)).isTrue();
            assertThat(result.get(2L)).isFalse();
        }
    }

    @Nested
    @DisplayName("createLikedWalkway 메서드는")
    class Describe_createLikedWalkway {
        @Test
        @DisplayName("산책로의 좋아요를 생성한다.")
        void it_returns_void() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            boolean isLiked = false;

            when(walkwayReader.existsLikedWalkway(memberId, walkwayId)).thenReturn(isLiked);

            // when
            walkwayService.createLikedWalkway(memberId, walkwayId);

            // then
            verify(walkwayWriter).saveLikedWalkway(memberId, walkwayId);
        }
    }

    @Nested
    @DisplayName("deleteLikedWalkway 메서드는")
    class Describe_deleteLikedWalkway {
        @Test
        @DisplayName("산책로의 좋아요를 삭제한다.")
        void it_returns_void() {
            Long memberId = 1L;
            Long walkwayId = 1L;
            boolean isLiked = true;

            when(walkwayReader.existsLikedWalkway(memberId, walkwayId)).thenReturn(isLiked);

            // when
            walkwayService.deleteLikedWalkway(memberId, walkwayId);

            // then
            verify(walkwayWriter).deleteLikedWalkway(memberId, walkwayId);
        }
    }

    @Nested
    @DisplayName("getUserLikedWalkway 메서드는")
    class Describe_getUserLikedWalkway {
        @Test
        @DisplayName("회원의 산책로를 반환한다.")
        void it_returns_walkway_list() {
            // given
            Long memberId = 1L;
            Integer size = 10;
            Long walkwayId = 1L;
            Walkway walkway = WalkwayFixture.createWalkway();
            List<Walkway> walkways = List.of(WalkwayFixture.createWalkway());

            when(walkwayReader.getWalkway(walkwayId)).thenReturn(walkway);
            when(walkwayReader.getUserLikedWalkway(memberId, size, walkway.createdAt())).thenReturn(walkways);

            // when
            List<Walkway> result = walkwayService.getUserLikedWalkway(memberId, size, walkwayId);

            // then
            assertThat(result).hasSize(walkways.size());
        }
    }

    @Nested
    @DisplayName("getUserWalkway 메서드는")
    class Describe_getUserWalkway {
        @Test
        @DisplayName("회원의 산책로를 반환한다.")
        void it_returns_walkway_list() {
            // given
            Long memberId = 1L;
            Integer size = 10;
            Long walkwayId = 1L;
            Walkway walkway = WalkwayFixture.createWalkway();
            List<Walkway> walkways = List.of(WalkwayFixture.createWalkway());

            when(walkwayReader.getWalkway(walkwayId)).thenReturn(walkway);
            when(walkwayReader.getUserWalkWay(memberId, size, walkway.createdAt())).thenReturn(walkways);

            // when
            List<Walkway> result = walkwayService.getUserLikedWalkway(memberId, size, walkwayId);

            // then
            assertThat(result).hasSize(walkways.size());
        }
    }

    @Nested
    @DisplayName("createWalkwayHistory 메서드는")
    class Describe_createWalkwayHistory {
        @Test
        @DisplayName("산책 기록을 저장하고 반환한다.")
        void it_returns_id() {
            // given
            Long walkwayHistoryId = 1L;
            CreateWalkwayHistory createWalkwayHistory = new CreateWalkwayHistory(1L, 1L, 2.5, 100);

            when(walkwayWriter.saveWalkwayHistory(createWalkwayHistory)).thenReturn(walkwayHistoryId);

            // when
            Long result = walkwayService.createWalkwayHistory(createWalkwayHistory);

            // then
            assertThat(result).isEqualTo(walkwayHistoryId);
        }
    }

    @Nested
    @DisplayName("getCanReviewWalkwayHistory 메서드는")
    class Describe_getCanReviewWalkwayHistory {
        @Test
        @DisplayName("리뷰 가능한 산책로의 산책 기록을 조회힌다.")
        void it_returns_walkway_history_list() {
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            List<WalkwayHistory> walkwayHistories = List.of(WalkwayFixture.createWalkwayHistory());

            when(walkwayReader.getCanReviewWalkwayHistory(walkwayId, memberId)).thenReturn(walkwayHistories);

            // when
            List<WalkwayHistory> result = walkwayService.getCanReviewWalkwayHistory(walkwayId, memberId);

            // then
            assertThat(result).hasSize(walkwayHistories.size());
        }
    }

    @Nested
    @DisplayName("getUserCanReviewWalkwayHistory 메서드는")
    class Describe_getUserCanReviewWalkwayHistory {
        @Test
        @DisplayName("회원의 리뷰 가능한 산책 기록을 반환한다.")
        void it_returns_walkway_history_list() {
            // given
            Long lastWalkwayHistoryId = 1L;
            Long memberId = 1L;
            int size = 10;
            WalkwayHistory walkwayHistory = WalkwayFixture.createWalkwayHistory();
            List<WalkwayHistory> walkwayHistories = List.of(WalkwayFixture.createWalkwayHistory());

            when(walkwayReader.getWalkwayHistory(lastWalkwayHistoryId)).thenReturn(walkwayHistory);
            when(walkwayReader.getUserCanReviewWalkwayHistory(memberId, size, walkwayHistory.createdAt())).thenReturn(walkwayHistories);

            // when
            List<WalkwayHistory> result = walkwayService.getUserCanReviewWalkwayHistory(lastWalkwayHistoryId, memberId, size);

            // then
            assertThat(result).hasSize(walkwayHistories.size());
        }
    }

    @Nested
    @DisplayName("isCanReview 메서드는")
    class Describe_isCanReview {
        @Test
        @DisplayName("산책 기록이 리뷰 가능한지 반환한다.")
        void it_returns_can_review() {
            // given
            Long walkwayHistoryId = 1L;
            WalkwayHistory walkwayHistory = WalkwayFixture.createWalkwayHistory(0.0);

            when(walkwayReader.getWalkwayHistory(walkwayHistoryId)).thenReturn(walkwayHistory);

            // when
            boolean result = walkwayService.isCanReview(walkwayHistoryId);

            // then
            assertThat(result).isFalse();
        }
    }

}
