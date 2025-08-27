package com.dongsan.domain.support.paging;

import java.util.List;

public record CursorResponse<T>(
        List<T> data,
        boolean hasNext
) {
    public CursorResponse(List<T> data, int size) {
        this(data.size() > size ? List.copyOf(data.subList(0, size)) : List.copyOf(data),
                data.size() > size);
    }
}

