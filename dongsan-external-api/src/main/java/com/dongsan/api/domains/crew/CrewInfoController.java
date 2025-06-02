package com.dongsan.api.domains.crew;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.crew.dto.request.CreateCrewRequest;
import com.dongsan.api.domains.crew.dto.request.CreateCrewResponse;
import com.dongsan.api.domains.crew.dto.response.*;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/crews")
@Validated
@Tag(name = "크루")
public class CrewInfoController {
    private final CrewInfoFacade crewInfoFacade;

    public CrewInfoController(CrewInfoFacade crewInfoFacade) {
        this.crewInfoFacade = crewInfoFacade;
    }

    @Operation(summary = "크루 이름 중복 체크")
    @GetMapping("/exists")
    public ResponseEntity<IsNameDuplicatedResponse> isNameDuplicated(
            @RequestParam @NotBlank String name
    ) {
        boolean isValid = crewInfoFacade.isNameDuplicated(name);
        return ResponseEntity.ok(new IsNameDuplicatedResponse(isValid));
    }

    @Operation(summary = "크루 정보 조회")
    @GetMapping("/{crewId}/info")
    public ResponseEntity<GetCrewInfoResponse> getCrewInfo(
            @PathVariable Long crewId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        GetCrewInfoResponse response = crewInfoFacade.getCrewInfo(crewId, customOAuth2User.getMemberId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "크루 피드 조회")
    @GetMapping("/{crewId}/feeds")
    public ResponseEntity<CursorResponse<GetCrewFeedResponse>> getCrewFeed(
            @PathVariable Long crewId,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CursorResponse<GetCrewFeedResponse> response = crewInfoFacade.getCrewFeed(crewId, customOAuth2User.getMemberId(),
                new CursorRequest(lastId, size));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "크루원 랭킹 조회")
    @GetMapping("/{crewId}/ranking")
    public ResponseEntity<CursorResponse<GetCrewMemberRankingResponse>> getCrewMemberRanking(
            @PathVariable Long crewId,
            @RequestParam String period,
            @RequestParam LocalDate date,
            @RequestParam String sort,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        CrewRankingSort rankingSort = CrewRankingSort.typeOf(sort);
        CrewRankingPeriod rankingPeriod = CrewRankingPeriod.typeOf(period);
        CursorRequest cursorRequest = new CursorRequest(lastId, size);
        CursorResponse<GetCrewMemberRankingResponse> response = crewInfoFacade.getCrewMemberRanking(crewId, customOAuth2User.getMemberId(), date,
                rankingSort, rankingPeriod, cursorRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "크루 등록")
    @PostMapping()
    public ResponseEntity<CreateCrewResponse> createCrew(
            @Valid @RequestBody CreateCrewRequest request,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        Long crewId = crewInfoFacade.saveCrew(request, customOAuth2User.getMemberId());
        return ResponseEntity.ok(new CreateCrewResponse(crewId));
    }

    @Operation(summary = "크루 이미지 등록")
    @PostMapping(value = "/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreateCrewImageResponse> createCrewImage(
            @RequestPart MultipartFile crewImage
    ) {
        CreateCrewImageResponse response = crewInfoFacade.saveImage(crewImage);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "크루 탈퇴")
    @DeleteMapping(value = "/{crewId}/members")
    public ResponseEntity<Void> leaveCrew(
            @PathVariable Long crewId,
            @AuthenticationPrincipal CustomAuthUser customOAuth2User
    ) {
        crewInfoFacade.leaveCrew(crewId, customOAuth2User.getMemberId());
        return ResponseEntity.ok().build();
    }


}
