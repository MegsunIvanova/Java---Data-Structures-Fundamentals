package implementations;

import interfaces.IReversedList;

import java.util.Arrays;
import java.util.Iterator;

public class ReversedList<E> implements IReversedList<E> {
    private final int INITIAL_CAPACITY = 2;
    private Object[] elements;
    private int size;
    private int head;
    //keep the elements in the order of their adding,
    // by access them in reversed order (from end to start)

    public ReversedList() {
        this.elements = new Object[INITIAL_CAPACITY];
    }

    @Override
    public void add(E element) {
        growIfNeeded();
        this.elements[size++] = element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int capacity() {
        return this.elements.length;
    }

    @Override
    public E get(int index) {
        int realIndex = getRealIndex(index);
        ensureIndex(realIndex);
        return getAt(realIndex);
    }

    @Override
    public void removeAt(int index) {
//        int realIndex = getRealIndex(index);
        ensureIndex(index);
//        E element = getA(realIndex);
        shiftLeft(index);

//        shrinkIfNeeded();
//        return element;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = size - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public E next() {
                return getAt(index--);
            }
        };
    }

    private void growIfNeeded() {
        if (this.size() == this.capacity()) {
            int newCapacity = this.capacity() * 2;
            this.elements = Arrays.copyOf(this.elements, newCapacity);
        }
    }

//    private void shrinkIfNeeded() {
//        if (this.size() < this.capacity() / 3) {
//            this.capacity /= 2;
//            this.elements = Arrays.copyOf(this.elements, capacity);
//        }
//    }

    private int getRealIndex(int index) {
        int realIndex = this.size - 1 - index;
        return realIndex;
    }

    private void ensureIndex(int index) {
        if (this.size == 0 || index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Invalid index.");
        }
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) this.elements[index];
    }

    private void shiftLeft(int index) {
        for (int i = index + 1; i < this.capacity(); i++) {
            this.elements[i - 1] = this.elements[i];
        }
        this.size--;
        this.elements[this.size] = null;
    }
}
