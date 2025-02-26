package com.dongsan.core.domains.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import image.ImageFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageService Unit Test")
public class ImageServiceTest {
    @Mock
    ImageWriter imageWriter;
    @Mock
    ImageReader imageReader;
    @Mock
    ImageValidator imageValidator;
    @InjectMocks
    ImageService imageService;

    @Nested
    @DisplayName("createImage 메서드는")
    class Describe_createImage {
        @Test
        @DisplayName("이미지 url을 입력 받으면 Image를 생성한다.")
        void it_returns_entity() {
            // Given
            Long id = 1L;
            String url = "test.com";

            when(imageWriter.createImage(url)).thenReturn(id);

            // When
            Long result = imageService.createImage(url);

            // Then
            assertThat(result).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("getImage 메서드는")
    class Describe_getImage {
        @Test
        @DisplayName("이미지를 반환 한다.")
        void it_returns_image() {
            // given
            Long imageId = 1L;
            Image image = ImageFixture.createImage();

            when(imageReader.getImage(imageId)).thenReturn(image);

            // when
            Image result = imageService.getImage(imageId);

            // then
            assertThat(result).isNotNull();
        }
    }
}
