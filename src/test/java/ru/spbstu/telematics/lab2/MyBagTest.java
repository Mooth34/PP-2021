package ru.spbstu.telematics.lab2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
    public void emptyList() {
        Assert.assertEquals("Lengths are not equal", my.size(), original.size());
    }

    @Test
    public void initialization() {
        my = new MyBag<>(Arrays.asList(-2, -1, 0, 1, 2));
        original = new TreeBag<>(Arrays.asList(-2, -1, 0, 1, 2));
        Assert.assertEquals("Contents are not equal", original, my);
    }

    @Test
    public void size() {
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
    public void contains() {
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
    public void add() {
        my.add(3);
        my.add(1);

        original.add(3);
        original.add(1);

        Assert.assertEquals("Contents are not equal", original, my);

        my.add(3, 5);
        my.add(1, 10);

        original.add(3, 5);
        original.add(1, 10);

        Assert.assertEquals("Contents are not equal", original, my);
    }

    @Test
    public void remove() {
        my = new MyBag<>(Arrays.asList(-2, -1, 0, 1, 2));
        original = new TreeBag<>(Arrays.asList(-2, -1, 0, 1, 2));

        original.remove(0);
        original.remove(2);

        my.remove(0);
        my.remove(2);

        Assert.assertEquals("Contents are not equal after remove", original, my);
    }

    @Test
    public void getCount() {
        my.add(3, 5);
        my.add(1, 10);

        original.add(3, 5);
        original.add(1, 10);

        Assert.assertEquals("Contents are not equal after remove", original.getCount(3), my.getCount(3));
        Assert.assertEquals("Contents are not equal after remove", original.getCount(1), my.getCount(1));
    }

    @Test
    public void uniqueSet() {
        my.add(3, 5);
        my.add(1, 10);
        my.add(0);

        original.add(3, 5);
        original.add(1, 10);
        original.add(0);

        Assert.assertEquals("Contents are not equal after remove", original.uniqueSet(), my.uniqueSet());
    }

    @Test
    public void isEmpty() {
        Assert.assertEquals("Contents are not equal after remove", original.isEmpty(), my.isEmpty());
        my.add(3);
        my.add(1);

        original.add(3);
        original.add(1);

        Assert.assertEquals("Contents are not equal after remove", original.isEmpty(), my.isEmpty());

        my.remove(3);
        my.remove(1);

        original.remove(3);
        original.remove(1);

        Assert.assertEquals("Contents are not equal after remove", original.isEmpty(), my.isEmpty());

        my.add(3, 5);
        my.add(1, 10);

        original.add(3, 5);
        original.add(1, 10);

        my.clear();
        original.clear();

        Assert.assertEquals("Contents are not equal after remove", original.isEmpty(), my.isEmpty());
    }

    @Test
    public void containsAll() {
        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        ArrayList<Integer> list = new ArrayList<>();

        Assert.assertEquals("Contents are not equal after remove", original.containsAll(list), my.containsAll(list));

        list.add(3);
        list.add(3);

        Assert.assertEquals("Contents are not equal after remove", original.containsAll(list), my.containsAll(list));

        list.add(1);

        Assert.assertEquals("Contents are not equal after remove", original.containsAll(list), my.containsAll(list));

        list.add(5);

        Assert.assertEquals("Contents are not equal after remove", original.containsAll(list), my.containsAll(list));
    }

    @Test
    public void addAll() {
        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        ArrayList<Integer> list = new ArrayList<>();

        Assert.assertEquals("Contents are not equal after remove", original.addAll(list), my.addAll(list));

        list.add(5);
        list.add(5);

        Assert.assertEquals("Contents are not equal after remove", original.addAll(list), my.addAll(list));

        list.add(0);

        Assert.assertEquals("Contents are not equal after remove", original.addAll(list), my.addAll(list));
    }

    @Test
    public void removeAll() {
        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        ArrayList<Integer> list = new ArrayList<>();

        Assert.assertEquals("Contents are not equal after remove", original.removeAll(list), my.removeAll(list));

        list.add(3);
        list.add(3);

        Assert.assertEquals("Contents are not equal after remove", original.removeAll(list), my.removeAll(list));

        list.add(1);

        Assert.assertEquals("Contents are not equal after remove", original.removeAll(list), my.removeAll(list));

        list.add(5);

        Assert.assertEquals("Contents are not equal after remove", original.removeAll(list), my.removeAll(list));

    }

    @Test
    public void retainAll() {
        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        ArrayList<Integer> list = new ArrayList<>();

        Assert.assertEquals("Contents are not equal after remove", original.retainAll(list), my.retainAll(list));

        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        list.add(3);
        list.add(3);

        Assert.assertEquals("Contents are not equal after remove", original.retainAll(list), my.retainAll(list));

        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        list.add(1);

        Assert.assertEquals("Contents are not equal after remove", original.retainAll(list), my.retainAll(list));

        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        list.add(5);

        Assert.assertEquals("Contents are not equal after remove", original.retainAll(list), my.retainAll(list));
    }

    @Test
    public void clear() {
        original.clear();
        my.clear();

        Assert.assertEquals("Contents are not equal after remove", original, my);

        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        Assert.assertEquals("Contents are not equal after remove", original, my);

        original.clear();
        my.clear();

        Assert.assertEquals("Contents are not equal after remove", original, my);

    }

    @Test
    public void iterator() {
        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        Iterator<Integer> myIterator = my.iterator();
        Iterator<Integer> originalIterator = original.iterator();

        while (myIterator.hasNext() && originalIterator.hasNext())
            Assert.assertEquals("Contents are not equal after remove", myIterator.next(), originalIterator.next());

        Assert.assertEquals(myIterator.hasNext(), originalIterator.hasNext());
    }

    @Test
    public void toArray() {
        Assert.assertEquals("Contents are not equal after remove", my.toArray(), original.toArray());

        my.add(3, 2);
        my.add(1, 3);

        original.add(3, 2);
        original.add(1, 3);

        Assert.assertEquals("Contents are not equal after remove", my.toArray(), original.toArray());
    }
}