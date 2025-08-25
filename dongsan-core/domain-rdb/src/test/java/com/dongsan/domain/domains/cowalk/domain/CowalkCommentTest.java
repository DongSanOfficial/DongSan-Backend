package com.dongsan.domain.domains.cowalk.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CowalkCommentTest {
    @Test
    @DisplayName("산책로 댓글을 생성한다")
    void shouldCreateCowalkComment() {
        Long cowalkPostId = 1L;
        Long memberId = 10L;
        String content = "댓글 내용";

        CowalkComment comment = new CowalkComment(cowalkPostId, memberId, content);

        assertThat(comment.getMemberId()).isEqualTo(memberId);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getId()).isNull();
    }
}
