package com.lynx.fqb.repos;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsCollectionContaining.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.lynx.fqb.repos.page.DefaultPage;
import com.lynx.fqb.repos.page.Page;

public class DefaultPageTest {

    private static final Page<Long> PAGE = DefaultPage.of(1, 100, 1000, Arrays.asList(1l, 2l));

    @Test
    public void shouldHasCorrectOffset() {
        Assert.assertThat(PAGE.getOffset(), is(1));
    }

    @Test
    public void shouldHasCorrectLimit() {
        Assert.assertThat(PAGE.getLimit(), is(100));
    }

    @Test
    public void shouldHasCorrectTotal() {
        Assert.assertThat(PAGE.getTotal(), is(1000l));
    }

    @Test
    public void shouldHasCorrectContent() {
        Assert.assertThat(PAGE.getContent(), hasItems(1l, 2l));
    }

}
