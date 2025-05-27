package com.dongsan.domain.support.util;

public record CursorRequest(
        Long lastId,
        int size
) {
    private static final int DEFAULT_PAGE_SIZE = 10;

    public CursorRequest(Long lastId, Integer size) {
        this(lastId, (size == null || size <= 0) ? DEFAULT_PAGE_SIZE : size);
    }
}
