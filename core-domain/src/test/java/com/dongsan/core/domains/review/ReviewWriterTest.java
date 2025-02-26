package com.dongsan.domains.walkway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.review.CreateReview;
import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.ReviewRepository;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.review.ReviewWriter;
import fixture.MemberFixture;
import fixture.ReviewFixture;
import fixture.WalkwayFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewCommandService Unit Test")
class ReviewWriterTest {

    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    ReviewWriter reviewWriter;

    @Nested
    @DisplayName("createReview 메서드는")
    class Describe_createReview {
        @Test
        @DisplayName("리뷰 생성에 성공하면 리뷰를 반환한다.")
        void it_returns_Review() {
            // Given
            Long reviewId = 1L;
            CreateReview createReview = new CreateReview(null, null, null, null, null);

            when(reviewRepository.save(createReview)).thenReturn(reviewId);

            // When
            Long result = reviewWriter.createReview(createReview);

            // Then
            assertThat(result).isEqualTo(reviewId);
        }
    }
}