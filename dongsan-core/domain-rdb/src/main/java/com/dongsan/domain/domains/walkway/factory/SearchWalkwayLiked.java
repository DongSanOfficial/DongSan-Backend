package com.dongsan.domain.domains.walkway.factory;

import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.domains.walkway.WalkwaySort;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.domain.WalkwayRepository;
import com.dongsan.domain.support.util.CursorRequest;
import com.dongsan.domain.support.util.CursorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchWalkwayLiked implements SearchWalkway {

    private final WalkwayRepository walkwayRepository;

    public SearchWalkwayLiked(WalkwayRepository walkwayRepository) {
        this.walkwayRepository = walkwayRepository;
    }

    @Override
    public WalkwaySort getSortType() {
        return WalkwaySort.LIKED;
    }

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<Walkway> search(SearchWalkwayQuery searchWalkwayQuery, CursorRequest paging) {
        return walkwayRepository.searchWalkwaysLiked(searchWalkwayQuery, paging.lastId(), paging.size());
    }
}
