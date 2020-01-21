package collections.list.adt;

import collections.exceptions.ElementNotFoundException;

public interface UnorderedListADT<T> extends ListADT<T> {

    /**
     * Adds the specified element to the front of this list
     *
     * @param element the element to be added to this list
     */
    public void addToFront(T element);

    /**
     * Adds the specified element to the back of this list
     *
     * @param element the element to be added to this list
     */
    public void addToRear(T element);

    /**
     * Adds the specified element after the target element
     *
     * @param element the element to be added to this list
     * @param target the element to add after
     * @throws ElementNotFoundException when target element is
     * not in the list
     */
    public void addAfter(T element, T target) throws ElementNotFoundException;
}
