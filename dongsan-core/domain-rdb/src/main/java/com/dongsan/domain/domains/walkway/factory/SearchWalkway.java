package com.dongsan.domain.domains.walkway.factory;

import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.domains.walkway.WalkwaySort;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.support.util.CursorRequest;
import com.dongsan.domain.support.util.CursorResponse;

public interface SearchWalkway {
    WalkwaySort getSortType();

    CursorResponse<Walkway> search(SearchWalkwayQuery searchWalkwayQuery, CursorRequest paging);
}
