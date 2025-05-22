package com.dongsan.rdb.domains.walkway.factory;

import com.dongsan.rdb.domains.walkway.SearchWalkwayQuery;
import com.dongsan.rdb.domains.walkway.WalkwaySort;
import com.dongsan.rdb.domains.walkway.domain.Walkway;

import java.util.List;

public interface SearchWalkway {
    WalkwaySort getSortType();

    List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery);
}
