package com.dongsan.core.support.util;

public record CursorPagingRequest(
        Long lastId,
        Integer size
) {
}
