package com.dongsan.api.domains.walkway.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.dongsan.rdb.domains.walkway.Walkway;

public record MyWalkwayResponse(
	Long walkwayId,
	String name,
	LocalDate date,
	Double distance,
	String courseImageUrl,
	Integer time,
	String memo,
	Integer likeCount,
	Integer reviewCount,
	Double rating,
	List<String> hashtags
) {

	public MyWalkwayResponse(Walkway w) {
		this(
			w.walkwayId(),
			w.name(),
			w.createdAt()
				.toLocalDate(),
			w.courseInfo()
				.distance(),
			w.courseInfo()
				.courseImageUrl(),
			w.courseInfo()
				.time(),
			w.memo(),
			w.stat()
				.likeCount(),
			w.stat()
				.reviewCount(),
			w.stat()
				.rating(),
			w.hashtags()
		);
	}

	public static List<MyWalkwayResponse> from(List<Walkway> walkways) {
		return walkways.stream()
			.map(MyWalkwayResponse::new)
			.toList();
	}

}
