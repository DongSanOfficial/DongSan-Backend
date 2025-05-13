//package com.dongsan.rdb.domains.image;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.dongsan.rds.domains.image.Image;
//import com.dongsan.core.support.error.CoreException;
//
//import fixture.ImageFixture;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ImageCoreRepository Unit Test")
//class ImageCoreRepositoryTest {
//	@InjectMocks
//	private ImageCoreRepository imageCoreRepository;
//	@Mock
//	private ImageJpaRepository imageJpaRepository;
//
//	@Nested
//	@DisplayName("save 메서드는")
//	class Describe_save {
//		@Test
//		@DisplayName("이미지를 저장하고 id를 반환한다.")
//		void it_returns_id() {
//			// given
//			String imageUrl = "test url";
//			ImageEntity imageEntity = ImageFixture.createImage();
//
//			when(imageJpaRepository.save(any(ImageEntity.class))).thenReturn(imageEntity);
//
//			// when
//			Long result = imageCoreRepository.save(imageUrl);
//
//			// then
//			assertThat(result).isEqualTo(imageEntity.getId());
//		}
//	}
//
//	@Nested
//	@DisplayName("findById 메서드는")
//	class Describe_findById {
//		@Test
//		@DisplayName("이미지를 반환한다.")
//		void it_returns_image() {
//			// given
//			Long imageId = 1L;
//			String imageUrl = "test url";
//			ImageEntity imageEntity = ImageFixture.createImage(imageUrl);
//
//			when(imageJpaRepository.findById(imageId)).thenReturn(Optional.of(imageEntity));
//
//			// when
//			Image result = imageCoreRepository.findById(imageId);
//
//			// then
//			assertThat(result.url()).isEqualTo(imageUrl);
//		}
//
//		@Test
//		@DisplayName("존재하지 않는 이미지 ID를 받으면 예외를 발생시킨다.")
//		void it_returns_exception() {
//			// given
//			Long imageId = 1L;
//			when(imageJpaRepository.findById(imageId)).thenReturn(Optional.empty());
//
//			// when & then
//			assertThatThrownBy(() -> imageCoreRepository.findById(imageId))
//				.isInstanceOf(CoreException.class);
//		}
//	}
//
//	@Nested
//	@DisplayName("existsById 메서드는")
//	class Describe_existsById {
//		@Test
//		@DisplayName("존재하는 이미지면 true를 반환한다.")
//		void it_returns_exists_true() {
//			// given
//			Long imageId = 1L;
//			when(imageJpaRepository.existsById(imageId)).thenReturn(true);
//
//			// when
//			boolean result = imageCoreRepository.existsById(imageId);
//
//			// then
//			assertThat(result).isTrue();
//		}
//
//		@Test
//		@DisplayName("존재하지 않는 이미지면 true를 반환한다.")
//		void it_returns_exists_false() {
//			// given
//			Long imageId = 1L;
//			when(imageJpaRepository.existsById(imageId)).thenReturn(false);
//
//			// when
//			boolean result = imageCoreRepository.existsById(imageId);
//
//			// then
//			assertThat(result).isFalse();
//		}
//	}
//}
