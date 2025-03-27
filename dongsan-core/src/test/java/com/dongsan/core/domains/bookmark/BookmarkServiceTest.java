package com.dongsan.core.domains.bookmark;

import static bookmark.BookmarkFixture.*;
import static bookmark.MarkedWalkwayFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.WalkwayReader;
import com.dongsan.core.domains.walkway.WalkwayValidator;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkService Unit Test")
class BookmarkServiceTest {
	@InjectMocks
	BookmarkService bookmarkService;
	@Mock
	BookmarkReader bookmarkReader;
	@Mock
	BookmarkWriter bookmarkWriter;
	@Mock
	BookmarkValidator bookmarkValidator;
	@Mock
	WalkwayReader walkwayReader;
	@Mock
	WalkwayValidator walkwayValidator;

	@Nested
	@DisplayName("createBookmark 메서드는")
	class Describe_createBookmark {
		@Test
		void 생성한_북마크의_Id를_반환한다() {
			// given
			Long memberId = 1L;
			String name = "북마크1";
			Long bookmarkId = 10L;
			when(bookmarkWriter.createBookmark(memberId, name)).thenReturn(bookmarkId);

			// when
			Long result = bookmarkService.createBookmark(memberId, name);

			// then
			assertThat(result).isEqualTo(bookmarkId);
			verify(bookmarkValidator).validateUniqueBookmarkName(memberId, name);
			verify(bookmarkWriter).createBookmark(memberId, name);
		}
	}

	@Nested
	@DisplayName("renameBookmark 메서드는")
	class Describe_renameBookmark {
		@Test
		void 북마크_이름을_변경한다() {
			// given
			Long memberId = 1L;
			Long bookmarkId = 10L;
			String newName = "새로운 북마크 이름";
			Bookmark bookmark = createBookmark(10L, "기존 북마크 이름");
			when(bookmarkReader.getBookmark(bookmarkId)).thenReturn(bookmark);

			// when
			bookmarkService.renameBookmark(memberId, bookmarkId, newName);

			// then
			verify(bookmarkReader).getBookmark(bookmarkId);
			verify(bookmarkValidator).validateBookmarkOwner(memberId, bookmark);
			verify(bookmarkValidator).validateUniqueBookmarkName(memberId, newName);
			verify(bookmarkWriter).renameBookmark(bookmarkId, newName);
		}
	}

	@Nested
	@DisplayName("includeWalkway 메서드는")
	class Describe_includeWalkway {
		@Test
		void 북마크에_산책로를_추가한다() {
			// given
			Long memberId = 1L;
			Long bookmarkId = 10L;
			Long walkwayId = 20L;
			Bookmark bookmark = createBookmark(bookmarkId, "북마크1");
			when(bookmarkReader.getBookmark(bookmarkId)).thenReturn(bookmark);

			// when
			bookmarkService.includeWalkway(memberId, bookmarkId, walkwayId);

			// then
			verify(bookmarkReader).getBookmark(bookmarkId);
			verify(walkwayValidator).validateWalkwayExists(walkwayId);
			verify(bookmarkValidator).validateBookmarkOwner(memberId, bookmark);
			verify(bookmarkValidator).validateWalkwayNotInBookmark(bookmarkId, walkwayId);
			verify(bookmarkWriter).includeWalkway(bookmarkId, walkwayId);
		}
	}

	@Nested
	@DisplayName("excludeWalkway 메서드는")
	class Describe_excludeWalkway {
		@Test
		void 북마크에_산책로를_제외한다() {
			// given
			Long memberId = 1L;
			Long bookmarkId = 10L;
			Long walkwayId = 20L;
			Bookmark bookmark = createBookmark(bookmarkId, "북마크1");
			when(bookmarkReader.getBookmark(bookmarkId)).thenReturn(bookmark);

			// when
			bookmarkService.excludeWalkway(memberId, bookmarkId, walkwayId);

			// then
			verify(bookmarkReader).getBookmark(bookmarkId);
			verify(walkwayValidator).validateWalkwayExists(walkwayId);
			verify(bookmarkValidator).validateBookmarkOwner(memberId, bookmark);
			verify(bookmarkValidator).validateWalkwayExistsInBookmark(bookmarkId, walkwayId);
			verify(bookmarkWriter).excludeWalkway(bookmarkId, walkwayId);
		}
	}

	@Nested
	@DisplayName("deleteBookmark 메서드는")
	class Describe_deleteBookmark {
		@Test
		void 산책로를_삭제한다() {
			// given
			Long memberId = 1L;
			Long bookmarkId = 10L;
			Bookmark bookmark = createBookmark(bookmarkId, "북마크1");
			when(bookmarkReader.getBookmark(bookmarkId)).thenReturn(bookmark);

			// when
			bookmarkService.deleteBookmark(memberId, bookmarkId);

			// then
			verify(bookmarkReader).getBookmark(bookmarkId);
			verify(bookmarkValidator).validateBookmarkOwner(memberId, bookmark);
			verify(bookmarkWriter).deleteBookmark(bookmarkId);
		}
	}

	@Nested
	@DisplayName("getBookmarkWalkways 메서드는")
	class Describe_getBookmarkWalkways {
		@Test
		void lastId가_null이면_북마크에_저장된_산책로를_첫_페이지를_반환한다() {
			// given
			Long memberId = 1L;
			Long bookmarkId = 10L;
			CursorRequest paging = new CursorRequest(null, 10);
			Bookmark bookmark = createBookmark(bookmarkId, "북마크1");
			List<MarkedWalkway> markedWalkways = new ArrayList<>(List.of(
				createMarkedWalkway(1L, memberId),
				createMarkedWalkway(2L, memberId),
				createMarkedWalkway(3L, memberId)
			));
			when(bookmarkReader.getBookmark(bookmarkId)).thenReturn(bookmark);
			when(bookmarkReader.getBookmarkWalkway(bookmarkId, paging.size() + 1, null, memberId)).thenReturn(
				markedWalkways);

			// when
			PagingResponse<MarkedWalkway> result = bookmarkService.getBookmarkWalkways(memberId, bookmarkId, paging);

			// then
			assertThat(result.data()).hasSize(markedWalkways.size());
			assertThat(result.hasNext()).isFalse();
			verify(bookmarkReader).getBookmark(bookmarkId);
			verify(bookmarkValidator).validateBookmarkOwner(memberId, bookmark);
			verify(bookmarkReader).getBookmarkWalkway(bookmarkId, paging.size() + 1, null, memberId);
		}

		@Test
		void lastId가_존재하면_커서_다음으로_북마크에_저장된_산책로를_반환한다() {
			// given
			Long memberId = 1L;
			Long bookmarkId = 10L;
			CursorRequest paging = new CursorRequest(1L, 10);
			Bookmark bookmark = createBookmark(bookmarkId, "북마크1");
			Walkway walkway = new Walkway(paging.lastId(), null, null, null, null, null, null, null,
				null); // TODO : fixture로 추후 변경
			LocalDateTime lastCreatedAt = LocalDateTime.now()
				.minusHours(10);
			List<MarkedWalkway> markedWalkways = new ArrayList<>(List.of(
				createMarkedWalkway(2L, memberId),
				createMarkedWalkway(3L, memberId),
				createMarkedWalkway(4L, memberId)
			));
			when(bookmarkReader.getBookmark(bookmarkId)).thenReturn(bookmark);
			when(walkwayReader.getWalkway(paging.lastId())).thenReturn(walkway);
			when(bookmarkReader.getBookmarkedDate(bookmarkId, walkway.walkwayId())).thenReturn(lastCreatedAt);
			when(bookmarkReader.getBookmarkWalkway(bookmarkId, paging.size() + 1, lastCreatedAt, memberId)).thenReturn(
				markedWalkways);

			// when
			PagingResponse<MarkedWalkway> result = bookmarkService.getBookmarkWalkways(memberId, bookmarkId, paging);

			// then
			assertThat(result.data()).hasSize(markedWalkways.size());
			assertThat(result.hasNext()).isFalse();
			verify(bookmarkReader).getBookmark(bookmarkId);
			verify(bookmarkValidator).validateBookmarkOwner(memberId, bookmark);
			verify(walkwayReader).getWalkway(paging.lastId());
			verify(bookmarkReader).getBookmarkedDate(bookmarkId, walkway.walkwayId());
			verify(bookmarkReader).getBookmarkWalkway(bookmarkId, paging.size() + 1, lastCreatedAt, memberId);
		}

		@Test
		void 다음_페이지에_데이터가_존재하면_hasNext에_true를_반환한다() {
			// given
			Long memberId = 1L;
			Long bookmarkId = 10L;
			CursorRequest paging = new CursorRequest(null, 2);
			Bookmark bookmark = createBookmark(bookmarkId, "북마크1");
			List<MarkedWalkway> markedWalkways = new ArrayList<>(List.of(
				createMarkedWalkway(1L, memberId),
				createMarkedWalkway(2L, memberId),
				createMarkedWalkway(3L, memberId)
			));
			when(bookmarkReader.getBookmark(bookmarkId)).thenReturn(bookmark);
			when(bookmarkReader.getBookmarkWalkway(bookmarkId, paging.size() + 1, null, memberId)).thenReturn(
				markedWalkways);

			// when
			PagingResponse<MarkedWalkway> result = bookmarkService.getBookmarkWalkways(memberId, bookmarkId, paging);

			// then
			assertThat(result.data()).hasSize(2);
			assertThat(result.hasNext()).isTrue();
			verify(bookmarkReader).getBookmark(bookmarkId);
			verify(bookmarkValidator).validateBookmarkOwner(memberId, bookmark);
			verify(bookmarkReader).getBookmarkWalkway(bookmarkId, paging.size() + 1, null, memberId);
		}
	}

	@Nested
	@DisplayName("getUserBookmarksName 메서드는")
	class Describe_getUserBookmarksName {
		@Test
		void lastId가_null이면_북마크들의_첫_페이지를_반환한다() {
			// given
			Long memberId = 1L;
			CursorRequest paging = new CursorRequest(null, 10);
			List<Bookmark> bookmarks = new ArrayList<>(List.of(
				createBookmark(),
				createBookmark(),
				createBookmark()
			));
			when(bookmarkReader.getUserBookmarkNames(null, memberId, paging.size())).thenReturn(bookmarks);

			// when
			PagingResponse<Bookmark> result = bookmarkService.getUserBookmarksName(memberId, paging);

			// then
			assertThat(result.data()).hasSize(bookmarks.size());
			assertThat(result.hasNext()).isFalse();
			verify(bookmarkReader).getUserBookmarkNames(null, memberId, paging.size());
		}

		@Test
		void lastId가_존재하면_커서_다음의_북마크들을_반환한다() {
			// given
			Long memberId = 1L;
			CursorRequest paging = new CursorRequest(1L, 10);
			Bookmark bookmark = createBookmark();
			List<Bookmark> bookmarks = new ArrayList<>(List.of(
				createBookmark(),
				createBookmark(),
				createBookmark()
			));
			when(bookmarkReader.getBookmark(paging.lastId())).thenReturn(bookmark);
			when(bookmarkReader.getUserBookmarkNames(bookmark.createdAt(), memberId, paging.size())).thenReturn(
				bookmarks);

			// when
			PagingResponse<Bookmark> result = bookmarkService.getUserBookmarksName(memberId, paging);

			// then
			assertThat(result.data()).hasSize(bookmarks.size());
			assertThat(result.hasNext()).isFalse();
			verify(bookmarkReader).getBookmark(paging.lastId());
			verify(bookmarkReader).getUserBookmarkNames(bookmark.createdAt(), memberId, paging.size());
		}

	}

	@Nested
	@DisplayName("getBookmarksWithMarkedWalkway 메서드는")
	class Describe_getBookmarksWithMarkedWalkway {
		@Test
		void lastId가_null이면_북마크들의_첫_페이지를_반환한다() {
			// given
			Long memberId = 1L;
			Long walkwayId = 10L;
			CursorRequest paging = new CursorRequest(null, 10);
			List<BookmarkWithMarkedStatus> bookmarks = new ArrayList<>(List.of(
				createBookmarkWithMarkedStatus(),
				createBookmarkWithMarkedStatus(),
				createBookmarkWithMarkedStatus()
			));
			when(bookmarkReader.getBookmarksWithMarkedStatus(walkwayId, memberId, null, paging.size())).thenReturn(
				bookmarks);

			// when
			PagingResponse<BookmarkWithMarkedStatus> result = bookmarkService.getBookmarksWithMarkedWalkway(memberId,
				walkwayId, paging);

			// then
			assertThat(result.data()).hasSize(bookmarks.size());
			assertThat(result.hasNext()).isFalse();
			verify(walkwayValidator).validateWalkwayExists(walkwayId);
			verify(bookmarkReader).getBookmarksWithMarkedStatus(walkwayId, memberId, null, paging.size());
		}

		@Test
		void lastId가_존재하면_커서_다음의_북마크들을_반환한다() {
			// given
			Long memberId = 1L;
			Long walkwayId = 10L;
			CursorRequest paging = new CursorRequest(1L, 10);
			Bookmark bookmark = createBookmark();
			List<BookmarkWithMarkedStatus> bookmarks = new ArrayList<>(List.of(
				createBookmarkWithMarkedStatus(),
				createBookmarkWithMarkedStatus(),
				createBookmarkWithMarkedStatus()
			));
			when(bookmarkReader.getBookmark(paging.lastId())).thenReturn(bookmark);
			when(bookmarkReader.getBookmarksWithMarkedStatus(walkwayId, memberId, bookmark.createdAt(),
				paging.size())).thenReturn(bookmarks);

			// when
			PagingResponse<BookmarkWithMarkedStatus> result = bookmarkService.getBookmarksWithMarkedWalkway(memberId,
				walkwayId, paging);

			// then
			assertThat(result.data()).hasSize(bookmarks.size());
			assertThat(result.hasNext()).isFalse();
			verify(walkwayValidator).validateWalkwayExists(walkwayId);
			verify(bookmarkReader).getBookmark(paging.lastId());
			verify(bookmarkReader).getBookmarksWithMarkedStatus(walkwayId, memberId, bookmark.createdAt(),
				paging.size());
		}
	}

	@Nested
	@DisplayName("existsMarkedWalkway 메서드는")
	class Describe_existsMarkedWalkway {
		@Test
		void 북마크에_추가된_산책로이면_true를_반환한다() {
			// given
			Long memberId = 1L;
			Long walkwayId = 10L;
			when(bookmarkReader.existsByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(true);

			// when
			boolean result = bookmarkService.existsMarkedWalkway(memberId, walkwayId);

			// then
			assertThat(result).isTrue();
		}

		@Test
		void 북마크에_추가된_산책로가_아니면_false를_반환한다() {
			// given
			Long memberId = 1L;
			Long walkwayId = 20L;
			when(bookmarkReader.existsByMemberIdAndWalkwayId(memberId, walkwayId)).thenReturn(false);

			// when
			boolean result = bookmarkService.existsMarkedWalkway(memberId, walkwayId);

			// then
			assertThat(result).isFalse();
		}
	}

	@Nested
	@DisplayName("existsMarkedWalkways 메서드는")
	class Describe_existsMarkedWalkways {
		@Test
		void 북마크_Ids가_없으면_빈_map을_반환한다() {
			// given
			Long walkwayId = 10L;
			List<Long> bookmarkIds = Collections.emptyList();
			when(bookmarkReader.existsMarkedWalkway(walkwayId, bookmarkIds)).thenReturn(Collections.emptyMap());

			// when
			Map<Long, Boolean> result = bookmarkService.existsMarkedWalkways(walkwayId, bookmarkIds);

			// then
			assertThat(result).isEmpty();
		}

		@Test
		void 북마크_Ids가_존재하면_북마크_추가여부를_map에_반환한다() {
			// given
			Long walkwayId = 10L;
			List<Long> bookmarkIds = List.of(1L, 2L, 3L);
			Map<Long, Boolean> bookmarksWithMarkedStatus = Map.of(1L, true, 2L, false, 3L, true);
			when(bookmarkReader.existsMarkedWalkway(walkwayId, bookmarkIds)).thenReturn(bookmarksWithMarkedStatus);

			// when
			Map<Long, Boolean> result = bookmarkService.existsMarkedWalkways(walkwayId, bookmarkIds);

			// then
			assertThat(result).isEqualTo(bookmarksWithMarkedStatus);
			assertThat(result.get(1L)).isTrue();
			assertThat(result.get(2L)).isFalse();
			assertThat(result.get(3L)).isTrue();
		}
	}

}
