package sk.pa3kc;

import java.util.AbstractList;

public class MList<T> extends AbstractList<T> {
    Object[] arr = new Object[8];
    int index = 0;

    @Override
    public boolean add(T e) {
        ensureCapacity();
        this.arr[this.index++] = e;
        return true;
    }

    @Override
    public T get(int index) {
        return (T)this.arr[index];
    }

    @Override
    public int size() {
        return this.index;
    }

    private void ensureCapacity() {
        if (this.index+1 == this.arr.length) {
            final int length = this.arr.length;
            System.arraycopy(this.arr, 0, (this.arr = new Object[length*2]), 0, length);
        }
    }
}
