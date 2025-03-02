package com.dongsan.core.domains.bookmark;

import java.time.LocalDateTime;

public record BookmarkWithMarkedStatus(
        Long bookmarkId,
        String title,
        LocalDateTime createdAt,
        boolean marked
) {
}
