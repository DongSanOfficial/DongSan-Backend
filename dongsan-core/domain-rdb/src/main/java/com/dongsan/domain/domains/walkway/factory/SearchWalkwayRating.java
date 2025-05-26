package com.dongsan.domain.domains.walkway.factory;

import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.support.util.CursorRequest;
import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.domains.walkway.WalkwaySort;
import com.dongsan.domain.domains.walkway.domain.WalkwayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchWalkwayRating implements SearchWalkway {
    private final WalkwayRepository walkwayRepository;

    public SearchWalkwayRating(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    @Override
    public WalkwaySort getSortType() {
        return WalkwaySort.RATING;
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPage<Walkway> search(SearchWalkwayQuery searchWalkwayQuery, CursorRequest paging) {
        return walkwayRepository.searchWalkwaysRating(searchWalkwayQuery, paging.lastId(), paging.size());
    }
}
