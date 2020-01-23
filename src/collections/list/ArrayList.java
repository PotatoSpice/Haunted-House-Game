package collections.list;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.adt.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class ArrayList<T> implements ListADT<T> {

    /** counter of list elements. */
    protected int count;
    /** array of generic elements to represent the list */
    protected T[] list;
    /** constant to represent the default capacity of the array */
    protected final int DEFAULT_CAPACITY = 100;

    /** for use in Iterator */
    protected int modCount;

    /**
     * Creates an empty list, from an array of Objects, using the default
     * capacity.
     */
    public ArrayList() {
        count = 0;
        modCount = 0;
        list = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates an empty list, from an array of Objects, using the specified
     * capacity.
     *
     * @param initialCapacity represents the specified capacity
     */
    public ArrayList(int initialCapacity) {
        count = 0;
        modCount = 0;
        list = (T[]) (new Object[initialCapacity]);
    }

    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        T temp = list[0];
        // Shift all the elements "backwards". This will replace the first
        // element, which is the one we want to remove
        shiftArrayBackward(0, count - 1);
        
        count--;
        modCount++;
        return temp;
    }

    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        T temp = list[count - 1];
        // Simply turn the last element to null, no shifting needed
        list[count - 1] = null;
        
        count--;
        modCount++;
        return temp;
    }

    @Override
    public T remove(T element) throws EmptyCollectionException, ElementNotFoundException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        T remove;
        int elem;
        // check if element is at the start or end of the list before searching
        if (element.equals(list[0])) {
            remove = list[0];
            elem = 0;
            
        } else if (element.equals(list[count - 1])) {
            remove = list[count - 1];
            elem = count - 1;
            
        } else {
            // search for the element
            boolean found = false;
            elem = 1;
            while (!found && elem < count) {
                if (element.equals(list[elem])) {
                    found = true;
                } else {
                    elem++;
                }
            }
            
            if (!found) {
                throw new ElementNotFoundException();
            }
            
            remove = list[elem];
        }
        
        // shift all the elements "backwards". 
        // This will replace the element we want to remove with the one 
        // that procedes it in the array, null if it's the last
        shiftArrayBackward(elem, count - 1);
        
        count--;
        modCount++;
        return remove;
    }

    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        return list[0];
    }

    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        return list[count - 1];
    }

    @Override
    public boolean contains(T target) throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        if (target.equals(list[0])) {
            return true;
        } else if (target.equals(list[count - 1])) {
            return true;
        } else {
            boolean found = false;
            int i = 0;
            while (!found && i < count) {
                if (target.equals(list[i])) {
                    found = true;
                } else {
                    i++;
                }
            }
            return found;
        }
    }

    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < count; i++) {
            s = s + "[" + list[i].toString() + "]";
        }
        return s;
    }

    /**
     * Increases the size of the array used by the List by DEFAULT_CAPACITY.
     */
    protected void expandCapacity() {
        T[] new_array = (T[]) (new Object[list.length + DEFAULT_CAPACITY]);
        for (int i = 0; i < count; i++) {
            new_array[i] = list[i];
        }
        list = new_array;
    }

    /**
     * Shifts all elements within the specified gap "backwards". This means all
     * elements will each be shifted one position to the "left" of the array.
     *
     * Note that the element on the "start" index will be replaced and the
     * element on the "end" index will be set to 'null'.
     *
     * @param start starting index
     * @param end index of the last element to be shifted
     */
    protected void shiftArrayBackward(int start, int end) throws NullPointerException {
        for (int i = start; i < end; i++) {
            list[i] = list[i + 1];
        }
        list[end] = null;
    }

    @Override
    public Iterator<T> iterator() {
        return new BasicIterator();
    }

    /**
     * Basic Iterator for this Collection elements.
     *
     */
    private class BasicIterator implements Iterator {

        private int current;
        private int expectedModCount;
        private boolean okToRemove;

        public BasicIterator() {
            current = 0;
            expectedModCount = modCount;
            okToRemove = true;
        }

        @Override
        public boolean hasNext() throws ConcurrentModificationException {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (current != count);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                // exception
                return null;
            }

            okToRemove = true;
            ++current;
            return list[current - 1];
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (okToRemove) {
                try {
                    ArrayList.this.remove(list[current - 1]);
                } catch (EmptyCollectionException | ElementNotFoundException ex) {
                }
                --current;

                expectedModCount = modCount;
                okToRemove = false;
            }
        }
    }
}
