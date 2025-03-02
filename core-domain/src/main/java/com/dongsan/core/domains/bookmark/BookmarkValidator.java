package com.dongsan.core.domains.bookmark;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class BookmarkValidator {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkValidator(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public void validateUniqueBookmarkName(Long memberId, String name){
        if(bookmarkRepository.existsByMemberIdAndName(memberId, name)){
            throw new CoreException(CoreErrorCode.SAME_BOOKMARK_NAME_EXIST);
        }
    }

    public void validateBookmarkOwner(Long memberId, Bookmark bookmark) {
        if(!bookmark.author().authorId().equals(memberId)){
            throw new CoreException(CoreErrorCode.NOT_BOOKMARK_OWNER);
        }
    }

    public void validateWalkwayNotInBookmark(Long bookmarkId, Long walkwayId) {
        if(bookmarkRepository.isWalkwayAdded(bookmarkId, walkwayId)){
            throw new CoreException(CoreErrorCode.WALKWAY_ALREADY_EXIST_IN_BOOKMARK);
        }
    }

    public void validateWalkwayExistsInBookmark(Long bookmarkId, Long walkwayId){
        if(!bookmarkRepository.isWalkwayAdded(bookmarkId, walkwayId)){
            throw new CoreException(CoreErrorCode.WALKWAY_NOT_EXIST_IN_BOOKMARK);
        }
    }

}
