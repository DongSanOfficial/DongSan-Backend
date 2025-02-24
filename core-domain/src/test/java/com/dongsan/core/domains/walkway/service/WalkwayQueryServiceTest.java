//package com.dongsan.core.domains.walkway.service;
//
//import static fixture.BookmarkFixture.createBookmark;
//import static fixture.ReflectFixture.reflectField;
//import static fixture.WalkwayFixture.createWalkway;
//import static fixture.WalkwayFixture.createWalkwayWithId;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.walkway.WalkwayReader;
//import com.dongsan.common.error.exception.CustomException;
//import com.dongsan.domains.bookmark.entity.Bookmark;
//import com.dongsan.domains.bookmark.entity.MarkedWalkway;
//import com.dongsan.domains.bookmark.repository.MarkedWalkwayQueryDSLRepository;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.core.domains.walkway.WalkwaySort;
//import com.dongsan.domains.walkway.repository.WalkwayQueryDSLRepository;
//import com.dongsan.domains.walkway.repository.WalkwayRepository;
//import com.dongsan.core.domains.walkway.service.factory.SearchWalkwayServiceFactory;
//import com.dongsan.core.domains.walkway.service.search.walkway.SearchWalkwayService;
//import fixture.MarkedWalkwayFixture;
//import member.MemberFixture;
//import fixture.WalkwayFixture;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.IntStream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("WalkwayQueryService Unit Test")
//class WalkwayQueryServiceTest {
//    @InjectMocks
//    WalkwayReader walkwayQueryService;
//    @Mock
//    WalkwayRepository walkwayRepository;
//    @Mock
//    WalkwayQueryDSLRepository walkwayQueryDSLRepository;
//    @Mock
//    MarkedWalkwayQueryDSLRepository markedWalkwayQueryDSLRepository;
//    @Mock
//    SearchWalkwayServiceFactory serviceFactory;
//    @Mock
//    SearchWalkwayService searchWalkwayService;
//
//
//    @Nested
//    @DisplayName("getWalkwayWithHashtagAndLike 메서드는")
//    class Describe_getWalkwayWithHashtagAndLike {
//
//        @Test
//        @DisplayName("walkwayId를 받으면 해당하는 Walkway와 Liked 리스트를 반환한다.")
//        void it_returns_walkway_liked() {
//            // given
//            Member member = MemberFixture.createMemberWithId(1L);
//            Walkway walkway = createWalkwayWithId(1L, member);
//
//            // Mocking Tuple 반환값 설정
//            when(walkwayQueryDSLRepository.getUserWalkwayWithHashtagAndLike(member.getId(), walkway.getId())).thenReturn(walkway);
//
//            // when
//            Walkway result = walkwayQueryService.getWalkwayWithHashtagAndLike(member.getId(), walkway.getId());
//
//            // then
//            assertThat(result).isEqualTo(walkway);
//        }
//    }
//
//    @Nested
//    @DisplayName("getUserWalkWay 메서드는")
//    class Describe_getUserWalkWay{
//        @Test
//        @DisplayName("lastCreated보다 created가 작은 walkway가 있으면 walkway를 리스트로 반환한다.")
//        void it_returns_walkway_list(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 23, 11, 11);
//            List<Walkway> walkways = IntStream.range(0, 2)
//                    .mapToObj(index ->
//                            createWalkwayWithId((long)(index+1), null)
//                    ).toList();
//            when(walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt)).thenReturn(walkways);
//
//            // when
//            List<Walkway> result = walkwayQueryService.getUserWalkWay(memberId, size, lastCreatedAt);
//
//            // then
//            assertThat(result).isEqualTo(walkways);
//        }
//
//        @Test
//        @DisplayName("lastCreated보다 created가 작은 walkway가 없으면 빈 리스트를 반환한다.")
//        void it_returns_emtpy_list(){
//            // given
//            Long memberId = 1L;
//            Integer size = 5;
//            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 23, 11, 11);
//            List<Walkway> walkways = Collections.emptyList();
//            when(walkwayQueryDSLRepository.getUserWalkway(memberId, size, lastCreatedAt)).thenReturn(walkways);
//
//            // when
//            List<Walkway> result = walkwayQueryService.getUserWalkWay(memberId, size, lastCreatedAt);
//
//            // then
//            assertThat(result).isEmpty();
//        }
//    }
//
//    @Nested
//    @DisplayName("existsByWalkwayId 메서드는")
//    class Describe_existsByWalkwayId{
//        @Test
//        @DisplayName("walkway가 존재하면 true를 반환한다.")
//        void it_returns_true(){
//            // given
//            Long walkwayId = 1L;
//            when(walkwayRepository.existsById(walkwayId)).thenReturn(true);
//
//            // when
//            boolean result = walkwayQueryService.existsByWalkwayId(walkwayId);
//
//            // then
//            assertThat(result).isTrue();
//        }
//
//        @Test
//        @DisplayName("walkway가 존재하지 않으면 false를 반환한다.")
//        void it_returns_false(){
//            // given
//            Long walkwayId = 1L;
//            when(walkwayRepository.existsById(walkwayId)).thenReturn(false);
//
//            // when
//            boolean result = walkwayQueryService.existsByWalkwayId(walkwayId);
//
//            // then
//            assertThat(result).isFalse();
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkway 메서드는")
//    class Describe_getWalkway {
//        @Test
//        @DisplayName("산책로를 반환한다.")
//        void it_returns_walkway() {
//            // Given
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, null);
//
//            when(walkwayRepository.findById(walkway.getId())).thenReturn(Optional.of(walkway));
//
//            // When
//            Walkway result = walkwayQueryService.getWalkway(walkway.getId());
//
//            // Then
//            assertThat(result).isEqualTo(walkway);
//        }
//
//        @Test
//        @DisplayName("산책로가 없을 경우 예외를 반환한다.")
//        void it_returns_exception() {
//            // Given
//            Long walkwayId = 1L;
//            when(walkwayRepository.findById(walkwayId)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThatThrownBy(() -> walkwayQueryService.getWalkway(walkwayId))
//                    .isInstanceOf(CustomException.class);
//        }
//    }
//
//    @Nested
//    @DisplayName("getBookmarkWalkway 메서드는")
//    class Describe_getBookmarkWalkway{
//        @Test
//        @DisplayName("산책로가 존재하면 산책로 리스트를 반환한다.")
//        void it_returns_walkway_list(){
//            // given
//            Bookmark bookmark = createBookmark(null);
//            reflectField(bookmark, "id", 1L);
//            Integer size = 10;
//            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 9, 11, 11);
//            Long memberId = 1L;
//            List<MarkedWalkway> markedWalkways = List.of(
//                    MarkedWalkwayFixture.createMarkedWalkway(createWalkway(null), bookmark),
//                    MarkedWalkwayFixture.createMarkedWalkway(createWalkway(null), bookmark),
//                    MarkedWalkwayFixture.createMarkedWalkway(createWalkway(null), bookmark));
//            when(markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmark.getId(), size, lastCreatedAt, memberId)).thenReturn(markedWalkways);
//
//            // when
//            List<Walkway> result = walkwayQueryService.getBookmarkWalkway(bookmark, size, lastCreatedAt, memberId);
//
//            // then
//            assertThat(result).hasSize(markedWalkways.size());
//            for(int i=0; i<result.size(); i++){
//                assertThat(result.get(i).getName()).isEqualTo(markedWalkways.get(i).getWalkway().getName());
//            }
//        }
//
//        @Test
//        @DisplayName("산책로가 존재하지 않으면 빈 리스트를 반환한다.")
//        void it_returns_empty_list(){
//            // given
//            Bookmark bookmark = createBookmark(null);
//            reflectField(bookmark, "id", 1L);
//            Integer size = 10;
//            LocalDateTime lastCreatedAt = LocalDateTime.of(2024, 12, 9, 11, 11);
//            Long memberId = 1L;
//            when(markedWalkwayQueryDSLRepository.getBookmarkWalkway(bookmark.getId(), size, lastCreatedAt, memberId)).thenReturn(Collections.emptyList());
//
//            // when
//            List<Walkway> result = walkwayQueryService.getBookmarkWalkway(bookmark, size, lastCreatedAt, memberId);
//
//            // then
//            assertThat(result).isEmpty();
//        }
//    }
//
//    @Nested
//    @DisplayName("searchWalkway 메서드는")
//    class Describe_searchWalkway {
//        @Test
//        @DisplayName("검색 결과를 반환한다.")
//        void it_returns_list() {
//            WalkwaySort sort = WalkwaySort.LIKED; // 테스트 대상 Sort
//            List<SearchWalkwayResult> searchWalkwayResults = List.of(); // 예상 결과 생성
//
//            when(serviceFactory.getService(sort)).thenReturn(searchWalkwayService);
//            when(searchWalkwayService.search(any())).thenReturn(searchWalkwayResults);
//
//            // When
//            List<SearchWalkwayResult> result = walkwayQueryService.searchWalkway(null, sort);
//
//            // Then
//            verify(serviceFactory).getService(sort);
//            verify(searchWalkwayService).search(null);
//            assertThat(result).isEqualTo(searchWalkwayResults);
//        }
//    }
//
//    @Nested
//    @DisplayName("isMarkedWalkway 메서드는")
//    class Describe_isMarkedWalkway {
//
//        @Test
//        @DisplayName("walkwayId와 memberId가 존재하면 true를 반환한다.")
//        void it_returns_true_if_marked_walkway_exists() {
//            // given
//            Long walkwayId = 1L;
//            Long memberId = 1L;
//
//            when(markedWalkwayQueryDSLRepository.existsMarkedWalkwayByMemberAndWalkway(walkwayId, memberId))
//                    .thenReturn(true);
//
//            // when
//            boolean result = walkwayQueryService.isMarkedWalkway(walkwayId, memberId);
//
//            // then
//            assertThat(result).isTrue();
//        }
//
//    }
//}