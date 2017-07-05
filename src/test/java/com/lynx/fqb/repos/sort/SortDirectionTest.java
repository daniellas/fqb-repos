package com.lynx.fqb.repos.sort;

import static org.junit.Assert.*;
import org.junit.Test;

import com.lynx.fqb.repos.sort.Sort.Direction;

public class SortDirectionTest {

    @Test
    public void shouldReturnASC() {
        assertEquals(Direction.ASC, Direction.valueOf("ASC"));
    }

    @Test
    public void shouldReturnDESC() {
        assertEquals(Direction.DESC, Direction.valueOf("DESC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnInvalidDirection() {
        Direction.valueOf("X");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromStringShouldFailOnNullDirection() {
        Direction.fromString(null);
    }

    @Test
    public void shouldHasTwoSortDirections() {
        assertEquals(2, Direction.values().length);
    }

    @Test
    public void fromStringShouldReturnASC() {
        assertEquals(Direction.ASC, Direction.fromString("ASC"));
    }

    @Test
    public void fromStringShouldReturnDESC() {
        assertEquals(Direction.DESC, Direction.fromString("DESC"));
    }

    @Test
    public void fromStringShouldReturnASCRegardlesCase() {
        assertEquals(Direction.ASC, Direction.fromString("asc"));
    }

    @Test
    public void fromStringShouldReturnDESCRegardlesCase() {
        assertEquals(Direction.DESC, Direction.fromString("desc"));
    }

}
