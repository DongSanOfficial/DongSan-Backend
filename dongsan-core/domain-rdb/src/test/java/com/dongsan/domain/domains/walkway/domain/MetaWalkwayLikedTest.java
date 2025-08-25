package com.dongsan.domain.domains.walkway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MetaWalkwayLikedTest {
    @Test
    @DisplayName("walkway의 초기 좋아요 갯수는 0개 이다")
    void shouldCreateNewMetaWalkwayLikedWithZeroCount() {
        Long walkwayId = 1L;

        MetaWalkwayLiked metaWalkwayLiked = new MetaWalkwayLiked(walkwayId);

        assertThat(metaWalkwayLiked.getLikeCount()).isEqualTo(0);
        assertThat(metaWalkwayLiked.getId()).isNull();
    }

    @Test
    @DisplayName("좋아요 수를 1 증가시킬 수 있다")
    void shouldIncreaseLikeCount() {
        MetaWalkwayLiked metaWalkwayLiked = new MetaWalkwayLiked(1L);
        Integer initialCount = metaWalkwayLiked.getLikeCount();

        metaWalkwayLiked.increaseLikeCount();

        assertThat(metaWalkwayLiked.getLikeCount()).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("좋아요 수를 1 감소시킬 수 있다")
    void shouldDecreaseLikeCount() {
        MetaWalkwayLiked metaWalkwayLiked = new MetaWalkwayLiked(1L);
        metaWalkwayLiked.increaseLikeCount();
        Integer initialCount = metaWalkwayLiked.getLikeCount();

        metaWalkwayLiked.decreaseLikeCount();

        assertThat(metaWalkwayLiked.getLikeCount()).isEqualTo(initialCount - 1);
    }

    @Test
    @DisplayName("좋아요 수가 0일 때 감소시키면 0을 유지한다")
    void shouldNotDecreaseBelowZero() {
        MetaWalkwayLiked metaWalkwayLiked = new MetaWalkwayLiked(1L);

        metaWalkwayLiked.decreaseLikeCount();

        assertThat(metaWalkwayLiked.getLikeCount()).isEqualTo(0);
    }

}
