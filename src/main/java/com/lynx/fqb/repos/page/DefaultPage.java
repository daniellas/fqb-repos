package com.lynx.fqb.repos.page;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class DefaultPage<T> implements Page<T> {

    private static final int FIRST_PAGE_IDX = 0;

    @Getter
    private final int offset;

    @Getter
    private final int limit;

    @Getter
    private final long total;

    @Getter
    private final List<T> content;

    public static int offset(int page, int limit) {
        return Page.offset(FIRST_PAGE_IDX, page, limit);
    }
}
