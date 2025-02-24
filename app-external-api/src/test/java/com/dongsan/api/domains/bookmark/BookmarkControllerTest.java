package com.dongsan.api.domains.bookmark;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(BookmarkController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("BookmarkController Unit Test")
class BookmarkControllerTest {
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    ObjectMapper objectMapper;
//    @MockBean
//    BookmarkService bookmarkService;
//    @MockBean
//    BookmarkReader bookmarkReader;
//    @MockBean
//    WalkwayReader walkwayQueryService;
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
//    @DisplayName("createBookmark 메서드는")
//    class Describe_createBookmarkEntity {
//        @Test
//        @DisplayName("DTO에 name이 공백이면 예외를 반환한다.")
//        void it_returns_400_when_name_is_blank() throws Exception{
//            // given
//            BookmarkNameRequest request = BookmarkNameRequest.builder()
//                    .name(" ")
//                    .build();
//            String requestBody = objectMapper.writeValueAsString(request);
//
//            // when & then
//            mockMvc.perform(post("/bookmarks")
//                        .content(requestBody)
//                        .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("DTO에 name이 null이면 예외를 반환한다.")
//        void it_returns_400_when_name_is_null() throws Exception{
//            // given
//            BookmarkNameRequest request = BookmarkNameRequest.builder()
//                    .name(null)
//                    .build();
//            String requestBody = objectMapper.writeValueAsString(request);
//
//            // when & then
//            mockMvc.perform(post("/bookmarks")
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("생성한 북마크의 Id를 response DTO로 반환한다.")
//        void it_returns_responseDTO() throws Exception{
//            // given
//            BookmarkNameRequest request = BookmarkNameRequest.builder()
//                    .name("북마크1")
//                    .build();
//            BookmarkIdResponse response = BookmarkIdResponse.builder()
//                    .bookmarkId(1L)
//                    .build();
//            when(bookmarkService.createBookmark(customOAuth2User.getMemberId(), request)).thenReturn(response);
//            String requestBody = objectMapper.writeValueAsString(request);
//
//            // when & then
//            mockMvc.perform(post("/bookmarks")
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.data.bookmarkId").value(response.bookmarkId()))
//                    .andReturn();
//        }
//    }
//
//    @Nested
//    @DisplayName("renameBookmark 메서드는")
//    class Describe_renameBookmarkEntity {
//        @Test
//        @DisplayName("DTO에 name이 공백이면 예외를 반환한다.")
//        void it_returns_400_when_name_is_blank() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            BookmarkNameRequest request = BookmarkNameRequest.builder()
//                    .name(" ")
//                    .build();
//            String requestBody = objectMapper.writeValueAsString(request);
//
//            // when & then
//            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("bookmarkId가 존재하지 않으면 예외를 반환한다.")
//        void it_returns_400_when_bookmark_not_exist() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            BookmarkNameRequest request = BookmarkNameRequest.builder()
//                    .name("북마크1")
//                    .build();
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(false);
//            String requestBody = objectMapper.writeValueAsString(request);
//
//            // when & then
//            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("북마크 이름 변경을 완료하면 201을 반환한다.")
//        void it_returns_201() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            BookmarkNameRequest request = BookmarkNameRequest.builder()
//                    .name("북마크1")
//                    .build();
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(true);
//            String requestBody = objectMapper.writeValueAsString(request);
//
//            // when & then
//            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isNoContent())
//                    .andReturn();
//        }
//    }
//
//    @Nested
//    @DisplayName("addWalkway 메서드는")
//    class Describe_addWalkwayEntity {
//        @Test
//        @DisplayName("DTO에 id가 null이면 예외를 반환한다.")
//        void it_throws_400_when_walkwayId_is_null() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            WalkwayIdRequest request = WalkwayIdRequest.builder()
//                    .walkwayId(null)
//                    .build();
//            String requestBody = objectMapper.writeValueAsString(request);
//
//            // when & then
//            mockMvc.perform(post("/bookmarks/{bookmarkId}/walkways", bookmarkId)
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("bookmark가 존재하지 않으면 예외를 반환한다.")
//        void it_returns_400_when_bookmark_not_exist() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            WalkwayIdRequest request = WalkwayIdRequest.builder()
//                    .walkwayId(2L)
//                    .build();
//            String requestBody = objectMapper.writeValueAsString(request);
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(false);
//
//            // when & then
//            mockMvc.perform(post("/bookmarks/{bookmarkId}/walkways", bookmarkId)
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("북마크에 산책로를 추가하면 204를 반환한다.")
//        void it_returns_204() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            WalkwayIdRequest request = WalkwayIdRequest.builder()
//                    .walkwayId(2L)
//                    .build();
//            String requestBody = objectMapper.writeValueAsString(request);
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(true);
//
//            // when & then
//            mockMvc.perform(post("/bookmarks/{bookmarkId}/walkways", bookmarkId)
//                            .content(requestBody)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isNoContent())
//                    .andReturn();
//        }
//    }
//
//    @Nested
//    @DisplayName("deleteWalkway 메서드는")
//    class Describe_deleteWalkwayEntity {
//        @Test
//        @DisplayName("bookmark가 존재하지 않으면 예외를 반환한다.")
//        void it_returns_400_when_bookmark_not_exist() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            Long walkwayId = 2L;
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(false);
//            when(walkwayQueryService.existsWalkway(walkwayId)).thenReturn(true);
//
//            // when & then
//            mockMvc.perform(delete("/bookmarks/{bookmarkId}/walkways/{walkwayId}", bookmarkId, walkwayId)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("walkway가 존재하지 않으면 예외를 반환한다.")
//        void it_returns_400_when_walkway_not_exist() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            Long walkwayId = 2L;
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(true);
//            when(walkwayQueryService.existsWalkway(walkwayId)).thenReturn(false);
//
//            // when & then
//            mockMvc.perform(delete("/bookmarks/{bookmarkId}/walkways/{walkwayId}", bookmarkId, walkwayId)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("북마크를 산책로에서 제거하면 204를 반환한다.")
//        void it_returns_204() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            Long walkwayId = 2L;
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(true);
//            when(walkwayQueryService.existsWalkway(walkwayId)).thenReturn(true);
//
//            // when & then
//            mockMvc.perform(delete("/bookmarks/{bookmarkId}/walkways/{walkwayId}", bookmarkId, walkwayId)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isNoContent())
//                    .andReturn();
//        }
//    }
//
//    @Nested
//    @DisplayName("deleteBookmark 메서드는")
//    class Describe_deleteBookmarkEntity {
//        @Test
//        @DisplayName("bookmark가 존재하지 않으면 예외를 반환한다.")
//        void it_returns_400_when_bookmark_not_exist() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(false);
//
//            // when & then
//            mockMvc.perform(delete("/bookmarks/{bookmarkId}", bookmarkId)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("북마크를 삭제하면 204를 반환한다.")
//        void it_returns_204() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(true);
//
//            // when & then
//            mockMvc.perform(delete("/bookmarks/{bookmarkId}", bookmarkId)
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isNoContent())
//                    .andReturn();
//        }
//    }
//
//    @Nested
//    @DisplayName("getBookmarkDetail 메서드는")
//    class Describe_getBookmarkDetailEntity {
//        @Test
//        @DisplayName("bookmark가 존재하지 않으면 예외를 반환한다.")
//        void it_returns_400_when_bookmark_not_exist() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            Integer size = 10;
//            Long walkwayId = 3L;
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(false);
//
//            // when & then
//            mockMvc.perform(get("/bookmarks/{bookmarkId}/walkways", bookmarkId)
//                            .param("size", size.toString())
//                            .param("lastId", walkwayId.toString())
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isBadRequest())
//                    .andReturn();
//        }
//
//        @Test
//        @DisplayName("북마크 상세 조회 결과를 DTO로 반환한다.")
//        void it_returns_DTO() throws Exception{
//            // given
//            Long bookmarkId = 1L;
//            Integer size = 10;
//            Long walkwayId = 3L;
//            GetBookmarkDetailParam param = new GetBookmarkDetailParam(customOAuth2User.getMemberId(), bookmarkId, size, walkwayId);
//            GetBookmarkDetailResponse response = GetBookmarkDetailResponse.builder()
//                    .name("북마크 이름")
//                    .walkways(List.of(
//                            WalkwayInfo.builder()
//                                    .walkwayId(1L)
//                                    .name("산책로이름")
//                                    .date(LocalDateTime.now())
//                                    .distance(3.5)
//                                    .hashtags(List.of("a", "b", "c"))
//                                    .courseImageUrl("image.url")
//                                    .build()
//                    ))
//                    .build();
//            when(bookmarkReader.existsById(bookmarkId)).thenReturn(true);
//            when(bookmarkService.getBookmarkDetails(param)).thenReturn(response);
//
//            // when & then
//            mockMvc.perform(get("/bookmarks/{bookmarkId}/walkways", bookmarkId)
//                            .param("size", size.toString())
//                            .param("lastId", walkwayId.toString())
//                            .contentType("application/json;charset=UTF-8"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.name").value(response.name()))
//                    .andExpect(jsonPath("$.data.walkways.size()", CoreMatchers.is(response.walkways().size())))
//                    .andExpect(jsonPath("$.data.walkways[0].name").value(response.walkways().get(0).name()))
//                    .andReturn();
//        }
//    }

    //    @Nested
//    @DisplayName("getUserBookmarks 메서드는")
//    class Describe_getUserBookmarks {
//        @Test
//        @DisplayName("북마크가 존재하면 북마크를 아이디 기준 내림차순으로 반환한다.")
//        void getUserBookmarks() throws Exception {
//            // Given
//            List<BookmarkInfo> bookmarkInfoList = new ArrayList<>();
//
//            for(long id = 2L; id != 0L; id--) {
//                BookmarkInfo bookmarkInfo = BookmarkInfo.builder()
//                                .bookmarkId(id)
//                                .title("test" + id)
//                                .build();
//                bookmarkInfoList.add(bookmarkInfo);
//            }
//
//            GetBookmarksResponse getBookmarksResponse = new GetBookmarksResponse(bookmarkInfoList, true);
//
//            Integer limit = 2;
//            Long userId = member.getId();
//            Long bookmarkId = 3L;
//
//            when(userProfileUsecase.getUserBookmarks(userId, bookmarkId, limit)).thenReturn(getBookmarksResponse);
//
//            // When
//            ResultActions response = mockMvc.perform(get("/users/bookmarks/title")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .param("lastId", "3")
//                    .param("size", "2"));
//
//
//            // Then
//            response.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.data.bookmarks.size()", CoreMatchers.is(limit)))
//                    .andExpect(jsonPath("$.data.bookmarks[0].title").value(getBookmarksResponse.bookmarks().get(0).title()))
//                    .andExpect(jsonPath("$.data.bookmarks[1].title").value(getBookmarksResponse.bookmarks().get(1).title()));
//        }
//    }

}
