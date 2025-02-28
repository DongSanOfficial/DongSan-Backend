package com.dongsan.core.domains.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
@DisplayName("GetReviewsFactory Unit Test")
class GetReviewsFactoryTest {

    @Mock
    private GetLatestReviews getLatestReviews;
    @Mock
    private GetRatingReviews getRatingReviews;
    private GetReviewsFactory factory;

    @BeforeEach
    void setUp() {
        // Mock 서비스의 Sort 반환값 설정
        when(getLatestReviews.getSortType()).thenReturn(ReviewSort.LATEST);
        when(getRatingReviews.getSortType()).thenReturn(ReviewSort.RATING);

        // 팩토리에 서비스 리스트 주입
        factory = new GetReviewsFactory(List.of(getLatestReviews, getRatingReviews));
    }

    @Nested
    @DisplayName("getService 메서드는")
    class Describe_getService {
        @Test
        @DisplayName("Latest가 입력 되면 해당 되는 서비스를 반환한다.")
        void it_returns_latest_service() {
            GetReviews service = factory.getService(ReviewSort.LATEST);
            assertThat(service.getSortType()).isEqualTo(getLatestReviews.getSortType());
        }

        @Test
        @DisplayName("Rating이 입력 되면 해당 되는 서비스를 반환한다.")
        void it_returns_rating_service() {
            GetReviews service = factory.getService(ReviewSort.RATING);
            assertThat(service.getSortType()).isEqualTo(getRatingReviews.getSortType());
        }
    }
}