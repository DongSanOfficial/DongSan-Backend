package com.dongsan.api.support.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.bookmark.domain.Bookmark;
import com.dongsan.domain.domains.bookmark.domain.BookmarkRepository;

@Component
public class BookmarkFactory {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    public Long save(Long memberId) {
        return bookmarkRepository.save(new Bookmark("test", memberId));
    }
}
