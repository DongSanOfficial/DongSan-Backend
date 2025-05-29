package com.dongsan.api.domains.crew;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.crew.dto.request.CreateCrewRequest;
import com.dongsan.api.domains.crew.dto.request.CreateCrewResponse;
import com.dongsan.api.domains.crew.dto.response.CreateCrewImageResponse;
import com.dongsan.api.domains.crew.dto.response.IsNameDuplicatedResponse;
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
            @RequestParam(name = "name", required = false) @NotBlank String name
    ) {
        boolean isValid = crewInfoFacade.isNameDuplicated(name);
        return ResponseEntity.ok(new IsNameDuplicatedResponse(isValid));
    }

    @Operation(summary = "크루 등록")
    @PostMapping()
    public ResponseEntity<CreateCrewResponse> createCrew(
            @RequestBody @Valid CreateCrewRequest request,
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
            @RequestPart("crewImage") MultipartFile crewImage
    ) {
        CreateCrewImageResponse response = crewInfoFacade.saveImage(crewImage);
        return ResponseEntity.ok(response);
    }


}
