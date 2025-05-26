package com.dongsan.rdb.domains.walkway.factory;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.WalkwaySort;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.support.util.CursorPage;
import com.dongsan.rdb.support.util.CursorRequest;

public interface SearchWalkway {
    WalkwaySort getSortType();

    CursorPage<Walkway> search(SearchWalkwayQuery searchWalkwayQuery, CursorRequest paging);
}
