package solutions;

import interfaces.Decrease;
import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinHeap<E extends Comparable<E> & Decrease<E>> implements Heap<E> {
    private List<E> data;

    public MinHeap() {
        this.data = new ArrayList<>();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void add(E element) {
        this.data.add(element);
        this.heapifyUp(this.data.size() - 1);
    }

    private void heapifyUp(int index) {
        int parentIndex = getParentIndexFor(index);

        while (index > 0 && isLess(index, parentIndex)) {
            Collections.swap(this.data, index, parentIndex);
            index = parentIndex;
            parentIndex = getParentIndexFor(index);
        }
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return data.get(0);
    }

    @Override
    public E poll() {
        ensureNonEmpty();
        Collections.swap(this.data, 0, this.data.size() - 1);
        E toReturn = this.data.remove(this.data.size() - 1);
        heapifyDown();

        return toReturn;
    }

    private void heapifyDown() {
        int index = 0;
        int swapIndex = this.getLeftChildIndex(index);

        while (swapIndex < this.data.size()) {

            int rightChildIndex = this.getRightChildIndex(index);
            if (rightChildIndex < this.data.size()) {
                swapIndex = isLess(swapIndex, rightChildIndex) ? swapIndex : rightChildIndex;
            }

            if (isLess(index, swapIndex)) {
                break;
            }

            Collections.swap(this.data, index, swapIndex);
            index = swapIndex;
            swapIndex = getLeftChildIndex(index);
        }
    }

//    @Override
//    public void decrease(E element) {
//        this.data.remove(element);
//        element.decrease();
//        this.add(element);
//    }

    @Override
    public void decrease(E element) {
        int elementIndex = this.data.indexOf(element);
        E heapElement = this.data.get(elementIndex);
        heapElement.decrease();
        this.heapifyUp(elementIndex);
    }

    private boolean isLess(int firstIndex, int secondIndex) {
        return this.data.get(firstIndex).compareTo(this.data.get(secondIndex)) < 0;
    }

    private int getParentIndexFor(int index) {
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int index) {
        return 2 * index + 1;
    }

    private int getRightChildIndex(int index) {
        return 2 * index + 2;
    }

    private void ensureNonEmpty() {
        if (this.data.isEmpty()) {
            throw new IllegalStateException();
        }
    }
}
