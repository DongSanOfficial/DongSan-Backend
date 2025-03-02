package com.dongsan.core.domains.image;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.dongsan.core.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageValidator Unit Test")
class ImageValidatorTest {
    @InjectMocks
    ImageValidator imageValidator;
    @Mock
    ImageRepository imageRepository;

    @Nested
    @DisplayName("validateImageExists 메서드는")
    class Describe_validateImageExists {
        @Test
        @DisplayName("존재하지 않는 이미지면 예외를 발생시킨다.")
        void it_returns_exception() {
            // given
            Long imageId = 1L;
            boolean exists = false;

            when(imageRepository.existsById(imageId)).thenReturn(exists);

            // when & then
            assertThatThrownBy(() -> imageValidator.validateImageExists(imageId))
                    .isInstanceOf(CoreException.class);
        }
    }
}