package com.lynx.fqb.repos.page;

import java.util.List;

public interface Page<T> {

    int getOffset();

    int getLimit();

    long getTotal();

    List<T> getContent();

    public static int offset(int firstPageIdx, int page, int limit) {
        return (page - firstPageIdx) * limit;
    }
}
