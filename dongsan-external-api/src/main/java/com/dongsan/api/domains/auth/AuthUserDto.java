package com.dongsan.api.domains.auth;

import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberRole;

public record AuthUserDto(
	Long memberId,
	String email,
	String nickname,
	String profileImageUrl,
	MemberRole role
) {
	public AuthUserDto(Member member) {
		this(member.id(), member.email(), member.nickname(), member.profileImageUrl(), member.role());
	}

	public Member toMember() {
		return new Member(memberId, email, nickname, profileImageUrl, role);
	}
}
