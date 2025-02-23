package com.dongsan.rdb.domains.walkway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.CreateWalkwayHistory;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.core.domains.walkway.SearchWalkwayQuery;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.member.MemberJpaRepository;
import fixture.LikedWalkwayFixture;
import fixture.MemberFixture;
import fixture.WalkwayFixture;
import fixture.WalkwayHistoryFixture;
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
@DisplayName("WalkwayCoreJpaRepository Unit Test")
class WalkwayCoreJpaRepositoryTest {
    @InjectMocks
    private WalkwayCoreJpaRepository walkwayCoreJpaRepository;
    @Mock
    private MemberJpaRepository memberJpaRepository;
    @Mock
    private LikedWalkwayJpaRepository likedWalkwayJpaRepository;
    @Mock
    private LikedWalkwayQueryDSLRepository likedWalkwayQueryDSLRepository;
    @Mock
    private WalkwayJpaRepository walkwayJpaRepository;
    @Mock
    private WalkwayQueryDSLRepository walkwayQueryDSLRepository;
    @Mock
    private WalkwayHistoryJpaRepository walkwayHistoryJpaRepository;
    @Mock
    private WalkwayHistoryQueryDSLRepository walkwayHistoryQueryDSLRepository;
    @Nested
    @DisplayName("saveWalkway 메서드는")
    class Describe_saveWalkway {
        @Test
        @DisplayName("올바른 CreateWalkway DTO를 받으면 저장된 Walkway의 ID를 반환한다")
        void it_returns_id() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            CreateWalkway createWalkway
                    = new CreateWalkway("Sample Walkway", 2.5, 30, ExposeLevel.PUBLIC, null, null, "A beautiful walkway.", null, null,null,memberId);
            MemberEntity memberEntity = MemberFixture.createMember();
            WalkwayEntity walkwayEntity = WalkwayFixture.createWalkwayWithId(walkwayId, memberEntity);

            when(memberJpaRepository.getReferenceById(memberId)).thenReturn(memberEntity);
            when(walkwayJpaRepository.save(any(WalkwayEntity.class))).thenReturn(walkwayEntity);

            // when
            Long result = walkwayCoreJpaRepository.saveWalkway(createWalkway);

            // then
            assertThat(result).isEqualTo(walkwayId);
        }
    }

    @Nested
    @DisplayName("getWalkway 메서드는")
    class Describe_getWalkway {
        @Test
        @DisplayName("존재하는 walkwayId를 받으면 해당 Walkway를 반환한다")
        void it_returns_walkway() {
            // given
            Long walkwayId = 1L;
            WalkwayEntity walkwayEntity = WalkwayFixture.createWalkway(null);

            when(walkwayJpaRepository.findById(walkwayId)).thenReturn(Optional.of(walkwayEntity));

            // when
            Optional<Walkway> result = walkwayCoreJpaRepository.getWalkway(walkwayId);

            // then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("존재하지 않는 walkwayId를 받으면 빈 Optional을 반환한다")
        void it_returns_empty() {
            // given
            Long walkwayId = 999L;
            when(walkwayJpaRepository.findById(walkwayId)).thenReturn(Optional.empty());

            // when
            Optional<Walkway> result = walkwayCoreJpaRepository.getWalkway(walkwayId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("saveLikedWalkway 메서드는")
    class Describe_saveLikedWalkway {
        @Test
        @DisplayName("좋아요 정보를 저장하고 ID를 반환한다")
        void it_returns_id() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            MemberEntity memberEntity = MemberFixture.createMember();
            WalkwayEntity walkwayEntity = WalkwayFixture.createWalkway(memberEntity);
            LikedWalkwayEntity likedWalkwayEntity = LikedWalkwayFixture.createLikedWalkway(memberEntity, walkwayEntity);        when(memberJpaRepository.getReferenceById(memberId)).thenReturn(memberEntity);
            when(walkwayJpaRepository.getReferenceById(walkwayId)).thenReturn(walkwayEntity);
            when(likedWalkwayJpaRepository.save(any(LikedWalkwayEntity.class))).thenReturn(likedWalkwayEntity);

            // when
            Long result = walkwayCoreJpaRepository.saveLikedWalkway(memberId, walkwayId);

            // then
            assertThat(result).isEqualTo(likedWalkwayEntity.getId());
        }
    }

    @Nested
    @DisplayName("searchWalkwaysLiked 메서드는")
    class Describe_searchWalkwaysLikedTest {
        @Test
        @DisplayName("검색 조건에 맞는 좋아요 순으로 정렬된 산책로 목록을 반환한다")
        void it_returns_sorted_list() {
            // given
            SearchWalkwayQuery query = new SearchWalkwayQuery(null, null, null, null, null, 10);
            List<WalkwayEntity> walkwayEntities = List.of(WalkwayFixture.createWalkway(null));

            when(walkwayQueryDSLRepository.searchWalkwaysLiked(query)).thenReturn(walkwayEntities);

            // when
            List<Walkway> result = walkwayCoreJpaRepository.searchWalkwaysLiked(query);

            // then
            assertThat(result).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("searchWalkwaysRating 메서드는")
    class Describe_searchWalkwaysRatingTest {
        @Test
        @DisplayName("검색 조건에 맞는 좋아요 순으로 정렬된 산책로 목록을 반환한다")
        void it_returns_sorted_list() {
            // given
            SearchWalkwayQuery query = new SearchWalkwayQuery(null, null, null, null, null, 10);
            List<WalkwayEntity> walkwayEntities = List.of(WalkwayFixture.createWalkway(null));

            when(walkwayQueryDSLRepository.searchWalkwaysRating(query)).thenReturn(walkwayEntities);

            // when
            List<Walkway> result = walkwayCoreJpaRepository.searchWalkwaysLiked(query);

            // then
            assertThat(result).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("getUserLikedWalkway 메서드는")
    class Describe_getUserLikedWalkway {
        @Test
        @DisplayName("회원이 좋아요를 누른 산책로 목록을 반환한다.")
        void it_returns_liked_walkway_list() {
            // given
            Long memberId = 1L;
            Integer size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            List<WalkwayEntity> walkwayEntities = List.of(WalkwayFixture.createWalkway(null));

            when(walkwayQueryDSLRepository.getUserLikedWalkway(memberId, size, lastCreatedAt)).thenReturn(walkwayEntities);

            // when
            List<Walkway> result = walkwayCoreJpaRepository.getUserWalkway(memberId, size, lastCreatedAt);

            // then
            assertThat(result).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("getUserWalkway 메서드는")
    class Describe_getUserWalkway {
        @Test
        @DisplayName("회원이 좋아요를 누른 산책로 목록을 반환한다.")
        void it_returns_user_walkway_list() {
            // given
            Long memberId = 1L;
            Integer size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            List<WalkwayEntity> walkwayEntities = List.of(WalkwayFixture.createWalkway(null));

            when(walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt)).thenReturn(walkwayEntities);

            // when
            List<Walkway> result = walkwayCoreJpaRepository.getUserWalkway(memberId, size, lastCreatedAt);

            // then
            assertThat(result).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("updateWalkwayRating 메서드는")
    class Describe_updateWalkwayRating {
        @Test
        @DisplayName("산책로의 평점과 리뷰 수를 수정한다.")
        void it_update_walkway_rating() {
            // given
            Integer reviewCount = 100;
            Double rating = 3.5;
            Long walkwayId = 1L;
            WalkwayEntity walkwayEntity = WalkwayFixture.createWalkway(null);

            when(walkwayJpaRepository.getReferenceById(walkwayId)).thenReturn(walkwayEntity);

            // when
            walkwayCoreJpaRepository.updateWalkwayRating(reviewCount, rating, walkwayId);

            // then
            verify(walkwayJpaRepository).save(walkwayEntity);
        }
    }

    @Nested
    @DisplayName("existsLikedWalkways 메서드는")
    class Describe_existsLikedWalkways {
        @Test
        @DisplayName("산책로의 좋아요 존재 여부를 반환한다.")
        void it_returns_exists_liked_walkway() {
            // given
            Long memberId = 1L;
            List<Long> walkwayIds = List.of();
            Map<Long, Boolean> isLiked = new HashMap<>();

            when(likedWalkwayQueryDSLRepository.existsLikedWalkways(memberId, walkwayIds)).thenReturn(isLiked);

            // when
            Map<Long, Boolean> result = walkwayCoreJpaRepository.existsLikedWalkways(1L, walkwayIds);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("deleteLikedWalkway 메서드는")
    class Describe_deleteLikedWalkway {
        @Test
        @DisplayName("산책로 좋아요를 삭제한다.")
        void it_delete_liked_walkway() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            WalkwayEntity walkwayEntity = WalkwayFixture.createWalkway(null);

            when(walkwayJpaRepository.getReferenceById(walkwayId)).thenReturn(walkwayEntity);

            // when
            walkwayCoreJpaRepository.deleteLikedWalkway(memberId, walkwayId);

            // then
            verify(walkwayJpaRepository).save(walkwayEntity);
            verify(likedWalkwayJpaRepository).deleteByMemberIdAndWalkwayId(memberId, walkwayId);
        }
    }

    @Nested
    @DisplayName("saveWalkwayHistory 메서드는")
    class Describe_saveWalkwayHistoryTest {
        @Test
        @DisplayName("산책 기록을 저장하고 ID를 반환한다")
        void it_returns_id() {
            // given
            Long memberId = 1L;
            Long walkwayId = 1L;
            CreateWalkwayHistory createHistory = new CreateWalkwayHistory(memberId, walkwayId, 1000.0, 30);
            MemberEntity memberEntity = MemberFixture.createMember();
            WalkwayEntity walkwayEntity = WalkwayFixture.createWalkway(memberEntity);
            WalkwayHistoryEntity historyEntity = WalkwayHistoryFixture.createWalkwayHistory(memberEntity, walkwayEntity);

            when(memberJpaRepository.getReferenceById(memberId)).thenReturn(memberEntity);
            when(walkwayJpaRepository.getReferenceById(walkwayId)).thenReturn(walkwayEntity);
            when(walkwayHistoryJpaRepository.save(historyEntity)).thenReturn(historyEntity);

            // when
            Long result = walkwayCoreJpaRepository.saveWalkwayHistory(createHistory);

            // then
            assertThat(result).isEqualTo(historyEntity.getId());
        }
    }

    @Nested
    @DisplayName("getCanReviewWalkwayHistory 메서드는")
    class Describe_getCanReviewWalkwayHistory {
        @Test
        @DisplayName("리뷰 가능한 산책 기록 목록을 조회 한다.")
        void it_returns_walkway_history() {
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            List<WalkwayHistoryEntity> walkwayHistoryEntities = List.of(WalkwayHistoryFixture.createWalkwayHistory(null, null));
            List<WalkwayHistory> walkwayHistories = walkwayHistoryEntities.stream()
                    .map(WalkwayHistoryEntity::toWalkwayHistory)
                    .toList();
            when(walkwayHistoryQueryDSLRepository.getCanReviewWalkwayHistories(walkwayId, memberId)).thenReturn(walkwayHistoryEntities);

            // when
            List<WalkwayHistory> result = walkwayCoreJpaRepository.getCanReviewWalkwayHistory(walkwayId, memberId);

            // then
            assertThat(result).hasSize(walkwayHistories.size());
        }
    }

    @Nested
    @DisplayName("getUserCanReviewWalkwayHistory 메서드는")
    class Describe_getUserCanReviewWalkwayHistory {
        @Test
        @DisplayName("회원의 리뷰 가능한 산책 기록 목록을 조회 한다.")
        void it_returns_walkway_history() {
            // given
            Long memberId = 1L;
            int size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            List<WalkwayHistoryEntity> walkwayHistoryEntities = List.of(WalkwayHistoryFixture.createWalkwayHistory(null, null));
            List<WalkwayHistory> walkwayHistories = walkwayHistoryEntities.stream()
                    .map(WalkwayHistoryEntity::toWalkwayHistory)
                    .toList();
            when(walkwayHistoryQueryDSLRepository.getUserCanReviewWalkwayHistories(memberId, size, lastCreatedAt)).thenReturn(walkwayHistoryEntities);

            // when
            List<WalkwayHistory> result = walkwayCoreJpaRepository.getUserCanReviewWalkwayHistory(memberId, size, lastCreatedAt);

            // then
            assertThat(result).hasSize(walkwayHistories.size());
        }
    }

    @Nested
    @DisplayName("getWalkwayHistory 메서드는")
    class Describe_getWalkwayHistory {
        @Test
        @DisplayName("id에 해당하는 산책 기록을 조회한다.")
        void it_returns_walkway_history() {
            // given
            Long walkwayHistoryId = 1L;
            WalkwayHistoryEntity walkwayHistoryEntity = WalkwayHistoryFixture.createWalkwayHistory(null, null);

            when(walkwayHistoryJpaRepository.findById(walkwayHistoryId)).thenReturn(Optional.of(walkwayHistoryEntity));

            // when
            Optional<WalkwayHistory> result = walkwayCoreJpaRepository.getWalkwayHistory(walkwayHistoryId);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("updateWalkwayHistoryIsReviewed 메서드는")
    class Describe_updateWalkwayHistoryIsReviewed {
        @Test
        @DisplayName("id에 해당하는 산책 기록의 리뷰 상태를 변경한다.")
        void it_update_is_reviewed() {
            // given
            Long walkwayHistoryId = 1L;
            boolean isReviewed = true;
            WalkwayHistoryEntity walkwayHistoryEntity = WalkwayHistoryFixture.createWalkwayHistory(null, null);

            when(walkwayHistoryJpaRepository.getReferenceById(walkwayHistoryId)).thenReturn(walkwayHistoryEntity);

            // when
            walkwayCoreJpaRepository.updateWalkwayHistoryIsReviewed(walkwayHistoryId, isReviewed);

            // then
            verify(walkwayHistoryJpaRepository).save(walkwayHistoryEntity);
        }
    }
}