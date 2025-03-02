package com.dongsan.core.domains.bookmark;

public record GetBookmarkDetailParam(
        Long memberId,
        Long bookmarkId,
        Integer size,
        Long lastId
) {
}
