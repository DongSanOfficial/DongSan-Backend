package com.dongsan.api.support.response;

import java.util.List;

public record CursorResponse<T>(
        List<T> data,
        boolean hasNext
) {
}
