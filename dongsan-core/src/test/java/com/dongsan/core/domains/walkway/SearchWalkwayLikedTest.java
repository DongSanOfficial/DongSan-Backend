package com.dongsan.core.domains.walkway;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static walkway.WalkwayFixture.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SearchLikedWalkwayService Unit Test")
class SearchWalkwayLikedTest {
	@Mock
	private WalkwayRepository walkwayRepository;
	@InjectMocks
	private SearchWalkwayLiked searchLikedWalkwayService;

	@Nested
	@DisplayName("getSortType 메서드는")
	class Describe_getWalkwaySortType {
		@Test
		@DisplayName("서비스에 해당하는 정렬을 반환한다.")
		void it_returns_sort() {
			assertThat(searchLikedWalkwayService.getSortType()).isEqualTo(WalkwaySort.LIKED);
		}
	}

	@Nested
	@DisplayName("search 메서드는")
	class Describe_search {
		List<Walkway> results = new ArrayList<>();

		@BeforeEach
		void setUp() {
			for (long i = 1; i <= 10; i++) {
				Walkway result = createWalkway();
				results.add(result);
			}
		}

		@Test
		@DisplayName("좋아요순으로 검색한 결과를 반환한다.")
		void it_returns_liked_result() {
			// Given
			Long userId = 1L;
			Double latitude = 2.0;
			Double longitude = 2.0;
			Double distance = 10.0;
			Long lastWalkwayId = 1L;
			int size = 10;

			SearchWalkwayQuery searchWalkwayRequest
				= new SearchWalkwayQuery(userId, longitude, latitude, distance, lastWalkwayId, size);

			when(walkwayRepository.searchWalkwaysLiked(searchWalkwayRequest)).thenReturn(results);

			// When
			List<Walkway> result = searchLikedWalkwayService.search(searchWalkwayRequest);

			// Then
			assertThat(result).isNotNull()
				.hasSize(size);
		}
	}
}
