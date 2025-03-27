package com.dongsan.core.domains.image;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import image.ImageFixture;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageReader Unit Test")
public class ImageReaderTest {
	@Mock
	private ImageRepository imageRepository;
	@InjectMocks
	private ImageReader imageReader;

	@Nested
	@DisplayName("getImage 메서드는")
	class Describe_getImage {
		@Test
		@DisplayName("이미지를 반환 한다.")
		void it_returns_entity() {
			// Given
			Long id = 1L;
			Image image = ImageFixture.createImage();

			when(imageRepository.findById(id)).thenReturn(image);

			// When
			Image result = imageReader.getImage(id);

			// Then
			assertThat(result).isNotNull();
		}
	}
}
