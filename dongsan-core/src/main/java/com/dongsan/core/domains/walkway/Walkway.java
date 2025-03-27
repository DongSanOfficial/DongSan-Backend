package com.dongsan.core.domains.walkway;

import java.time.LocalDateTime;
import java.util.List;

import com.dongsan.core.support.util.Author;

public record Walkway(
	Long walkwayId,
	String name,
	LocalDateTime createdAt,
	String memo,
	Stat stat,
	List<String> hashtags,
	CourseInfo courseInfo,
	Author author,
	ExposeLevel exposeLevel
) {

}
