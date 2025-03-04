package com.dongsan.core.support.util;

import java.util.List;

public record PagingResponse<T>(
        List<T> data,
        boolean hasNext
) {
    public static <T> PagingResponse<T> from(List<T> data, int size){
        boolean hasNext = data.size() > size;
        List<T> resultData = hasNext ? List.copyOf(data.subList(0, size)) : List.copyOf(data);
        return new PagingResponse<>(resultData, hasNext);
    }
}
