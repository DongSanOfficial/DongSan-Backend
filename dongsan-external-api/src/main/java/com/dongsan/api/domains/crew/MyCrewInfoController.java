package com.dongsan.api.domains.crew;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.crew.dto.response.GetCrewsResponse;
import com.dongsan.api.domains.crew.dto.response.GetMyCrewIdsResponse;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users/crews")
@Validated
@Tag(name = "마이페이지")
public class MyCrewInfoController {
    private final CrewInfoFacade crewInfoFacade;

    public MyCrewInfoController(CrewInfoFacade crewInfoFacade) {
        this.crewInfoFacade = crewInfoFacade;
    }

    @Operation(summary = "나의 크루 조회(커뮤니티 메인)")
    @GetMapping()
    public ResponseEntity<CursorResponse<GetCrewsResponse>> getMyCrews(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") Integer size,
            @AuthenticationPrincipal CustomAuthUser customAuthUser
    ) {
        CursorRequest cursorRequest = new CursorRequest(lastId, size);
        CursorResponse<GetCrewsResponse> result
                = crewInfoFacade.getMyCrews(customAuthUser.getMemberId(), cursorRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "나의 크루 아이디 조회")
    @GetMapping("/ids")
    public ResponseEntity<GetMyCrewIdsResponse> getMyCrewIds(
            @AuthenticationPrincipal CustomAuthUser customAuthUser
    ) {
        GetMyCrewIdsResponse result
                = crewInfoFacade.getMyCrewIds(customAuthUser.getMemberId());
        return ResponseEntity.ok(result);
    }
}
