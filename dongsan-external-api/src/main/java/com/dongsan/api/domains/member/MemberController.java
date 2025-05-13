package com.dongsan.api.domains.member;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.rdb.domains.member.Member;
import com.dongsan.rdb.domains.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Tag(name = "마이페이지")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 유저 프로필 조회
     */
    @Operation(summary = "사용자 프로필 조회")
    @GetMapping("/users/profile")
    public ResponseEntity<MemberProfileResponse> getProfile(
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        Member member = memberService.getMember(customOAuth2User.getMemberId());
        return ResponseEntity.ok(new MemberProfileResponse(member));
    }

    /**
     * 닉네임 변경
     */
    @Operation(summary = "닉네임 변경")
    @PatchMapping("/users/profile/nickname")
    public ResponseEntity<Void> patchNickname(
            @Valid @RequestBody PatchNicknameRequest request,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        memberService.patchNickname(customOAuth2User.getMemberId(), request.nickname().trim());
        return ResponseEntity.ok().build();
    }
}
