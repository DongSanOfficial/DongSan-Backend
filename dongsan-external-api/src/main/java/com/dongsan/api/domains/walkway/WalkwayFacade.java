package com.dongsan.api.domains.walkway;

import com.dongsan.api.domains.bookmark.BookmarkFacade;
import com.dongsan.file.service.S3FileService;
import com.dongsan.rdb.domains.bookmark.service.BookmarkRdbService;
import com.dongsan.rdb.domains.image.ImageService;
import com.dongsan.rdb.domains.walkway.service.WalkwayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalkwayFacade {
    private final WalkwayService walkwayService;
    private final BookmarkRdbService bookmarkRdbService;
    private final BookmarkFacade bookmarkFacade;
    private final S3FileService s3FileService;
    private final ImageService imageService;
}
