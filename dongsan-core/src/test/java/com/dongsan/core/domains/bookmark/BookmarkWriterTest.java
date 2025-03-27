package com.dongsan.core.domains.bookmark;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkWriter Unit Test")
class BookmarkWriterTest {
	@InjectMocks
	BookmarkWriter bookmarkWriter;
	@Mock
	BookmarkRepository bookmarkRepository;

	@Nested
	@DisplayName("createBookmark 메서드는")
	class Describe_createBookmark {
		@Test
		void 북마크를_생성한다() {
			// given
			Long memberId = 1L;
			String name = "북마크1";
			Long bookmarkId = 1L;
			when(bookmarkRepository.save(memberId, name)).thenReturn(bookmarkId);

			// when
			Long result = bookmarkWriter.createBookmark(memberId, name);

			// then
			assertThat(result).isEqualTo(bookmarkId);
			verify(bookmarkRepository).save(memberId, name);
		}
	}

	@Nested
	@DisplayName("renameBookmark 메서드는")
	class Describe_renameBookmark {
		@Test
		void 북마크_이름을_변경한다() {
			// given
			Long bookmarkId = 1L;
			String newName = "수정된 북마크 이름";

			// when
			bookmarkWriter.renameBookmark(bookmarkId, newName);

			// then
			verify(bookmarkRepository).rename(bookmarkId, newName);
		}
	}

	@Nested
	@DisplayName("includeWalkway 메서드는")
	class Describe_includeWalkway {
		@Test
		void 북마크에_산책로를_추가한다() {
			// given
			Long bookmarkId = 1L;
			Long walkwayId = 2L;

			// when
			bookmarkWriter.includeWalkway(bookmarkId, walkwayId);

			// then
			verify(bookmarkRepository).includeWalkway(bookmarkId, walkwayId);
		}
	}

	@Nested
	@DisplayName("excludeWalkway 메서드는")
	class Describe_excludeWalkway {
		@Test
		void 북마크에_산책로를_제외한다() {
			// given
			Long bookmarkId = 1L;
			Long walkwayId = 2L;

			// when
			bookmarkWriter.excludeWalkway(bookmarkId, walkwayId);

			// then
			verify(bookmarkRepository).excludeWalkway(bookmarkId, walkwayId);
		}
	}

	@Nested
	@DisplayName("deleteBookmark 메서드는")
	class Describe_deleteBookmark {
		@Test
		void 북마크를_삭제한다() {
			// given
			Long bookmarkId = 1L;

			// when
			bookmarkWriter.deleteBookmark(bookmarkId);

			// then
			verify(bookmarkRepository).deleteById(bookmarkId);
		}
	}

}
