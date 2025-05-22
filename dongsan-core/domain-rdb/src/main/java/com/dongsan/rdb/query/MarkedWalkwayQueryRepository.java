package com.dongsan.rdb.query;

import com.dongsan.rdb.support.util.CursorPage;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MarkedWalkwayQueryRepository {
    CursorPage<MarkedWalkwayParam> getBookmarkWalkway(Long memberId, Long bookmarkId, LocalDateTime lastCreatedAt, int size);
}
