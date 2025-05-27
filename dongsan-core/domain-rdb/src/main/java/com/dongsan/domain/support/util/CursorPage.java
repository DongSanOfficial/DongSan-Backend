package com.dongsan.domain.support.util;

import java.util.List;

public class CursorPage<T> {
    private final List<T> data;
    private final boolean hasNext;

    public CursorPage(List<T> data, int size) {
        this.hasNext = data.size() > size;
        this.data = hasNext ? List.copyOf(data.subList(0, size)) : List.copyOf(data);
    }

    public CursorPage(List<T> data, boolean hasNext) {
        this.data = List.copyOf(data);
        this.hasNext = hasNext;
    }

    public List<T> getData() {
        return data;
    }

    public boolean getHasNext() {
        return hasNext;
    }
}
