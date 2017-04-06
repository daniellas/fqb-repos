package com.lynx.fqb.repos.page;

import org.junit.Assert;
import org.junit.Test;

public class PageTest {

    @Test
    public void shouldCalculateOffset() {
        Assert.assertEquals(0, Page.offset(0, 0, 100));
        Assert.assertEquals(0, Page.offset(1, 1, 100));
    }
}
