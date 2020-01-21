package collections.list.ordered;

import collections.list.adt.OrderedListADT;
import collections.list.ArrayList;

public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {

    /**
     * Creates an empty list using the default capacity.
     */
    public ArrayOrderedList() {
        super();
    }

    /**
     * Creates an empty list using the specified capacity.
     *
     * @param initialCapacity represents the specified capacity
     */
    public ArrayOrderedList(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public void add(T element) {
        if (count == list.length) {
            expandCapacity();
        }

        // Iterate while element is "greater" than the elements on the list
        int i = 0;
        while (i < count && ((Comparable) element).compareTo(list[i]) > 0) {
            i++;
        }

        // Shift elements on the list, greater than 'element', "forward"
        for (int j = count; j > i; j--) {
            list[j] = list[j - 1];
        }

        // Add the element to the now open position (after shift being made)
        list[i] = element;
        
        count++;
        modCount++;
    }
}
