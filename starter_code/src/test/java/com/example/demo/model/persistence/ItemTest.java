package com.example.demo.model.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemTest {

    Item item;

    @Before
    public void setUp() {
        item = new Item();
    }

    @Test
    public final void testHashCode() {
        assertEquals(31, item.hashCode());

        item.setId(1L);
        assertNotEquals(31, item.hashCode());
    }

    @Test
    public final void testEqualsObjectEquals() {
        assertTrue(item.equals(item));
    }

    @Test
    public final void testEqualsObjectNull() {
        assertFalse(item.equals(null));
    }

    @Test
    public final void testEqualsDiffClass() {
        assertFalse(item.equals(Double.valueOf(0.0)));
    }

    @Test
    public final void testEqualsNullId() {
        Item second = new Item();

        assertTrue(item.equals(second));

        second.setId(2L);

        assertFalse(item.equals(second));

        item.setId(1L);

        assertFalse(item.equals(second));

        second.setId(1L);

        assertTrue(item.equals(second));
    }
}
