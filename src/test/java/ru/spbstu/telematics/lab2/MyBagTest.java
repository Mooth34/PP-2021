package ru.spbstu.telematics.lab2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import org.apache.commons.collections4.bag.TreeBag;


public class MyBagTest {

    MyBag<Integer> my;
    TreeBag<Integer> original;

    @Before
    public void init() {
        my = new MyBag<>();
        original = new TreeBag<>();
    }

    @Test
    public void testEmptyList() {
        Assert.assertEquals("Lengths are not equal", my.size(), original.size());
    }

    @Test
    public void testInitialization() {
        my = new MyBag<>(Arrays.asList(-2, -1, 0, 1, 2));
        original = new TreeBag<>(Arrays.asList(-2, -1, 0, 1, 2));
        Assert.assertEquals("Contents are not equal", original, my);
    }

    @Test
    public void testSize() {
        Assert.assertEquals("Sizes are not equal if empty", original.size(), my.size());

        my = new MyBag<>(Arrays.asList(-2, -1, 0, 1, 2));
        original = new TreeBag<>(Arrays.asList(-2, -1, 0, 1, 2));

        Assert.assertEquals("Sizes are not equal after init from collection", original.size(), my.size());

        my.add(90);
        original.add(90);

        Assert.assertEquals("Sizes are not equal after add()", original.size(), my.size());

        my.remove(Integer.valueOf(-1));
        original.remove(Integer.valueOf(-1));

        Assert.assertEquals("Sizes are not equal after remove()", original.size(), my.size());

    }

    @Test
    public void testContains() {
        Assert.assertEquals(original.contains(4), my.contains(4));

        my.add(10);
        original.add(10);

        Assert.assertEquals(original.contains(10), my.contains(10));
        Assert.assertEquals(original.contains(5), my.contains(5));

        my.remove(Integer.valueOf(10));
        original.remove(Integer.valueOf(10));

        Assert.assertEquals(original.contains(10), my.contains(10));
    }

    @Test
    public void testAdd() {
        my.add(3);
        my.add(1);

        original.add(3);
        original.add(1);

        Assert.assertEquals("Contents are not equal", original, my);
    }

    @Test
    public void testRemove() {
        my = new MyBag<>(Arrays.asList(-2, -1, 0, 1, 2));
        original = new TreeBag<>(Arrays.asList(-2, -1, 0, 1, 2));

        original.remove(Integer.valueOf(0));
        original.remove(Integer.valueOf(4));

        my.remove(Integer.valueOf(0));
        my.remove(Integer.valueOf(4));

        Assert.assertEquals("Contents are not equal after remove", original, my);
    }

    @Test
    public void getCount() {

    }

    @Test
    public void add() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void uniqueSet() {
    }

    @Test
    public void size() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void contains() {
    }

    @Test
    public void containsAll() {
    }

    @Test
    public void addAll() {
    }

    @Test
    public void removeAll() {
    }

    @Test
    public void retainAll() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void iterator() {
    }

    @Test
    public void toArray() {
    }

    @Test
    public void testToArray() {
    }
}