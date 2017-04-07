package com.lynx.fqb.repos.sort;

import org.junit.Assert;
import org.junit.Test;

import com.lynx.fqb.repos.sort.Sort.Direction;

public class SortTest {

    @Test
    public void shouldReturnASC() {
        Assert.assertEquals(Direction.ASC, Direction.valueOf("ASC"));
    }

    @Test
    public void shouldReturnDESC() {
        Assert.assertEquals(Direction.DESC, Direction.valueOf("DESC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnInvalidDirection() {
        Direction.valueOf("X");
    }

    @Test
    public void shouldHasTwoSortDirections() {
        Assert.assertEquals(2, Direction.values().length);
    }

}
