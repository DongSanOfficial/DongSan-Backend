package com.dongsan.core.domains.bookmark;

import com.dongsan.core.support.util.Author;
import java.time.LocalDateTime;

public record Bookmark(
        Long bookmarkId,
        String title,
        Author author,
        LocalDateTime createdAt
) {
}
