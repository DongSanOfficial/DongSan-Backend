package com.dongsan.api.domains.walkway;

import bookmark.BookmarkFixture;
import com.dongsan.api.domains.auth.AuthUserDto;
import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.auth.oauth2.CustomRequestEntityConverter;
import com.dongsan.api.domains.walkway.dto.WalkwayCoordinate;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
import com.dongsan.api.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.api.domains.walkway.dto.request.UpdateWalkwayRequest;
import com.dongsan.api.support.error.ApiErrorCode;
import com.dongsan.api.support.response.discord.DiscordNotifier;
import com.dongsan.core.domains.bookmark.BookmarkService;
import com.dongsan.core.domains.bookmark.BookmarkWithMarkedStatus;
import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.image.ImageService;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.walkway.*;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;
import com.dongsan.file.service.S3FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import walkway.WalkwayFixture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static image.ImageFixture.createImage;
import static member.MemberFixture.createMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static walkway.WalkwayFixture.createWalkwayWithId;

@WebMvcTest(controllers = WalkwayController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("WalkwayController Unit Test")
class WalkwayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomRequestEntityConverter customRequestEntityConverter;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    WalkwayService walkwayService;

    @MockBean
    BookmarkService bookmarkService;

    @MockBean
    S3FileService s3FileService;

    @MockBean
    ImageService imageService;

    @MockBean
    DiscordNotifier discordNotifier;

    final Member member = createMember();
    final CustomAuthUser customOAuth2User = new CustomAuthUser(new AuthUserDto(member));

    @BeforeEach
    void setUp_Authentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        context.setAuthentication(authentication);
    }

    @Nested
    @DisplayName("createWalkway 메서드는")
    class Describe_createWalkwayEntity {
        @Test
        @DisplayName("request body를 전달 받으면 생성한 walkwayId를 반환한다.")
        void it_returns_walkwayId() throws Exception {
            // Given
            List<WalkwayCoordinate> course = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                course.add(new WalkwayCoordinate(0.0, 0.0));
            }
            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
                    1L,
                    "testName",
                    "testMemo",
                    10.0,
                    600,
                    List.of("하나", "둘"),
                    ExposeLevel.PUBLIC,
                    course
            );
            Image image = createImage();
            CreateWalkway createWalkway = createWalkwayRequest.toCreateWalkway(image, customOAuth2User.getMemberId());
            Long walkwayId = 1L;

            when(imageService.getImage(createWalkwayRequest.courseImageId())).thenReturn(image);
            when(walkwayService.createWalkway(createWalkway)).thenReturn(walkwayId);

            // When
            ResultActions response = mockMvc.perform(post("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createWalkwayRequest)));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.walkwayId").value(walkwayId));
        }

        @Test
        @DisplayName("request body의 name이나 course가 유효하지 않으면 INVALID_ARGUMENT_ERROR를 반환한다.")
        void it_returns_INVALID_ARGUMENT_ERROR() throws Exception {
            // Given
            List<WalkwayCoordinate> course = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                course.add(new WalkwayCoordinate(0.0, 0.0));
            }
            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
                    1L,
                    "",
                    "testMemo",
                    4.2,
                    20,
                    List.of("하나", "둘"),
                    ExposeLevel.PUBLIC,
                    course
            );

            // When
            ResultActions response = mockMvc.perform(post("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createWalkwayRequest)));

            // Then
            response.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ApiErrorCode.INVALID_ARGUMENT_ERROR.getCode()));
        }
    }

    @Nested
    @DisplayName("createWalkwayCourseImage 메서드는")
    class Describe_createWalkwayCourseImageEntityEntity {
        @Test
        @DisplayName("산책로에 코스 이미지를 등록하고 등록한 산책로의 walkwayId를 반환한다.")
        void it_returns_walkwayId() throws Exception {
            // Given
            String imageUrl = "https://test.com/";
            Long imageId = 1L;
            MockMultipartFile file = new MockMultipartFile(
                    "courseImage",
                    "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    "image-content".getBytes()
            );

            when(s3FileService.saveFile(file)).thenReturn(imageUrl);
            when(imageService.createImage(imageUrl)).thenReturn(imageId);

            // When
            ResultActions response = mockMvc.perform(multipart("/walkways/image")
                    .file(file)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .accept(MediaType.APPLICATION_JSON));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.courseImageId").exists());
        }
    }

    @Nested
    @DisplayName("getWalkway 메서드는")
    class Describe_getWalkwayEntity {

        @Test
        @DisplayName("walkwayId를 전달 받으면 DTO 반환한다.")
        void it_returns_DTO() throws Exception {
            // Given
            Long walkwayId = 1L;
            Walkway walkway = createWalkwayWithId(1L);
            boolean isLike = true;
            boolean isMarked = true;

            when(walkwayService.getWalkway(customOAuth2User.getMemberId(), walkwayId)).thenReturn(walkway);
            when(walkwayService.existsLikedWalkway(customOAuth2User.getMemberId(), walkwayId)).thenReturn(isLike);
            when(bookmarkService.existsMarkedWalkway(customOAuth2User.getMemberId(), walkwayId)).thenReturn(isMarked);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/" + walkwayId)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(walkway.name()));
        }
    }

    @Nested
    @DisplayName("getBookmarksWithMarkedWalkway 메서드는")
    class Describe_getBookmarksWithMarkedWalkwayEntity {
        @Test
        @DisplayName("marked 여부를 포함한 bookmark 리스트를 반환한다.")
        void it_returns_bookmarks() throws Exception {
            // Given
            Long walkwayId = 1L;
            Integer size = 5;
            List<BookmarkWithMarkedStatus> bookmarks = new ArrayList<>();

            for (long i = 0; i < 5; i++) {
                bookmarks.add(BookmarkFixture.createBookmarkWithMarkedStatus());
            }
            PagingResponse<BookmarkWithMarkedStatus> cursorPagingResponse = PagingResponse.from(bookmarks, size);

            when(bookmarkService.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId,
                    new CursorRequest(null, size)))
                    .thenReturn(cursorPagingResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/" + walkwayId + "/bookmarks")
                    .param("size", size.toString())
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data.size()").value(5));
        }
    }

    @Nested
    @DisplayName("updateWalkway 메서드는")
    class Describe_updateWalkwayEntity {
        @Test
        @DisplayName("204를 반환한다.")
        void it_returns_204() throws Exception {
            // Given
            Long walkwayId = 1L;
            UpdateWalkwayRequest updateWalkwayRequest = new UpdateWalkwayRequest(
                    "test name",
                    "test memo",
                    List.of(),
                    ExposeLevel.PRIVATE);

            // When
            ResultActions response = mockMvc.perform(put("/walkways/" + walkwayId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateWalkwayRequest)));

            // Then
            response.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("getWalkwaysSearch 메서드는")
    class Describe_getWalkwaysSearch {
        List<Walkway> walkways = new ArrayList<>();
        Map<Long, Boolean> isLiked = new HashMap<>();

        @BeforeEach
        void setUp() {
            for (long i = 1; i <= 10; i++) {
                walkways.add(createWalkwayWithId(i));
                isLiked.put(i, true);
            }
        }

        @Test
        @DisplayName("type이 liked이면 좋아요 순으로 DTO를 반환한다.")
        void it_returns_DTO_liked() throws Exception {
            // given
            String sort = "liked";
            Double latitude = 1.0;
            Double longitude = 1.0;
            Double distance = 1.3;
            Integer size = 10;
            PagingResponse<Walkway> cursorPagingResponse = PagingResponse.from(walkways, size);

            when(walkwayService.searchWalkway(any(), any())).thenReturn(cursorPagingResponse);
            when(walkwayService.existsLikedWalkways(any(), any())).thenReturn(isLiked);

            // When
            ResultActions response = mockMvc.perform(get("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("sort", sort)
                    .param("latitude", latitude.toString())
                    .param("longitude", longitude.toString())
                    .param("distance", distance.toString())
                    .param("size", size.toString()));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.size()").value(size));
        }

        @Test
        @DisplayName("type이 rating이면 좋아요 순으로 DTO를 반환한다.")
        void it_returns_DTO_rating() throws Exception {
            // given
            String sort = "rating";
            Double latitude = 1.0;
            Double longitude = 1.0;
            Double distance = 1.3;
            Integer size = 10;

            PagingResponse<Walkway> cursorPagingResponse = PagingResponse.from(walkways, size);

            when(walkwayService.searchWalkway(any(), any())).thenReturn(cursorPagingResponse);
            when(walkwayService.existsLikedWalkways(any(), any())).thenReturn(isLiked);

            // When
            ResultActions response = mockMvc.perform(get("/walkways")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("sort", sort)
                    .param("latitude", latitude.toString())
                    .param("longitude", longitude.toString())
                    .param("distance", distance.toString())
                    .param("size", size.toString()));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.size()").value(size));
        }
    }

    @Nested
    @DisplayName("createHistory 메서드는")
    class Describe_createHistory {

        @Test
        @DisplayName("walkwayId와 request body를 받아 생성한 walkwayHistoryId를 반환한다.")
        void it_returns_walkwayHistoryId() throws Exception {
            // Given
            Long walkwayId = 1L;
            Long walkwayHistoryId = 100L;

            CreateWalkwayHistoryRequest createWalkwayHistoryRequest
                    = new CreateWalkwayHistoryRequest(600, 10.0);

            when(walkwayService.createWalkwayHistory(any()))
                    .thenReturn(walkwayHistoryId);

            // When
            ResultActions response = mockMvc.perform(post("/walkways/{walkwayId}/history", walkwayId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createWalkwayHistoryRequest)));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.walkwayHistoryId").value(walkwayHistoryId));
        }
    }

    @Nested
    @DisplayName("getHistories 메서드는")
    class Describe_getHistories {
        @Test
        @DisplayName("리뷰 가능한 산책로 이용 기록을 반환한다.")
        void it_returns_walkway_histories() throws Exception {
            // Given
            Long walkwayId = 1L;
            Long memberId = member.id();
            Integer size = 1;
            Long lastHistoryId = 1L;

            List<WalkwayHistory> histories = List.of(WalkwayFixture.createWalkwayHistory());
            PagingResponse<WalkwayHistory> walkwayHistoryPagingResponse = PagingResponse.from(histories, size);
            when(walkwayService.getCanReviewWalkwayHistory(memberId, walkwayId, size, lastHistoryId)).thenReturn(
                    walkwayHistoryPagingResponse);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/{walkwayId}/history", walkwayId)
                    .param("size", size.toString())
                    .param("lastId", lastHistoryId.toString())
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.size()").value(histories.size()));
        }
    }

    @Nested
    @DisplayName("deleteWalkway 메서드는")
    class DeleteWalkway {
        @Test
        @DisplayName("산책로를 삭제한다.")
        void it_returns_void() throws Exception {
            // given
            Long walkwayId = 1L;

            // when
            ResultActions response = mockMvc.perform(delete("/walkways/{walkwayId}", walkwayId)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            response.andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("getWalkwaysLatest 메서드는")
    class Describe_getWalkwaysLatest {
        List<Walkway> walkways = new ArrayList<>();
        Map<Long, Boolean> isLiked = new HashMap<>();

        @BeforeEach
        void setUp() {
            for (long i = 1; i <= 10; i++) {
                walkways.add(createWalkwayWithId(i));
                isLiked.put(i, true);
            }
        }

        @Test
        @DisplayName("산책로 목록을 반환한다.")
        void it_returns_walkways() throws Exception {
            // given
            Integer size = 10;
            PagingResponse<Walkway> cursorPagingResponse = PagingResponse.from(walkways, size);

            when(walkwayService.getWalkways(any(), any(), any(), any())).thenReturn(cursorPagingResponse);
            when(walkwayService.existsLikedWalkways(any(), any())).thenReturn(isLiked);

            // When
            ResultActions response = mockMvc.perform(get("/walkways/all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("size", size.toString()));

            // Then
            response.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andExpect(jsonPath("$.data.size()").value(size));
        }
    }
}
