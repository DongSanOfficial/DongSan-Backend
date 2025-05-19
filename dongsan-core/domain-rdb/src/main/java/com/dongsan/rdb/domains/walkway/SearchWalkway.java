package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.domains.walkway.domain.Walkway;

import java.util.List;

public interface SearchWalkway {
    WalkwaySort getSortType();

    List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery);
}
