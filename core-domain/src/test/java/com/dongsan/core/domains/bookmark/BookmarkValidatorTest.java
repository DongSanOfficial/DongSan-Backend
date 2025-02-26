package com.dongsan.core.domains.bookmark;

import static bookmark.BookmarkFixture.createBookmark;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkValidator Unit Test")
class BookmarkValidatorTest {
    @InjectMocks
    BookmarkValidator bookmarkValidator;
    @Mock
    BookmarkRepository bookmarkRepository;

    @Nested
    @DisplayName("validateUniqueBookmarkName 메서드는")
    class Describe_validateUniqueBookmarkName{
        @Test
        void 사용자가_저장한_북마크_중_중복되는_이름이_존재하면_예외를_반환한다(){
            // given
            Long memberId = 1L;
            String name = "북마크1";
            when(bookmarkRepository.existsByMemberIdAndName(memberId, name)).thenReturn(true);

            // when & then
            CoreException thrown = assertThrows(CoreException.class, () -> {
                bookmarkValidator.validateUniqueBookmarkName(memberId, name);
            });
            assertEquals(CoreErrorCode.SAME_BOOKMARK_NAME_EXIST, thrown.getErrorCode());
        }

        @Test
        void 사용자가_저장한_북마크_중_중복되는_이름이_존재하지_않으면_예외를_반환하지_않는다(){
            // given
            Long memberId = 1L;
            String name = "북마크 이름";
            when(bookmarkRepository.existsByMemberIdAndName(memberId, name)).thenReturn(false);

            // when & then
            assertDoesNotThrow(() -> {
                bookmarkValidator.validateUniqueBookmarkName(memberId, name);
            });
        }
    }

    @Nested
    @DisplayName("validateBookmarkOwner 메서드는")
    class Describe_isOwnerOfBookmark{
        @Test
        void 북마크_생성자가_아니면_예외를_반환한다(){
            // given
            Long notAuthor = 1L;
            Bookmark bookmark = createBookmark(notAuthor + 5);

            // when & then
            CoreException thrown = assertThrows(CoreException.class, () -> {
                bookmarkValidator.validateBookmarkOwner(notAuthor, bookmark);
            });
            assertEquals(CoreErrorCode.NOT_BOOKMARK_OWNER, thrown.getErrorCode());
        }

        @Test
        void 북마크_생성자가_맞으면_예외를_반환하지_않는다(){
            // given
            Long author = 1L;
            Bookmark bookmark = createBookmark(author);

            // when & then
            assertDoesNotThrow(() -> {
                bookmarkValidator.validateBookmarkOwner(author, bookmark);
            });
        }
    }

    @Nested
    @DisplayName("validateWalkwayNotInBookmark 메서드는")
    class Describe_validateWalkwayNotInBookmark{
        @Test
        void 산책로가_북마크에_존재하면_예외를_반환한다(){
            // given
            Long bookmarkId = 1L;
            Long existWalkwayId = 2L;
            when(bookmarkRepository.isWalkwayAdded(bookmarkId, existWalkwayId)).thenReturn(true);

            // when & then
            CoreException thrown = assertThrows(CoreException.class, () -> {
                bookmarkValidator.validateWalkwayNotInBookmark(bookmarkId, existWalkwayId);
            });
            assertEquals(CoreErrorCode.WALKWAY_ALREADY_EXIST_IN_BOOKMARK, thrown.getErrorCode());
        }

        @Test
        void 산책로가_북마크에_존재하지_않으면_예외를_반환하지_않는다(){
            // given
            Long bookmarkId = 1L;
            Long notExistWalkwayId = 3L;
            when(bookmarkRepository.isWalkwayAdded(bookmarkId, notExistWalkwayId)).thenReturn(false);

            // when & then
            assertDoesNotThrow(() -> {
                bookmarkValidator.validateWalkwayNotInBookmark(bookmarkId, notExistWalkwayId);
            });
        }

    }

    @Nested
    @DisplayName("validateWalkwayExistsInBookmark 메소드는")
    class Describe_validateWalkwayExistsInBookmark{
        @Test
        void 산책로가_북마크에_존재하지_않으면_예외를_반환한다(){
            // given
            Long bookmarkId = 1L;
            Long notExistWalkwayId = 3L;
            when(bookmarkRepository.isWalkwayAdded(bookmarkId, notExistWalkwayId)).thenReturn(false);

            // when & then
            CoreException thrown = assertThrows(CoreException.class, () -> {
                bookmarkValidator.validateWalkwayExistsInBookmark(bookmarkId, notExistWalkwayId);
            });
            assertEquals(CoreErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK, thrown.getErrorCode());
        }

        @Test
        void 산책로가_북마크에_존재하면_예외를_반환하지_않는다(){
            // given
            Long bookmarkId = 1L;
            Long existWalkwayId = 2L;
            when(bookmarkRepository.isWalkwayAdded(bookmarkId, existWalkwayId)).thenReturn(true);

            // when & then
            assertDoesNotThrow(() -> {
                bookmarkValidator.validateWalkwayExistsInBookmark(bookmarkId, existWalkwayId);
            });
        }
    }

}
