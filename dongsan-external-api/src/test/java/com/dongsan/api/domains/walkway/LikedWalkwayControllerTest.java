package com.dongsan.api.domains.walkway;

import static member.MemberFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.dongsan.api.domains.auth.AuthUserDto;
import com.dongsan.api.domains.auth.CustomAuthUser;
import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.walkway.WalkwayService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LikedWalkwayController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@DisplayName("LikedWalkwayController Unit Test")
class LikedWalkwayControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	WalkwayService walkwayService;
	final Member member = createMember();
	final CustomAuthUser customOAuth2User = new CustomAuthUser(new AuthUserDto(member));

	@BeforeEach
	void setUp_Authentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, null);
		context.setAuthentication(authentication);
	}

	@Nested
	@DisplayName("createLikedWalkway 메서드는")
	class Describe_createLikedWalkwayEntityEntity {
		@Test
		@DisplayName("좋아요를 생성하고 created를 반환한다.")
		void it_returns_created() throws Exception {
			// Given
			Long walkwayId = 1L;

			// When
			ResultActions response = mockMvc.perform(post("/walkways/" + walkwayId + "/likes")
				.contentType(MediaType.APPLICATION_JSON));

			// Then
			response.andExpect(status().isOk());
		}
	}

	@Nested
	@DisplayName("deleteLikedWalkway 메서드는")
	class Describe_deleteLikedWalkwayEntityEntity {
		@Test
		@DisplayName("좋아요를 삭제하고 ok를 반환한다.")
		void it_returns_created() throws Exception {
			// Given
			Long walkwayId = 1L;

			// When
			ResultActions response = mockMvc.perform(delete("/walkways/" + walkwayId + "/likes")
				.contentType(MediaType.APPLICATION_JSON));

			// Then
			response.andExpect(status().isOk());
		}
	}
}
