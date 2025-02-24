//package com.dongsan.api.domains.walkway;
//
//import static fixture.MemberFixture.createMemberWithId;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
//import com.dongsan.core.domains.bookmark.BookmarkService;
//import com.dongsan.core.domains.image.ImageService;
//import com.dongsan.core.domains.walkway.WalkwayService;
//import com.dongsan.core.domains.walkway.service.WalkwayReader;
//import com.dongsan.core.domains.walkway.usecase.HashtagService;
//import com.dongsan.core.domains.walkway.usecase.LikedWalkwayService;
//import com.dongsan.domains.bookmark.controller.BookmarksWithMarkedWalkwayResponse;
//import com.dongsan.domains.bookmark.dto.BookmarksWithMarkedWalkwayDTO;
//import com.dongsan.domains.image.entity.Image;
//import com.dongsan.domains.image.usecase.S3UseCase;
//import com.dongsan.domains.member.entity.Member;
//import com.dongsan.domains.walkway.dto.WalkwayCoordinate;
//import com.dongsan.domains.walkway.dto.request.CreateWalkwayHistoryRequest;
//import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
//import com.dongsan.domains.walkway.dto.request.UpdateWalkwayRequest;
//import com.dongsan.domains.walkway.dto.response.CreateWalkwayHistoryResponse;
//import com.dongsan.domains.walkway.dto.response.GetWalkwayWithLikedResponse;
//import com.dongsan.domains.walkway.dto.response.SearchWalkwayResponse;
//import com.dongsan.domains.walkway.dto.response.SearchWalkwayResult;
//import com.dongsan.domains.walkway.entity.Walkway;
//import com.dongsan.domains.walkway.entity.WalkwayHistory;
//import com.dongsan.domains.walkway.enums.ExposeLevel;
//import com.dongsan.domains.walkway.usecase.WalkwayHistoryUseCase;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import fixture.ImageFixture;
//import fixture.WalkwayFixture;
//import fixture.WalkwayHistoryFixture;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//@WebMvcTest(controllers = WalkwayController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(MockitoExtension.class)
//@DisplayName("WalkwayController Unit Test")
//class WalkwayControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockBean
//    WalkwayService walkwayUseCase;
//
//    @MockBean
//    BookmarkService bookmarkService;
//
//    @MockBean
//    WalkwayReader walkwayQueryService;
//
//    @MockBean
//    LikedWalkwayService likedWalkwayUseCase;
//
//    @MockBean
//    HashtagService hashtagUseCase;
//
//    @MockBean
//    S3UseCase s3UseCase;
//
//    @MockBean
//    ImageService imageService;
//
//    @MockBean
//    WalkwayHistoryUseCase walkwayHistoryUseCase;
//
//    final Member member = createMemberWithId(1L);
//    final CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);
//
//    @BeforeEach
//    void setUp_Authentication(){
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
//        context.setAuthentication(authentication);
//    }
//
//    @Nested
//    @DisplayName("createWalkway 메서드는")
//    class Describe_createWalkwayEntity {
//
//        @Test
//        @DisplayName("request body를 전달 받으면 생성한 walkwayId를 반환한다.")
//        void it_returns_walkwayId() throws Exception {
//            // Given
//            List<WalkwayCoordinate> course = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                course.add(new WalkwayCoordinate(0.0, 0.0));
//            }
//            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
//                    1L,
//                    "testName",
//                    "testMemo",
//                    10.0,
//                    600,
//                    List.of("하나", "둘"),
//                    ExposeLevel.PUBLIC,
//                    course
//            );
//
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L , null);
//
//            when(walkwayUseCase.createWalkway(createWalkwayRequest, customOAuth2User.getMemberId())).thenReturn(walkway);
//
//            // When
//            ResultActions response = mockMvc.perform(post("/walkways")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(createWalkwayRequest)));
//
//            // Then
//            response.andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.data.walkwayId").value(1L));
//        }
//
//        @Test
//        @DisplayName("request body의 name이나 course가 유효하지 않으면 INVALID_ARGUMENT_ERROR를 반환한다.")
//        void it_returns_INVALID_ARGUMENT_ERROR() throws Exception {
//            // Given
//            List<WalkwayCoordinate> course = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                course.add(new WalkwayCoordinate(0.0, 0.0));
//            }
//            CreateWalkwayRequest createWalkwayRequest = new CreateWalkwayRequest(
//                    1L,
//                    "",
//                    "testMemo",
//                    4.2,
//                    20,
//                    List.of("하나", "둘"),
//                    ExposeLevel.PUBLIC,
//                    course
//            );
//
//            // When
//            ResultActions response = mockMvc.perform(post("/walkways")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(createWalkwayRequest)));
//
//            // Then
//            response.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.code").value(SystemErrorCode.INVALID_ARGUMENT_ERROR.getCode()));
//        }
//    }
//
//
//
//    @Nested
//    @DisplayName("createWalkwayCourseImage 메서드는")
//    class Describe_createWalkwayCourseImageEntityEntity {
//        @Test
//        @DisplayName("산책로에 코스 이미지를 등록하고 등록한 산책로의 walkwayId를 반환한다.")
//        void it_returns_walkwayId() throws Exception {
//            // Given
//            String imageUrl = "https://test.com/";
//            Long imageId = 1L;
//            MockMultipartFile file = new MockMultipartFile(
//                    "courseImage",
//                    "test.jpg",
//                    MediaType.IMAGE_JPEG_VALUE,
//                    "image-content".getBytes()
//            );
//            Image image = ImageFixture.createImageWithId(imageId, imageUrl);
//
//            when(s3UseCase.uploadCourseImage(any())).thenReturn(imageUrl);
//            when(imageService.createImage(imageUrl))
//                    .thenReturn(image);
//
//            // When
//            ResultActions response = mockMvc.perform(multipart("/walkways/image")
//                    .file(file)
//                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                    .accept(MediaType.APPLICATION_JSON));
//
//            // Then
//            response.andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.data").exists());
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkway 메서드는")
//    class Describe_getWalkwayEntity {
//
//        @Test
//        @DisplayName("walkwayId를 전달 받으면 DTO 반환한다.")
//        void it_returns_DTO() throws Exception {
//            // Given
//            Long walkwayId = 1L;
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, null);
//            GetWalkwayWithLikedResponse getWalkwayWithLikedResponse = new GetWalkwayWithLikedResponse(walkway, true);
//
//            when(walkwayUseCase.getWalkwayWithLiked(walkwayId, customOAuth2User.getMemberId()))
//                    .thenReturn(walkway);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/walkways/" + walkwayId)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(getWalkwayWithLikedResponse)));
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.name").value(walkway.getName()));
//        }
//    }
//
//    @Nested
//    @DisplayName("getBookmarksWithMarkedWalkway 메서드는")
//    class Describe_getBookmarksWithMarkedWalkwayEntity {
//        @Test
//        @DisplayName("marked 여부를 포함한 bookmark 리스트를 반환한다.")
//        void it_returns_bookmarks() throws Exception {
//            // Given
//            Long walkwayId = 1L;
//            Integer size = 5;
//            List<BookmarksWithMarkedWalkwayDTO> bookmarks = new ArrayList<>();
//
//            for(int i = 0; i < 5; i++) {
//                BookmarksWithMarkedWalkwayDTO bookmark
//                        = new BookmarksWithMarkedWalkwayDTO(1L, 1L, "test", 1L);
//                bookmarks.add(bookmark);
//            }
//
//            BookmarksWithMarkedWalkwayResponse bookmarksWithMarkedWalkwayResponse =
//                    BookmarksWithMarkedWalkwayMapper.toBookmarksWithMarkedWalkwayResponse(bookmarks, size);
//
//            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
//            when(bookmarkService.getBookmarksWithMarkedWalkway(customOAuth2User.getMemberId(), walkwayId, null, size))
//                    .thenReturn(bookmarksWithMarkedWalkwayResponse);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/walkways/" + walkwayId + "/bookmarks")
//                    .param("size", size.toString())
//                    .contentType(MediaType.APPLICATION_JSON));
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.bookmarks").isArray())
//                    .andExpect(jsonPath("$.data.bookmarks.size()").value(5));
//        }
//    }
//
//    @Nested
//    @DisplayName("updateWalkway 메서드는")
//    class Describe_updateWalkwayEntity {
//        @Test
//        @DisplayName("204를 반환한다.")
//        void it_returns_204() throws Exception {
//            // Given
//            Long walkwayId = 1L;
//            UpdateWalkwayRequest updateWalkwayRequest = new UpdateWalkwayRequest(
//                    "test name",
//                    "test memo",
//                    List.of(),
//                    ExposeLevel.PRIVATE);
//
//            when(walkwayQueryService.existsByWalkwayId(walkwayId)).thenReturn(true);
//
//            // When
//            ResultActions response = mockMvc.perform(put("/walkways/" + walkwayId)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(updateWalkwayRequest)));
//
//            // Then
//            response.andExpect(status().isNoContent());
//        }
//    }
//
//    @Nested
//    @DisplayName("getWalkwaysSearch 메서드는")
//    class Describe_getWalkwaysSearch {
//        List<SearchWalkwayResult> searchWalkwayResults = new ArrayList<>();
//
//        @BeforeEach
//        void setUp() {
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(null, null);
//            for (long i = 1; i <= 10; i++) {
//                SearchWalkwayResult searchWalkwayResult
//                        = new SearchWalkwayResult(i, null, null, List.of(), null, null, null, null, null, walkway.getStartLocation(), walkway.getCreatedAt());
//                searchWalkwayResults.add(searchWalkwayResult);
//            }
//        }
//
//        @Test
//        @DisplayName("type이 liked이면 좋아요 순으로 DTO를 반환한다.")
//        void it_returns_DTO_liked() throws Exception {
//            // given
//            String sort = "liked";
//            Double latitude = 1.0;
//            Double longitude = 1.0;
//            Double distance = 1.3;
//            Long lastId = null;
//            Integer size = 10;
//
//            when(walkwayUseCase.searchWalkway(customOAuth2User.getMemberId(), sort, latitude, longitude, distance, lastId, size))
//                    .thenReturn(searchWalkwayResults);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/walkways")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .param("sort", sort)
//                    .param("latitude", latitude.toString())
//                    .param("longitude", longitude.toString())
//                    .param("distance", distance.toString())
//                    .param("size", size.toString())
//                    .content(objectMapper.writeValueAsString(new SearchWalkwayResponse(searchWalkwayResults, size))));
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.walkways").isArray())
//                    .andExpect(jsonPath("$.data.walkways").isNotEmpty())
//                    .andExpect(jsonPath("$.data.walkways.size()").value(size));
//        }
//
//        @Test
//        @DisplayName("type이 rating이면 좋아요 순으로 DTO를 반환한다.")
//        void it_returns_DTO_rating() throws Exception {
//            // given
//            String sort = "rating";
//            Double latitude = 1.0;
//            Double longitude = 1.0;
//            Double distance = 1.3;
//            Long lastId = null;
//            Integer size = 10;
//
//            when(walkwayUseCase.searchWalkway(customOAuth2User.getMemberId(), sort, latitude, longitude, distance, lastId, size))
//                    .thenReturn(searchWalkwayResults);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/walkways")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .param("sort", sort)
//                    .param("latitude", latitude.toString())
//                    .param("longitude", longitude.toString())
//                    .param("distance", distance.toString())
//                    .param("size", size.toString())
//                    .content(objectMapper.writeValueAsString(new SearchWalkwayResponse(searchWalkwayResults, size))));
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.walkways").isArray())
//                    .andExpect(jsonPath("$.data.walkways").isNotEmpty())
//                    .andExpect(jsonPath("$.data.walkways.size()").value(size));
//        }
//    }
//
//    @Nested
//    @DisplayName("createHistory 메서드는")
//    class Describe_createHistory {
//
//        @Test
//        @DisplayName("walkwayId와 request body를 받아 생성한 walkwayHistoryId를 반환한다.")
//        void it_returns_walkwayHistoryId() throws Exception {
//            // Given
//            Long walkwayId = 1L;
//            Long walkwayHistoryId = 100L;
//
//            CreateWalkwayHistoryRequest createWalkwayHistoryRequest
//                    = new CreateWalkwayHistoryRequest(600, 10.0);
//            CreateWalkwayHistoryResponse createWalkwayHistoryResponse
//                    = new CreateWalkwayHistoryResponse(walkwayHistoryId, true);
//
//            when(walkwayHistoryUseCase.createWalkwayHistory(customOAuth2User.getMemberId(), walkwayId, createWalkwayHistoryRequest))
//                    .thenReturn(createWalkwayHistoryResponse);
//
//            // When
//            ResultActions response = mockMvc.perform(post("/walkways/{walkwayId}/history", walkwayId)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(createWalkwayHistoryRequest)));
//
//            // Then
//            response.andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.data.walkwayHistoryId").value(walkwayHistoryId));
//        }
//    }
//
//    @Nested
//    @DisplayName("getHistories 메서드는")
//    class Describe_getHistories {
//
//        @Test
//        @DisplayName("리뷰 가능한 산책로 이용 기록을 반환한다.")
//        void it_returns_walkway_histories() throws Exception {
//            // Given
//            Long walkwayId = 1L;
//            Long memberId = member.getId();
//            Walkway walkway = WalkwayFixture.createWalkwayWithId(1L, null);
//
//            List<WalkwayHistory> histories = List.of(
//                    WalkwayHistoryFixture.createWalkwayHistoryWithId(1L, null, walkway),
//                    WalkwayHistoryFixture.createWalkwayHistoryWithId(2L, null, walkway)
//            );
//
//            when(walkwayHistoryUseCase.getWalkwayHistories(memberId, walkwayId)).thenReturn(histories);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/walkways/{walkwayId}/history", walkwayId)
//                    .contentType(MediaType.APPLICATION_JSON));
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.walkwayHistories.size()").value(2));
//        }
//    }
//}