package com.dongsan.core.support.util;

import java.util.List;

public record CursorPagingResponse<T>(
        List<T> data,
        boolean hasNext
) {
    public static <T> CursorPagingResponse<T> from(List<T> data, Integer size){
        boolean hasNext = data.size() > size;
        if(hasNext){
            data.remove(data.size()-1);
        }
        return new CursorPagingResponse<>(data, hasNext);
    }
}
