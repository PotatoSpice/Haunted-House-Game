package collections.tree.adt;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;

/**
 * BinarySearchTreeADT defines the interface to a binary search tree.
 *
 * @param <T> generic type for tree node elements
 */
public interface BinarySearchTreeADT<T> extends BinaryTreeADT<T> {

    /**
     * Adds the specified element to the proper location in this tree.
     *
     * @param element the element to be added to this tree
     */
    public void addElement(T element);

    /**
     * Removes and returns the specified element from this tree.
     *
     * @param targetElement the element to be removed from this tree
     * @return the element removed from this tree
     * @throws ElementNotFoundException if target element is not found
     * @throws EmptyCollectionException if collection has no elements
     */
    public T removeElement(T targetElement) throws ElementNotFoundException, EmptyCollectionException;

    /**
     * Removes all occurences of the specified element from this tree.
     *
     * @param targetElement the element that the list will have all instances of
     * it removed
     * @throws ElementNotFoundException if target element is not found
     * @throws EmptyCollectionException if collection has no elements
     */
    public void removeAllOccurrences(T targetElement) throws ElementNotFoundException, EmptyCollectionException;

    /**
     * Removes and returns the smallest element from this tree.
     *
     * @return the smallest element from this tree.
     * @throws EmptyCollectionException if collection has no elements
     */
    public T removeMin() throws EmptyCollectionException;

    /** Removes and returns the largest element from this tree.
     *
     * @return the largest element from this tree
     * @throws EmptyCollectionException if collection has no elements
     */
    public T removeMax() throws EmptyCollectionException;

    /** Returns a reference to the smallest element in this tree.
     *
     * @return a reference to the smallest element in this tree
     * @throws EmptyCollectionException if collection has no elements
     */
    public T findMin() throws EmptyCollectionException;

    /** Returns a reference to the largest element in this tree.
     *
     * @return a reference to the largest element in this tree
     * @throws EmptyCollectionException if collection has no elements
     */
    public T findMax() throws EmptyCollectionException;
}
