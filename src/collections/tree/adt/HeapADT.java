package collections.tree.adt;

import collections.exceptions.EmptyCollectionException;

public interface HeapADT<T> extends BinaryTreeADT<T> {
    /**
     * Adds the specified object to this heap.
     * 
     * @param obj the element to added to this head
     */
    public void addElement(T obj);

    /**
     * Removes element with the lowest value from this heap.
     * 
     * @return the element with the lowest value from this heap
     * @throws collections.exceptions.EmptyCollectionException if collection has no elements
     */
    public T removeMin() throws EmptyCollectionException;

    /**
     * Returns a reference to the element with the lowest value in this heap.
     * 
     * @return a reference to the element with the lowest value in this heap
     */
    public T findMin();
}