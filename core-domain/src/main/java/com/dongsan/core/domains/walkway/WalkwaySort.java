package com.dongsan.core.domains.walkway;

import java.util.Arrays;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;

public enum WalkwaySort {
	LIKED("liked"),
	RATING("rating");

	private final String type;

	WalkwaySort(String type) {
		this.type = type;
	}

	public static WalkwaySort typeOf(String type) {
		return Arrays.stream(WalkwaySort.values())
			.filter(sort -> sort.type.equals(type))
			.findFirst()
			.orElseThrow(() -> new CoreException(CoreErrorCode.INVALID_SEARCH_TYPE));
	}
}
