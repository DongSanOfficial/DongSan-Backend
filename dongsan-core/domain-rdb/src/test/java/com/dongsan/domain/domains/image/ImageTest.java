package com.dongsan.domain.domains.image;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTest {
    @Test
    @DisplayName("새로운 이미지 생성 시 URL을 설정한다")
    void shouldCreateNewImage() {
        // given
        String imageUrl = "http://example.com/image.jpg";

        // when
        Image image = new Image(imageUrl);

        // then
        assertThat(image.getUrl()).isEqualTo(imageUrl);
        assertThat(image.getId()).isNull();
    }

}
