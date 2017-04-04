package com.lynx.fqb.repos.page;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class DefaultPage<T> implements Page<T> {

    @Getter
    private final int offset;

    @Getter
    private final int limit;

    @Getter
    private final long total;

    @Getter
    private final List<T> content;

}
