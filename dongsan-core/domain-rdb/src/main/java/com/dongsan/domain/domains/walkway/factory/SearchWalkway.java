package com.dongsan.domain.domains.walkway.factory;

import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.support.util.CursorRequest;
import com.dongsan.domain.domains.walkway.SearchWalkwayQuery;
import com.dongsan.domain.domains.walkway.WalkwaySort;

public interface SearchWalkway {
    WalkwaySort getSortType();

    CursorPage<Walkway> search(SearchWalkwayQuery searchWalkwayQuery, CursorRequest paging);
}
