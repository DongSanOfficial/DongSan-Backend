//package com.dongsan.core.domains.walkway.usecase;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.core.domains.walkway.usecase.HashtagService;
//import com.dongsan.core.domains.walkway.WalkwayService;
//import com.dongsan.common.error.exception.CustomException;
//import com.dongsan.domains.image.entity.Image;
//import com.dongsan.core.domains.image.ImageReader;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.core.domains.member.MemberReader;
//import com.dongsan.core.domains.walkway.dto.WalkwayCoordinate;
//import com.dongsan.core.domains.walkway.dto.request.CreateWalkwayRequest;
//import com.dongsan.domains.walkway.dto.request.SearchWalkwayQuery;
//import com.dongsan.core.domains.walkway.dto.request.UpdateWalkwayRequest;
//import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.domains.walkway.enums.ExposeLevel;
//import com.dongsan.core.domains.walkway.WalkwaySort;
//import com.dongsan.core.domains.walkway.service.HashtagReader;
//import com.dongsan.core.domains.walkway.service.HashtagWalkwayWriter;
//import com.dongsan.core.domains.walkway.WalkwayWriter;
//import com.dongsan.core.domains.walkway.WalkwayReader;
//import fixture.ImageFixture;
//import member.MemberFixture;
//import fixture.WalkwayFixture;
//import java.util.ArrayList;
//import java.util.List;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("WalkwayUseCase Unit Test")
//class WalkwayUseCaseTest {
//
//    @Mock
//    WalkwayWriter walkwayCommandService;
//    @Mock
//    WalkwayReader walkwayQueryService;
//    @Mock
//    MemberReader memberReader;
//    @Mock
//    HashtagWalkwayWriter hashtagWalkwayWriter;
//    @Mock
//    HashtagReader hashtagReader;
//    @Mock
//    HashtagService hashtagUseCase;
//    @Mock
//    ImageReader imageReader;
//    @InjectMocks
//    WalkwayService walkwayUseCase;
//
//    @Nested
//    @DisplayName("createWalkway 메서드는")
//    class Describe_createWalkway {
//
//        @Test
//        @DisplayName("요청한 유저가 존재하면 산책로를 등록하고 산책로 id를 DTO로 반환한다.")
//        void it_returns_responseDTO() {
//            // Given
//            Long memberId = 1L;
//            Long imageId = 1L;
//            Image image = ImageFixture.createImage();
//            Member member = MemberFixture.createMemberWithId(memberId);
//            List<WalkwayCoordinate> course = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                course.add(new WalkwayCoordinate(0.0, 0.0));
//            }
//            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
//                    imageId,
//                    "testName",
//                    "testMemo",
//                    4.2,
//                    20,
//                    List.of("하나", "둘"),
//                    ExposeLevel.PUBLIC,
//                    course
//            );
//
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, member);
//
//            when(memberReader.readMember(member.getId())).thenReturn(member);
//            when(imageReader.getImage(imageId)).thenReturn(image);
//            when(walkwayCommandService.createWalkway(any()))
//                    .thenReturn(walkway);
//
//            // When
//            Walkway result =
//                    walkwayUseCase.createWalkway(createWalkwayRequest, memberId);
//
//            // Then
//            assertThat(result.getId()).isEqualTo(1L);
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkwayWithLiked 메서드는")
//    class Describe_getWalkwayWithLiked {
//        @Test
//        @DisplayName("walkwayId에 해당하는 산책로가 없으면 예외처리 한다.")
//        void it_returns_exception_not_found_walkway() {
//            // given
//            when(walkwayQueryService.getWalkwayWithHashtagAndLike(1L, 1L)).thenReturn(null);
//
//            // when & then
//            assertThrows(CustomException.class, () -> {
//                walkwayUseCase.getWalkwayWithLiked(1L, 1L);
//            });
//        }
//
//        @Test
//        @DisplayName("walkwayId에 해당하는 산책로가 있으면 DTO를 반환한다.")
//        void it_returns_DTO() {
//            // given
//            Member member = MemberFixture.createMember();
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, member);
//
//            when(walkwayQueryService.getWalkwayWithHashtagAndLike(any(), any())).thenReturn(walkway);
//
//            // when
//            Walkway result = walkwayUseCase.getWalkwayWithLiked(walkway.getId(), member.getId());
//
//            // then
//            Assertions.assertThat(result).isEqualTo(walkway);
//        }
//    }
//
//    @Nested
//    @DisplayName("updateWalkway 메서드는")
//    class Describe_updateWalkway {
//        @Test
//        @DisplayName("산책로를 수정한다.")
//        void it_update_walkway() {
//            // Given
//            Member member = MemberFixture.createMemberWithId(1L);
//            Long walkwayId = 1L;
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(walkwayId, member);
//            UpdateWalkwayRequest updateWalkwayRequest = new UpdateWalkwayRequest(
//                    "test name",
//                    "test memo",
//                    List.of(),
//                    ExposeLevel.PRIVATE
//            );
//
//            when(walkwayQueryService.getWalkway(walkway.getId())).thenReturn(walkway);
//            when(walkwayCommandService.createWalkway(walkway)).thenReturn(null);
//
//            // When
//            walkwayUseCase.updateWalkway(updateWalkwayRequest, member.getId(), walkwayId);
//
//            // Then
//            Assertions.assertThat(walkway.getHashtagWalkways()).isEmpty();
//            Assertions.assertThat(walkway.getName()).isEqualTo("test name");
//            Assertions.assertThat(walkway.getMemo()).isEqualTo("test memo");
//            Assertions.assertThat(walkway.getExposeLevel()).isEqualTo(ExposeLevel.PRIVATE);
//        }
//    }
//
//    @Nested
//    @DisplayName("searchWalkway 메서드는")
//    class Describe_searchWalkway {
//        List<SearchWalkwayResult> searchWalkwayResults = new ArrayList<>();
//
//        @BeforeEach
//        void setUp() {
//            for (long i = 1; i <= 10; i++) {
//                SearchWalkwayResult searchWalkwayResult
//                        = new SearchWalkwayResult(i, null, null, null, null, null, null, null, null, null, null);
//                searchWalkwayResults.add(searchWalkwayResult);
//            }
//        }
//
//        @Test
//        @DisplayName("type이 liked면 좋아요 순으로 산책로를 반환한다.")
//        void it_returns_walkways_popular() {
//            // given
//            Long userId = 1L;
//            String type = "liked";
//            Double latitude = 2.0;
//            Double longitude = 2.0;
//            Double distance = 10.0;
//            Long lastId = null;
//            Walkway lastWalkway = null;
//            int size = 10;
//
//            SearchWalkwayQuery searchWalkwayRequest
//                    = new SearchWalkwayQuery(userId, longitude, latitude, distance, lastWalkway, size);
//
//            WalkwaySort sort = WalkwaySort.typeOf(type);
//            when(walkwayQueryService.searchWalkway(searchWalkwayRequest, sort))
//                    .thenReturn(searchWalkwayResults);
//
//            // when
//            List<SearchWalkwayResult> result
//                    = walkwayUseCase.searchWalkway(userId, type, latitude, longitude, distance, lastId, size);
//
//            // then
//            Assertions.assertThat(result)
//                    .isNotNull()
//                    .hasSize(size);
//        }
//
//        @Test
//        @DisplayName("type이 rating이면 별점 순으로 산책로를 반환한다.")
//        void it_returns_walkways_rating() {
//            // given
//            Long userId = 1L;
//            String type = "rating";
//            Double latitude = 2.0;
//            Double longitude = 2.0;
//            Double distance = 10.0;
//            Long lastId = null;
//            Walkway lastWalkway = null;
//            int size = 10;
//
//            SearchWalkwayQuery searchWalkwayRequest
//                    = new SearchWalkwayQuery(userId, longitude, latitude, distance, lastWalkway, size);
//
//            WalkwaySort sort = WalkwaySort.typeOf(type);
//            when(walkwayQueryService.searchWalkway(searchWalkwayRequest, sort))
//                    .thenReturn(searchWalkwayResults);
//
//            // when
//            List<SearchWalkwayResult> result
//                    = walkwayUseCase.searchWalkway(userId, type, latitude, longitude, distance, lastId, size);
//
//            // then
//            Assertions.assertThat(result)
//                    .isNotNull()
//                    .hasSize(size);
//        }
//
//        @Test
//        @DisplayName("type이 잘 못 입력 되면 예외처리")
//        void it_returns_exception() {
//            // given
//            Long userId = 1L;
//            String type = "으헤헿헿헤헤헤ㅔ헤헤헤헤헿";
//            Double latitude = 2.0;
//            Double longitude = 2.0;
//            Double distance = 10.0;
//            Long lastId = null;
//            int size = 10;
//
//            assertThatThrownBy(
//                    () -> walkwayUseCase.searchWalkway(userId, type, latitude, longitude, distance, lastId, size))
//                    .isInstanceOf(CustomException.class);
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
//            // Mocking: 해당 산책로가 북마크 되어 있는 경우
//            when(walkwayQueryService.isMarkedWalkway(walkwayId, memberId))
//                    .thenReturn(true);
//
//            // when
//            boolean result = walkwayUseCase.isMarkedWalkway(walkwayId, memberId);
//
//            // then
//            assertThat(result).isTrue();
//        }
//    }
//}