package com.dongsan.api.domains.cowalk;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.api.domains.cowalk.dto.request.CreateCowalkPostRequest;
import com.dongsan.api.domains.cowalk.dto.response.CowalkPostDetailResponse;
import com.dongsan.api.domains.cowalk.dto.response.CreateCowalkPostResponse;
import com.dongsan.api.domains.cowalk.dto.response.JoinCowalkParticipantResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/crews")
@Validated
@Tag(name = "같이 산책")
public class CowalkPostController {
	private final CowalkPostFacade cowalkPostFacade;

	public CowalkPostController(CowalkPostFacade cowalkPostFacade) {
		this.cowalkPostFacade = cowalkPostFacade;
	}

	@Operation(summary = "같이 산책 생성")
	@PostMapping("/{crewId}/cowalk")
	public ResponseEntity<CreateCowalkPostResponse> createCowalkPost(
		@PathVariable Long crewId,
		@Valid @RequestBody CreateCowalkPostRequest createCowalkPostRequest,
		@AuthenticationPrincipal CustomAuthUser customOAuth2User
	) {
		Long cowalkId = cowalkPostFacade.saveCowalkPost(createCowalkPostRequest, crewId,
			customOAuth2User.getMemberId());
		return ResponseEntity.ok(new CreateCowalkPostResponse(cowalkId));
	}

	@Operation(summary = "같이 산책 상세조회")
	@GetMapping("/{crewId}/cowalk/{cowalkId}")
	public ResponseEntity<CowalkPostDetailResponse> getCowalkPost(
		@PathVariable Long crewId,
		@PathVariable Long cowalkId,
		@AuthenticationPrincipal CustomAuthUser customOAuth2User
	) {
		CowalkPostDetailResponse cowalkPostDetail
			= cowalkPostFacade.getCowalkPostDetail(cowalkId, crewId, customOAuth2User.getMemberId());
		return ResponseEntity.ok(cowalkPostDetail);
	}

	@Operation(summary = "같이 산책 참여")
	@PostMapping("/{crewId}/cowalk/{cowalkId}/join")
	public ResponseEntity<JoinCowalkParticipantResponse> joinParticipant(
		@PathVariable Long crewId,
		@PathVariable Long cowalkId,
		@AuthenticationPrincipal CustomAuthUser customOAuth2User
	) {
		Long participantId
			= cowalkPostFacade.joinCowalkPost(crewId, cowalkId, customOAuth2User.getMemberId());
		return ResponseEntity.ok(new JoinCowalkParticipantResponse(participantId));
	}
}
