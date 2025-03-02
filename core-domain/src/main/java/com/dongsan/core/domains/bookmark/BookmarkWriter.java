package com.dongsan.core.domains.bookmark;

import org.springframework.stereotype.Component;

@Component
public class BookmarkWriter {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkWriter(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public Long createBookmark(Long memberId, String name) {
        return bookmarkRepository.save(memberId, name);
    }

    public void renameBookmark(Long bookmarkId, String name) {
        bookmarkRepository.rename(bookmarkId, name);
    }

    public void includeWalkway(Long bookmarkId, Long walkwayId) {
        bookmarkRepository.includeWalkway(bookmarkId, walkwayId);
    }

    public void excludeWalkway(Long bookmarkId, Long walkwayId) {
        bookmarkRepository.excludeWalkway(bookmarkId, walkwayId);
    }

    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

}
