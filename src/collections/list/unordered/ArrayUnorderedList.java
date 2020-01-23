package collections.list.unordered;

import collections.list.adt.UnorderedListADT;
import collections.exceptions.ElementNotFoundException;
import collections.list.ArrayList;

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
            shiftArrayForward(0, count - 1);
        }
        
        list[0] = element;
        count++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        if (size() == list.length) {
            expandCapacity();
        }

        list[count] = element;
        count++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) throws ElementNotFoundException {
        if (size() == list.length) {
            expandCapacity();
        }
        
        int temp;
        if (target.equals(list[0])) {
            temp = 0;
            
        } else if (target.equals(list[count - 1])) {
            temp = count - 1;
            
        } else {
            boolean found = false;
            int i = 1; // start after the head of the list
            while (!found && i < count) {
                if (target.equals(list[i])) {
                    found = true;
                } else {
                    i++;
                }
            }

            if (!found) {
                throw new ElementNotFoundException();
            }
            
            temp = i;
        }
        
        // shift all elements "forward" starting on found element's following index
        shiftArrayForward(temp + 1, count - 1);
        list[temp + 1] = element;
        
        count++;
        modCount++;
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
