package com.dongsan.core.domains.walkway;

import java.util.List;

public interface SearchWalkway {
    WalkwaySort getSortType();

    List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery);
}
