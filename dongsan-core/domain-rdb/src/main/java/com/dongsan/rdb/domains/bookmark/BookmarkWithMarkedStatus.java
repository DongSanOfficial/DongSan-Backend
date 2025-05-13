package com.dongsan.rdb.domains.bookmark;

import java.time.LocalDateTime;

public record BookmarkWithMarkedStatus(
        Long bookmarkId,
        String title,
        LocalDateTime createdAt,
        boolean marked
) {
}
