package com.dongsan.api.domains.cowalk.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.dongsan.domain.domains.cowalk.domain.CapacityType;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.member.Member;

public record CowalkPostDetailResponse(
	Long cowalkId,
	String profileImageUrl,
	String nickname,
	LocalDate createdDate,
	LocalDate date,
	LocalTime time,
	Boolean limitEnable,
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
			cowalkPost.getCapacityType().equals(CapacityType.LIMITED),
			memberCount,
			cowalkPost.getCapacity(),
			commentCount
		);
	}
}
