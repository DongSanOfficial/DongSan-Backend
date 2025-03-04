package com.dongsan.api.domains.bookmark;

import com.dongsan.api.domains.auth.security.oauth2.CustomOAuth2User;
import com.dongsan.api.support.response.CursorResponse;
import com.dongsan.core.domains.bookmark.Bookmark;
import com.dongsan.core.domains.bookmark.BookmarkService;
import com.dongsan.core.domains.bookmark.MarkedWalkway;
import com.dongsan.core.support.util.CursorRequest;
import com.dongsan.core.support.util.PagingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "북마크")
@Validated
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/bookmarks")
    @Operation(summary = "북마크 생성")
    public ResponseEntity<BookmarkIdResponse> createBookmark(
            @Valid @RequestBody BookmarkNameRequest dto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        Long response = bookmarkService.createBookmark(customOAuth2User.getMemberId(), dto.name());
        return ResponseEntity.ok(new BookmarkIdResponse(response));
    }

    @PutMapping("/bookmarks/{bookmarkId}")
    @Operation(summary = "북마크 이름 변경")
    public ResponseEntity<Void> renameBookmark(
            @PathVariable Long bookmarkId,
            @Valid @RequestBody BookmarkNameRequest dto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.renameBookmark(customOAuth2User.getMemberId(), bookmarkId, dto.name());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bookmarks/{bookmarkId}/walkways")
    @Operation(summary = "북마크에 산책로를 추가")
    public ResponseEntity<Void> includeWalkway(
            @PathVariable Long bookmarkId,
            @Valid @RequestBody WalkwayIdRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.includeWalkway(customOAuth2User.getMemberId(), bookmarkId, request.walkwayId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bookmarks/{bookmarkId}/walkways/{walkwayId}")
    @Operation(summary = "북마크에 산책로를 제거")
    public ResponseEntity<Void> excludeWalkway(
            @PathVariable Long bookmarkId,
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.excludeWalkway(customOAuth2User.getMemberId(), bookmarkId, walkwayId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    @Operation(summary = "북마크 삭제")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        bookmarkService.deleteBookmark(customOAuth2User.getMemberId(), bookmarkId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmarks/{bookmarkId}/walkways")
    @Operation(summary = "북마크에 저장된 산책로 조회")
    public ResponseEntity<CursorResponse<BookmarkWalkwaysResponse>> getBookmarkWalkways(
            @PathVariable Long bookmarkId,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        PagingResponse<MarkedWalkway> response = bookmarkService.getBookmarkWalkways(customOAuth2User.getMemberId(), bookmarkId, new CursorRequest(lastId, size));
        return ResponseEntity.ok(new CursorResponse<>(BookmarkWalkwaysResponse.from(response.data()), response.hasNext()));
    }

    @Operation(summary = "사용자가 북마크한 산책로 제목 리스트 보기")
    @GetMapping("/users/bookmarks/title")
    public ResponseEntity<CursorResponse<BookmarksNameResponse>> getBookmarksName(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        PagingResponse<Bookmark> response = bookmarkService.getUserBookmarksName(customOAuth2User.getMemberId(), new CursorRequest(lastId, size));
        return ResponseEntity.ok(new CursorResponse<>(BookmarksNameResponse.from(response.data()), response.hasNext()));
    }
}
