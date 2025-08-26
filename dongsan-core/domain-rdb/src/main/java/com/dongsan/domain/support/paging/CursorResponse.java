package com.dongsan.domain.support.paging;

import java.util.List;

public record CursorResponse<T>(
        List<T> data,
        boolean hasNext
) {
    // size 기반 생성 로직을 추가하고 싶으면 보조 생성자로 작성 가능
    public CursorResponse(List<T> data, int size) {
        this(data.size() > size ? List.copyOf(data.subList(0, size)) : List.copyOf(data),
                data.size() > size);
    }

    // 기본 생성자는 record가 자동 제공하는 canonical constructor
}

