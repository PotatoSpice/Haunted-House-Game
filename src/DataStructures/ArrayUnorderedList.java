/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;
import ADT.ArrayList;
import ADT.ElementNotFoundException;
import ADT.UnorderedListADT;

public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    /**
     * Creates an empty list using the default capacity.
     */
    public ArrayUnorderedList() {
        super();
    }

    /**
     * Creates an empty list using the specified capacity.
     *
     * @param initialCapacity represents the specified capacity
     */
    public ArrayUnorderedList(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public void addToFront(T element) {
        if (size() == list.length) {
            expandCapacity();
        }

        // When list is not empty, shift all elements "forward" before adding to front
        if (!isEmpty()) {
            shiftArrayForward(0, size() - 1);
        }
        // Add the element to the front of the list
        list[0] = element;
        // Increment number of list elements
        counter++;
    }

    @Override
    public void addToRear(T element) {
        if (size() == list.length) {
            expandCapacity();
        }

        // Add the element to the rear of the list (no need for shifting)
        list[size()] = element;
        // Increment number of list elements
        counter++;
    }

    @Override
    public void addAfter(T element, T target) throws ElementNotFoundException {
        if (size() == list.length) {
            expandCapacity();
        }

        // Search for the target element
        boolean found = false;
        int i = 0;
        while (!found && i < size()) {
            if (target.equals(list[i])) {
                found = true;
            } else {
                i++;
            }
        }

        // If the element is not found, throw an exception
        if (!found) {
            throw new ElementNotFoundException();
        }

        // Shift all elements "forward" starting on the found element index
        shiftArrayForward(i + 1, size() - 1);
        // Add the element to the now empty position
        list[i + 1] = element;
        // Increment number of list elements
        counter++;
    }

    /**
     * Increases the size of the array used by the List by DEFAULT_CAPACITY.
     */
    private void expandCapacity() {
        T[] new_array = (T[]) (new Object[super.DEFAULT_CAPACITY]);
        for (int i = 0; i < size(); i++) {
            new_array[i] = list[i];
        }
        list = new_array;
    }

    /**
     * Shifts all elements within the specified gap "forwards". This means all
     * elements will each be shifted one position to the "right" of the array.
     *
     * Note that the element on the "end" index will be replaced and the element
     * on the "start" index will be set to 'null'.
     *
     * @param start starting index
     * @param end index of the last element to be shifted
     */
    private void shiftArrayForward(int start, int end) throws NullPointerException {
        for (int i = end + 1; i > start; i--) {
            list[i] = list[i - 1];
        }
        list[start] = null;
    }
}
