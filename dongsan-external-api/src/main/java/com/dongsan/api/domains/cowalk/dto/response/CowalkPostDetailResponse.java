package com.dongsan.api.domains.cowalk.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.member.Member;

public record CowalkPostDetailResponse(
	Long cowalkId,
	String profileImageUrl,
	String nickname,
	LocalDate createdDate,
	LocalDate date,
	LocalTime time,
	Integer memberCount,
	Integer memberLimit,
	Integer commentCount
) {
	public CowalkPostDetailResponse(CowalkPost cowalkPost, Integer memberCount, Integer commentCount, Member member) {
		this(
			cowalkPost.getId(),
			member.getProfileImageUrl(),
			member.getNickname(),
			cowalkPost.getCreatedAt().toLocalDate(),
			cowalkPost.getDate(),
			cowalkPost.getTime(),
			memberCount,
			cowalkPost.getCapacity(),
			commentCount
		);
	}
}
