package com.dongsan.api.domains.cowalk;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.cowalk.dto.response.GetMyCowalkResponse;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/cowalk")
@Validated
@Tag(name = "마이페이지")
public class MyCowalkController {

    private final MyCowalkFacade myCowalkFacade;

    public MyCowalkController(MyCowalkFacade myCowalkFacade) {
        this.myCowalkFacade = myCowalkFacade;
    }

    @Operation(summary = "내가 신청한 같이 산책하기 목록 조회")
    @GetMapping()
    public ResponseEntity<CursorResponse<GetMyCowalkResponse>> getMyCowalk(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "3") Integer size,
            @AuthenticationPrincipal CustomAuthUser customAuthUser
    ) {
        CursorRequest cursorRequest = new CursorRequest(lastId, size);
        CursorResponse<GetMyCowalkResponse> response = myCowalkFacade.getMyCowalk(cursorRequest, customAuthUser.getMemberId());
        return ResponseEntity.ok(response);
    }
}
