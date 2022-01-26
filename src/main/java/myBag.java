import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.set.ListOrderedSet;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.Set;


public class myBag<E> implements Bag<E> {
    private int size;
    private Node<E> head;

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    Node<E> node(int index) {
        Node<E> tmp = head;
        while (index-- > 0) {
            if (iterator().hasNext())
                tmp = tmp.next;
            else
                throw new NoSuchElementException();
        }
        return tmp;
    }

    E deleteNode(Node<E> node) {
        E data = node.data;
        if (size == 0) return null;

        Node<E> prev = head;
        while (prev.next != node && prev.next != null)
            prev = prev.next;

        prev.next = node.next;
        node.next = null;

        node.data = null;
        size--;
        return data;
    }

    private void addNode(int index, E data) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();
        if (size == 0) {
            head = new Node<E>(data, null);
            return;
        }
        if (index == size) {
            Node<E> newLast = new Node<E>(data, null);
            int counter = size;
            Node<E> tmpLast = head;
            while (--counter > 0) {
                tmpLast = tmpLast.next;
            }
            tmpLast.next = newLast;
            return;
        } else {
            int counter = index - 1;
            Node<E> tmpPrev = head;
            while (--counter > 0) {
                tmpPrev = tmpPrev.next;
            }
            tmpPrev.next = new Node<>(data, tmpPrev.next);
        }
        size++;
    }

    private class myIterator implements ListIterator<E> {
        private Node<E> next;
        private Node<E> lastReturned;
        private int nextIndex;

        myIterator() {
            next = head;
            nextIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return !(next == null);
        }

        @Override
        public E next() {
            if (hasNext()) {
                lastReturned = next;
                next = next.next;
                nextIndex++;
                return lastReturned.data;
            } else
                throw new NoSuchElementException();
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            if (hasPrevious()) {
                lastReturned = next = node(nextIndex - 1);
                nextIndex--;
                return lastReturned.data;
            } else
                throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            if (lastReturned != null) {
                Node<E> lastNext = lastReturned.next;
                deleteNode(lastReturned);
                if (next == lastReturned)
                    next = lastNext;
                else nextIndex--;
                lastReturned = null;
            } else
                throw new IllegalStateException();
        }

        @Override
        public void set(E e) {
            if (lastReturned != null)
                lastReturned.data = e;
            else throw new IllegalStateException();
        }

        @Override
        public void add(E e) {
            addNode(nextIndex, e);
            lastReturned = null;
            nextIndex++;
        }
    }

    public myBag() {
        head = null;
        this.size = 0;
    }

    @Override
    public int getCount(Object o) {
        return 0;
    }

    @Override
    public boolean add(E e) {
        head = new Node<E>(e, head);
        return true;
    }

    @Override
    public boolean add(E e, int i) {
        for (int j = 0; j < i; j++)
            head = new Node<E>(e, head);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        ListIterator<E> it = iterator();
        while (it.hasNext()) {
            if (it.next().equals(o)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o, int i) {
        ListIterator<E> it = iterator();
        while (it.hasNext()) {
            if (it.next().equals(o)) {
                it.remove();
                i--;
            }
            if (i == 0) return true;
        }
        return false;
    }

    @Override
    public Set<E> uniqueSet() {
        ListOrderedSet<E> set = new ListOrderedSet<>();
        set.addAll(this);
        return set;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (E e : this.uniqueSet())
            if (e.equals(o)) return true;
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        if (collection.isEmpty())
            return true;
        Iterator<?> it = collection.iterator();
        for (E e : this)
            while (it.hasNext()) {
                if (it.next().equals(e)) {
                    it.remove();
                    break;
                }
            }
        return collection.isEmpty();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty())
            return false;
        for (E e : c) add(e);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection.isEmpty())
            return false;
        int res = 0;
        for (Object o : collection)
            if (remove(o))
                res++;
        return res > 0;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (collection.isEmpty()) {
            clear();
            return true;
        }
        int res = 0;
        for (Object o : collection)
            if (!contains(o)) {
                remove(o);
                res++;
            }
        return res > 0;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public ListIterator<E> iterator() {
        return new myIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (E e : this) arr[i++] = e;
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        } else if (a.length > size)
            a[size] = null;

        int i = 0;
        for (E e : this)
            a[i++] = (T) e;
        return a;
    }


    public static void main(String[] args) {

        myBag<Integer> bag = new myBag<>();
        HashBag<Integer> libBag = new HashBag<>();
        libBag.remove(25, 2);
    }
}
