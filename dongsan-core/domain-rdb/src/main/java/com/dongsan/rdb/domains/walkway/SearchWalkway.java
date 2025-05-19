package com.dongsan.rdb.domains.walkway;

import java.util.List;

public interface SearchWalkway {
	WalkwaySort getSortType();

	List<Walkway> search(SearchWalkwayQuery searchWalkwayQuery);
}
