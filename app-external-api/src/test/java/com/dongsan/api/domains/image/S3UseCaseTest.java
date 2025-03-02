//package com.dongsan.domains.image.usecase;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.dongsan.file.service.S3FileService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("S3UseCase Unit Test")
//class S3UseCaseTest {
//    @Mock
//    private S3FileService s3FileService;
//    @InjectMocks
//    private S3UseCase s3UseCase;
//
//    @Nested
//    @DisplayName("uploadCourseImage")
//    class Describe_uploadCourseImageEntity {
//        @Test
//        @DisplayName("이미지 파일이 입력되면 이미지 URL을 반환한다.")
//        void it_returns_url() {
//            // Given
//            MockMultipartFile file = new MockMultipartFile(
//                    "image",
//                    "test.jpg",
//                    MediaType.IMAGE_JPEG_VALUE,
//                    "image-content".getBytes()
//            );
//            String url = "test.com";
//            when(s3FileService.saveFile(file)).thenReturn(url);
//
//            // When
//            String result = s3UseCase.uploadCourseImage(file);
//
//            // Then
//            assertThat(result).isEqualTo(url);
//        }
//    }
//
//}