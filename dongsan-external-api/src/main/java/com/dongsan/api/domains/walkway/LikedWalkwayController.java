package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.auth.CustomAuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/walkways")
@Tag(name = "산책로")
@Validated
public class LikedWalkwayController {

    private final LikedWalkwayFacade likedWalkwayFacade;

    public LikedWalkwayController(LikedWalkwayFacade likedWalkwayFacade) {
        this.likedWalkwayFacade = likedWalkwayFacade;
    }

    @Operation(summary = "산책로 좋아요")
    @PostMapping("/{walkwayId}/likes")
    public ResponseEntity<Void> createLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        likedWalkwayFacade.createLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ResponseEntity.ok()
                .build();
    }

    @Operation(summary = "산책로 좋아요 취소")
    @DeleteMapping("/{walkwayId}/likes")
    public ResponseEntity<Void> deleteLikedWalkway(
            @PathVariable Long walkwayId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        likedWalkwayFacade.deleteLikedWalkway(customOAuth2User.getMemberId(), walkwayId);
        return ResponseEntity.ok()
                .build();
    }
}
