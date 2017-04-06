package com.lynx.fqb.repos.page;

import org.junit.Assert;
import org.junit.Test;

public class DefaultPageTest {

    @Test
    public void shouldCalculateOffset() {
        Assert.assertEquals(0, DefaultPage.offset(1, 100));
        Assert.assertEquals(100, DefaultPage.offset(2, 100));
    }
}
