package com.dongsan.core.domains.bookmark;

import static bookmark.BookmarkFixture.createBookmark;
import static bookmark.BookmarkFixture.createBookmarkWithMarkedStatus;
import static bookmark.MarkedWalkwayFixture.createMarkedWalkway;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import java.time.LocalDateTime;
import java.util.Collections;
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
@DisplayName("BookmarkQueryService Unit Test")
class BookmarkReaderTest {
    @InjectMocks
    BookmarkReader bookmarkReader;
    @Mock
    BookmarkRepository bookmarkRepository;

    @Nested
    @DisplayName("getBookmark 메서드는")
    class Describe_getBookmark{
        @Test
        void 북마크가_존재하지_않으면_예외를_반환한다(){
            // given
            Long bookmarkId = 1L;
            when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.empty());

            // when & then
            CoreException thrown = assertThrows(CoreException.class, () -> {
                bookmarkReader.getBookmark(bookmarkId);
            });
            assertEquals(CoreErrorCode.BOOKMARK_NOT_EXIST, thrown.getErrorCode());
        }

        @Test
        void 북마크가_존재하면_북마크를_반환한다(){
            // given
            Long bookmarkId = 1L;
            Bookmark bookmark = createBookmark(bookmarkId, "북마크");
            when(bookmarkRepository.findById(bookmarkId)).thenReturn(Optional.of(bookmark));

            // when
            Bookmark result = bookmarkReader.getBookmark(bookmarkId);

            // then
            assertThat(result.bookmarkId()).isEqualTo(bookmarkId);
        }
    }

    @Nested
    @DisplayName("existsById 메서드는")
    class Describe_existsById{
        @Test
        void 북마크가_존재하면_true를_반환한다(){
            // given
            Long bookmarkId = 1L;
            when(bookmarkRepository.existsById(bookmarkId)).thenReturn(true);

            // when
            boolean result = bookmarkReader.existsById(bookmarkId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 북마크가_존재하지_않으면_false를_반환한다(){
            // given
            Long bookmarkId = 1L;
            when(bookmarkRepository.existsById(bookmarkId)).thenReturn(false);

            // when
            boolean result = bookmarkReader.existsById(bookmarkId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("getBookmarkedDate")
    class Describe_getBookmarkedDate{
        @Test
        void 북마크에_산책로가_포함되지_않으면_예외를_반환한다(){
            // given
            Long bookmarkId = 1L;
            Long walkwayId = 2L;
            when(bookmarkRepository.getBookmarkedDate(bookmarkId, walkwayId)).thenReturn(Optional.empty());

            // when & then
            CoreException thrown = assertThrows(CoreException.class, () -> {
                bookmarkReader.getBookmarkedDate(bookmarkId, walkwayId);
            });
            assertEquals(CoreErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK, thrown.getErrorCode());
        }

        @Test
        void 북마크에_산책로가_포함되면_산책로가_북마크에_추가된_날짜를_반환한다(){
            // given
            Long bookmarkId = 1L;
            Long walkwayId = 2L;
            LocalDateTime includedAt = LocalDateTime.now();
            when(bookmarkRepository.getBookmarkedDate(bookmarkId, walkwayId)).thenReturn(Optional.of(includedAt));

            // when
            LocalDateTime result = bookmarkReader.getBookmarkedDate(bookmarkId, walkwayId);

            // then
            assertThat(result).isEqualTo(includedAt);
        }
    }

    @Nested
    @DisplayName("getBookmarkWalkway 메서드는")
    class Describe_getBookmarkWalkway{
        @Test
        void 산책로가_존재하면_산책로를_리스트로_반환한다(){
            // given
            Long bookmarkId = 1L;
            int size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            Long memberId = 1L;
            List<MarkedWalkway> markedWalkways = List.of(
                    createMarkedWalkway(),
                    createMarkedWalkway()
            );
            when(bookmarkRepository.getBookmarkWalkways(bookmarkId, size, lastCreatedAt, memberId)).thenReturn(markedWalkways);

            // when
            List<MarkedWalkway> result = bookmarkReader.getBookmarkWalkway(bookmarkId, size, lastCreatedAt, memberId);

            // then
            assertThat(result).hasSize(markedWalkways.size());
            for(int i=0; i<result.size(); i++){
                assertThat(result.get(i)).isEqualTo(markedWalkways.get(i));
            }
        }

        @Test
        void 산책로가_존재하지_않으면_빈_리스트를_반환한다(){
            // given
            Long bookmarkId = 1L;
            int size = 10;
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            Long memberId = 1L;
            when(bookmarkRepository.getBookmarkWalkways(bookmarkId, size, lastCreatedAt, memberId)).thenReturn(
                    Collections.emptyList());

            // when
            List<MarkedWalkway> result = bookmarkReader.getBookmarkWalkway(bookmarkId, size, lastCreatedAt, memberId);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getUserBookmarkNames 메소드는")
    class Describe_getUserBookmarkNames{
        @Test
        void 북마크가_존재하면_북마크들의_이름을_리스트로_반환한다(){
            // given
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            Long memberId = 1L;
            Integer size = 10;
            List<Bookmark> bookmarks = List.of(
                    createBookmark(1L, "북마크1"),
                    createBookmark(2L, "북마크2"),
                    createBookmark(3L, "북마크3")
            );
            when(bookmarkRepository.getUserBookmarks(size, lastCreatedAt, memberId)).thenReturn(bookmarks);

            // when
            List<Bookmark> result = bookmarkReader.getUserBookmarkNames(lastCreatedAt, memberId, size);

            // then
            assertThat(result).hasSize(bookmarks.size());
            for(int i=0; i<result.size(); i++){
                assertThat(result.get(i)).isEqualTo(bookmarks.get(i));
            }
        }

        @Test
        void 북마크가_존재하지_않으면_빈_리스트를_반환한다(){
            // given
            LocalDateTime lastCreatedAt = LocalDateTime.now();
            Long memberId = 1L;
            Integer size = 10;
            when(bookmarkRepository.getUserBookmarks(size, lastCreatedAt, memberId)).thenReturn(Collections.emptyList());

            // when
            List<Bookmark> result = bookmarkReader.getUserBookmarkNames(lastCreatedAt, memberId, size);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getBookmarksWithMarkedStatus 메소드는")
    class Describe_getBookmarksWithMarkedStatus{
        @Test
        void 북마크가_존재하면_북마크_추가여부를_리스트로_반환한다(){
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            LocalDateTime createdAt = LocalDateTime.now();
            Integer size = 10;
            List<BookmarkWithMarkedStatus> bookmarks = List.of(
                    createBookmarkWithMarkedStatus(1L, "북마크1", true),
                    createBookmarkWithMarkedStatus(2L, "북마크2", false),
                    createBookmarkWithMarkedStatus(3L, "북마크3", true),
                    createBookmarkWithMarkedStatus(4L, "북마크4", false)
            );
            when(bookmarkRepository.getBookmarksWithMarkedWalkway(walkwayId, memberId, createdAt, size)).thenReturn(bookmarks);

            // when
            List<BookmarkWithMarkedStatus> result = bookmarkReader.getBookmarksWithMarkedStatus(walkwayId, memberId, createdAt, size);

            // then
            assertThat(result).hasSize(bookmarks.size());
            for(int i=0; i<result.size(); i++){
                assertThat(result.get(i)).isEqualTo(bookmarks.get(i));
            }
        }

        @Test
        void 북마크가_존재하지_않으면_빈_리스트를_반환한다(){
            // given
            Long walkwayId = 1L;
            Long memberId = 1L;
            LocalDateTime createdAt = LocalDateTime.now();
            Integer size = 10;
            when(bookmarkRepository.getBookmarksWithMarkedWalkway(walkwayId, memberId, createdAt, size)).thenReturn(Collections.emptyList());

            // when
            List<BookmarkWithMarkedStatus> result = bookmarkReader.getBookmarksWithMarkedStatus(walkwayId, memberId, createdAt, size);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsByMemberIdAndWalkwayId 메소드는")
    class Describe_existsByMemberIdAndWalkwayId{
        @Test
        void 북마크가_존재하면_true를_반환한다(){
            // given
            Long memberId = 1L;
            Long walkwayId = 2L;
            when(bookmarkRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(true);

            // when
            boolean result = bookmarkReader.existsByMemberIdAndWalkwayId(memberId, walkwayId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 북마크가_존재하지_않으면_false를_반환한다(){
            // given
            Long memberId = 1L;
            Long walkwayId = 3L;
            when(bookmarkRepository.existsByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(false);

            // when
            boolean result = bookmarkReader.existsByMemberIdAndWalkwayId(memberId, walkwayId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsMarkedWalkway 메소드는")
    class Describe_existsMarkedWalkway {
        @Test
        void 리스트에_북마크가_존재하면_북마크_포함_여부를_map으로_반환한다(){
            // given
            Long walkwayId = 1L;
            List<Long> emptyBookmarkIds = List.of(10L, 11L, 12L);
            Map<Long, Boolean> expectedResult = Map.of(
                    10L, true,
                    11L, false,
                    12L, true
            );
            when(bookmarkRepository.existsMarkedWalkway(walkwayId, emptyBookmarkIds)).thenReturn(expectedResult);

            // when
            Map<Long, Boolean> result = bookmarkReader.existsMarkedWalkway(walkwayId, emptyBookmarkIds);

            // then
            assertEquals(expectedResult.size(), result.size());
            assertEquals(true, result.get(10L));
            assertEquals(false, result.get(11L));
            assertEquals(true, result.get(12L));
        }

        @Test
        void 빈_북마크_리스트를_전달하면_빈_map를_반환한다() {
            // given
            Long walkwayId = 1L;
            List<Long> emptyBookmarkIds = Collections.emptyList();
            when(bookmarkRepository.existsMarkedWalkway(walkwayId, emptyBookmarkIds)).thenReturn(Collections.emptyMap());

            // when
            Map<Long, Boolean> result = bookmarkReader.existsMarkedWalkway(walkwayId, emptyBookmarkIds);

            // then
            assertThat(result).isEmpty();
        }
    }

}


// Validaion 에 있어야 하는 칭구들

//@Nested
//@DisplayName("isOwnerOfBookmark 메서드는")
//class Describe_isOwnerOfBookmark{
//    @Test
//    @DisplayName("북마크 생성자가 아니면 예외를 반환한다.")
//    void it_throws_exception(){
//        // given
//        Member owner = createMemberWithId(1L);
//        Member notOwner = createMemberWithId(2L);
//        Bookmark bookmark = createBookmark(owner);
//
//        // when & then
//        CustomException thrown = assertThrows(CustomException.class, () -> {
//            bookmarkReader.isOwnerOfBookmark(notOwner, bookmark);
//        });
//        assertEquals(BookmarkErrorCode.NOT_BOOKMARK_OWNER, thrown.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("북마크 생성자가 맞으면 예외를 반환하지 않는다.")
//    void it_dont_throws_exception(){
//        // given
//        Member owner = createMemberWithId(1L);
//        Bookmark bookmark = createBookmark(owner);
//
//        // when & then
//        assertDoesNotThrow(() -> {
//            bookmarkReader.isOwnerOfBookmark(owner, bookmark);
//        });
//    }
//}

//@Nested
//@DisplayName("hasSameBookmarkName 메서드는")
//class Describe_hasSameBookmarkName{
//    @Test
//    @DisplayName("사용자가 저장한 북마크 중 중복되는 이름이 존재하면 예외를 반환한다.")
//    void it_throws_exception(){
//        // given
//        Long memberId = 1L;
//        String name = "북마크 이름";
//        when(bookmarkRepository.existsByMemberIdAndName(memberId, name)).thenReturn(true);
//
//        // when & then
//        CustomException thrown = assertThrows(CustomException.class, () -> {
//            bookmarkReader.hasSameBookmarkName(memberId, name);
//        });
//        assertEquals(BookmarkErrorCode.SAME_BOOKMARK_NAME_EXIST, thrown.getErrorCode());
//    }
//
//    @Test
//    @DisplayName("사용자가 저장한 북마크 중 중복되는 이름이 존재하지 않으면 예외를 반환하지 않는다.")
//    void it_dont_throws_exception(){
//        // given
//        Long memberId = 1L;
//        String name = "북마크 이름";
//        when(bookmarkRepository.existsByMemberIdAndName(memberId, name)).thenReturn(false);
//
//        // when & then
//        assertDoesNotThrow(() -> {
//            bookmarkReader.hasSameBookmarkName(memberId, name);
//        });
//    }
//}
//
//
//
//@Nested
//@DisplayName("isWalkwayAdded 메서드는")
//class Describe_isWalkwayAdded {
//    @Test
//    @DisplayName("북마크에 포함된 산책로이면 true를 반환한다.")
//    void it_returns_true() {
//        // given
//        Bookmark bookmark = createBookmark(null);
//        reflectField(bookmark, "id", 1L);
//        Walkway walkway = createWalkway(null);
//        reflectField(walkway, "id", 1L);
//        when(markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId())).thenReturn(
//                true);
//
//        // when
//        boolean result = bookmarkReader.isWalkwayAdded(bookmark, walkway);
//
//        // then
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    @DisplayName("북마크에 포함되지 않은 산책로이면 false를 반환한다.")
//    void it_returns_false() {
//        // given
//        Bookmark bookmark = createBookmark(null);
//        reflectField(bookmark, "id", 1L);
//        Walkway walkway = createWalkway(null);
//        reflectField(walkway, "id", 1L);
//        when(markedWalkwayRepository.existsByBookmarkIdAndWalkwayId(bookmark.getId(), walkway.getId())).thenReturn(
//                false);
//
//        // when
//        boolean result = bookmarkReader.isWalkwayAdded(bookmark, walkway);
//
//        // then
//        assertThat(result).isFalse();
//    }
//}