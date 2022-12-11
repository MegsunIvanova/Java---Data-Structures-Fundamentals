package implementations;

import interfaces.IReversedList;

import java.util.Iterator;

public class ReversedListtt<E> implements IReversedList<E> {
    private final int INITIAL_CAPACITY = 2;
    private Object[] elements;
    private int nextFree;
    //keep the elements in the order of their adding,
    // by access them in reversed order (from end to start)

    public ReversedListtt() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.nextFree = this.elements.length - 1;
    }

    @Override
    public void add(E element) {
        growIfNeeded();
        this.elements[nextFree--] = element;
    }

    @Override
    public int size() {
        return capacity() - (nextFree + 1);
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
        int realIndex = getRealIndex(index);
        ensureIndex(realIndex);
        shiftRight(realIndex);

    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = nextFree+1;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public E next() {
                return getAt(index++);
            }
        };
    }

    private void growIfNeeded() {
        if (this.nextFree == -1) {
            Object[] newElements = new Object[this.elements.length * 2];
            int index = newElements.length - 1;
            for (int i = this.elements.length - 1; i >= 0; i--) {
                newElements[index--] = this.elements[i];
            }

            this.nextFree = index;
            this.elements = newElements;
        }
    }

    private int getRealIndex(int index) {
        int realIndex = index + nextFree + 1;
        return realIndex;
    }

    private void ensureIndex(int index) {
        if (this.size() == 0 || index <= this.nextFree || index >= this.elements.length) {
            throw new IndexOutOfBoundsException("Invalid index.");
        }
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) this.elements[index];
    }

    private void shiftRight(int index) {
        for (int i = index; i > this.nextFree; i--) {
            this.elements[i] = this.elements[i-1];
        }
        this.nextFree++;
    }
}
