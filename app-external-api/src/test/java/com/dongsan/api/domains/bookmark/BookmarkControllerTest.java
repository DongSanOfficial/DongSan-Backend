package com.dongsan.api.domains.bookmark;

import static bookmark.BookmarkFixture.createBookmark;
import static bookmark.MarkedWalkwayFixture.createMarkedWalkway;
import static member.MemberFixture.createMember;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.support.response.CursorResponse;
import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.domains.bookmark.BookmarkService;
import com.dongsan.core.domains.bookmark.MarkedWalkway;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookmarkController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("BookmarkController Unit Test")
class BookmarkControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookmarkService bookmarkService;

    Member member;
    CustomOAuth2User customOAuth2User;

    @BeforeEach
    void setUp(){
        member = createMember();
        customOAuth2User = new CustomOAuth2User(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Nested
    @DisplayName("createBookmark 메서드는")
    class Describe_createBookmarkEntity {
        @Test
        void request_DTO에_name이_공백이면_400을_반환한다() throws Exception{
            // given
            BookmarkNameRequest request = new BookmarkNameRequest(" ");
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks")
                        .content(requestBody)
                        .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }


        @Test
        void request_DTO에_name이_null이면_400을_반환한다() throws Exception{
            // given
            BookmarkNameRequest request = new BookmarkNameRequest(null);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks")
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        void 생성한_북마크의_Id를_response_DTO로_반환한다() throws Exception{
            // given
            BookmarkNameRequest request = new BookmarkNameRequest("북마크1");
            Long bookmarkId = 1L;
            BookmarkIdResponse response = new BookmarkIdResponse(bookmarkId);
            when(bookmarkService.createBookmark(customOAuth2User.getMemberId(), request.name())).thenReturn(bookmarkId);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks")
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.bookmarkId").value(response.bookmarkId()))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("renameBookmark 메서드는")
    class Describe_renameBookmarkEntity {
        @Test
        void request_DTO에_name이_공백이면_400을_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;
            BookmarkNameRequest request = new BookmarkNameRequest(" ");
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        void request_DTO에_name이_null이면_400을_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;
            BookmarkNameRequest request = new BookmarkNameRequest(null);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        void 북마크_이름_변경을_완료하면_200을_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;
            BookmarkNameRequest request = new BookmarkNameRequest("북마크1");
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(put("/bookmarks/{bookmarkId}", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("addWalkway 메서드는")
    class Describe_addWalkwayEntity {
        @Test
        void request_DTO에_walkwayId가_null이면_400을_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;
            WalkwayIdRequest request = new WalkwayIdRequest(null);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks/{bookmarkId}/walkways", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }

        @Test
        void 북마크에_산책로를_추가하면_200을_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;
            WalkwayIdRequest request = new WalkwayIdRequest(2L);
            String requestBody = objectMapper.writeValueAsString(request);

            // when & then
            mockMvc.perform(post("/bookmarks/{bookmarkId}/walkways", bookmarkId)
                            .content(requestBody)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("deleteWalkway 메서드는")
    class Describe_deleteWalkwayEntity {
        @Test
        void 북마크를_산책로에서_제거하면_200을_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;
            Long walkwayId = 2L;

            // when & then
            mockMvc.perform(delete("/bookmarks/{bookmarkId}/walkways/{walkwayId}", bookmarkId, walkwayId)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("deleteBookmark 메서드는")
    class Describe_deleteBookmarkEntity {
        @Test
        void 북마크를_삭제하면_200_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;

            // when & then
            mockMvc.perform(delete("/bookmarks/{bookmarkId}", bookmarkId)
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("getBookmarkDetail 메서드는")
    class Describe_getBookmarkDetailEntity {
        @Test
        void 북마크에_추가된_산책로들의_정보를_response_DTO로_반환한다() throws Exception{
            // given
            Long bookmarkId = 1L;
            Integer size = 10;
            Long lastId = 3L;
            List<MarkedWalkway> markedWalkways = new ArrayList<>(List.of(createMarkedWalkway(1L, 3L), createMarkedWalkway(2L, 3L)));
            PagingResponse<MarkedWalkway> response = new PagingResponse<>(markedWalkways, false);
            when(bookmarkService.getBookmarkWalkways(customOAuth2User.getMemberId(), bookmarkId, new CursorRequest(lastId, size))).thenReturn(response);
            CursorResponse<BookmarkWalkwaysResponse> result = new CursorResponse<>(BookmarkWalkwaysResponse.from(response.data()), response.hasNext());

            // when & then
            mockMvc.perform(get("/bookmarks/{bookmarkId}/walkways", bookmarkId)
                            .param("size", size.toString())
                            .param("lastId", lastId.toString())
                            .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.size()", CoreMatchers.is(result.data().size())))
                    .andExpect(jsonPath("$.data[0].name").value(result.data().get(0).name()))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("getUserBookmarks 메서드는")
    class Describe_getUserBookmarks {
        @Test
        void 북마크_이름을_response_DTO로_반환한다() throws Exception {
            // given
            Integer size = 10;
            Long lastId = 3L;
            List<Bookmark> bookmarks = new ArrayList<>(List.of(
                    createBookmark(1L, "북마크1"),
                    createBookmark(2L, "북마크2")
                    ));
            PagingResponse<Bookmark> response = new PagingResponse<>(bookmarks, false);
            when(bookmarkService.getUserBookmarksName(customOAuth2User.getMemberId(), new CursorRequest(lastId, size))).thenReturn(response);
            CursorResponse<BookmarksNameResponse> result = new CursorResponse<>(BookmarksNameResponse.from(response.data()), response.hasNext());

            // when & then
            mockMvc.perform(get("/users/bookmarks/title")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("lastId", lastId.toString())
                            .param("size", size.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.size()", CoreMatchers.is(response.data().size())))
                    .andExpect(jsonPath("$.data[0].title").value(response.data().get(0).title()))
                    .andExpect(jsonPath("$.data[1].title").value(response.data().get(1).title()));
        }
    }

}
