package com.dongsan.api.support.factory;

import com.dongsan.domain.domains.bookmark.domain.Bookmark;
import com.dongsan.domain.domains.bookmark.domain.BookmarkRepository;
import com.dongsan.domain.domains.bookmark.domain.MarkedWalkwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookmarkFactory {
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private MarkedWalkwayRepository markedWalkwayRepository;

    public Long save(Long memberId) {
        return bookmarkRepository.save(new Bookmark("test", memberId));
    }

    public Long save(Long memberId, String bookmarkName) {
        return bookmarkRepository.save(new Bookmark(bookmarkName, memberId));
    }

    public void includeWalkway(Long bookmarkId, Long walkwayId) {
        markedWalkwayRepository.includeWalkway(bookmarkId, walkwayId);
    }

}
