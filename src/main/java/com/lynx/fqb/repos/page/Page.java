package com.lynx.fqb.repos.page;

import java.util.List;

public interface Page<T> {

    int getOffset();

    int getLimit();

    long getTotal();

    List<T> getContent();
}
