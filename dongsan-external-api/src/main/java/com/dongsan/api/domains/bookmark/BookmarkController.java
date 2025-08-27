package com.dongsan.api.domains.bookmark;

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

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.domain.domains.bookmark.domain.Bookmark;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "북마크")
@Validated
public class BookmarkController {
    private final BookmarkFacade bookmarkFacade;

    public BookmarkController(BookmarkFacade bookmarkFacade) {
        this.bookmarkFacade = bookmarkFacade;
    }

    @PostMapping("/bookmarks")
    @Operation(summary = "북마크 생성")
    public ResponseEntity<BookmarkIdResponse> createBookmark(
            @Valid @RequestBody BookmarkNameRequest dto,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        Long response = bookmarkFacade.save(customOAuth2User.getMemberId(), dto.name());
        return ResponseEntity.ok(new BookmarkIdResponse(response));
    }

    @PutMapping("/bookmarks/{bookmarkId}")
    @Operation(summary = "북마크 이름 변경")
    public ResponseEntity<Void> renameBookmark(
            @PathVariable Long bookmarkId,
            @Valid @RequestBody BookmarkNameRequest dto,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        bookmarkFacade.rename(customOAuth2User.getMemberId(), bookmarkId, dto.name());
        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/bookmarks/{bookmarkId}/walkways")
    @Operation(summary = "북마크에 산책로를 추가")
    public ResponseEntity<Void> includeWalkway(
            @PathVariable Long bookmarkId,
            @Valid @RequestBody WalkwayIdRequest request,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        bookmarkFacade.includeWalkway(customOAuth2User.getMemberId(), bookmarkId, request.walkwayId());
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/bookmarks/{bookmarkId}/walkways/{walkwayId}")
    @Operation(summary = "북마크에 산책로를 제거")
    public ResponseEntity<Void> excludeWalkway(
            @PathVariable Long bookmarkId,
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        bookmarkFacade.excludeWalkway(customOAuth2User.getMemberId(), bookmarkId, walkwayId);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    @Operation(summary = "북마크 삭제")
    public ResponseEntity<Void> deleteBookmark(
            @PathVariable Long bookmarkId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        bookmarkFacade.delete(customOAuth2User.getMemberId(), bookmarkId);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/bookmarks/{bookmarkId}/walkways")
    @Operation(summary = "북마크에 저장된 산책로 조회")
    public ResponseEntity<CursorResponse<MarkedWalkwayResponse>> getBookmarkWalkways(
            @PathVariable Long bookmarkId,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorResponse<MarkedWalkwayResponse> response = bookmarkFacade.getBookmarkWalkways(customOAuth2User.getMemberId(),
                bookmarkId, new CursorRequest(lastId, size));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자가 북마크한 산책로 제목 리스트 보기")
    @GetMapping("/users/bookmarks/title")
    public ResponseEntity<CursorResponse<BookmarksNameResponse>> getBookmarksName(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorResponse<Bookmark> response = bookmarkFacade.getUserBookmark(customOAuth2User.getMemberId(),
                new CursorRequest(lastId, size));
        return ResponseEntity.ok(new CursorResponse<>(BookmarksNameResponse.from(response.data()), response.hasNext()));
    }
}
