package ADT;

import java.util.Iterator;

public class ArrayList<T> implements ListADT<T> {

    /** counter of list elements. Note: this variable will be used to reference
     * the index of the last element within the array in below methods */
    protected int counter;
    /** array of generic elements to represent the list */
    protected T[] list;
    /** constant to represent the default capacity of the array */
    protected final int DEFAULT_CAPACITY = 100;

    /**
     * Creates an empty list using the default capacity.
     */
    public ArrayList() {
        counter = 0;
        list = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates an empty list using the specified capacity.
     *
     * @param initialCapacity represents the specified capacity
     */
    public ArrayList(int initialCapacity) {
        counter = 0;
        list = (T[]) (new Object[initialCapacity]);
    }

    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        // Save the element that's being removed
        T temp = list[0];
        // Shift all the elements "backwards". This will replace the first
        // element, which is the one we want to remove
        shiftArrayBackward(0, size() - 1);
        // Decrease the size of the array
        counter--;
        return temp;
    }

    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        // Save the element that's being removed
        T temp = list[size() - 1];
        // Simply turn the last element to null, no shifting needed
        list[size() - 1] = null;
        // Decrease the size of the array
        counter--;
        return temp;
    }

    @Override
    public T remove(T element) throws EmptyCollectionException, ElementNotFoundException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        // Search for the element that's being removed
        boolean found = false;
        int i = 0;
        while (!found && i < size()) {
            if (element.equals(list[i])) {
                found = true;
            } else {
                i++;
            }
        }

        // If the element is not found, throw an exception
        if (!found) {
            throw new ElementNotFoundException();
        }

        // If the element is found, save it
        T temp = list[i];
        // Shift all the elements "backwards". This will replace the element we
        // want to remove with the one that procedes it in the array
        shiftArrayBackward(i, size() - 1);
        // Decrease the size of the array
        counter--;
        return temp;
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

        return list[size() - 1];
    }

    @Override
    public boolean contains(T target) throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        // Search for the element
        boolean found = false;
        int i = 0;
        while (!found && i < size()) {
            if (target.equals(list[i])) {
                found = true;
            } else {
                i++;
            }
        }
        return found;
    }

    @Override
    public boolean isEmpty() {
        return (counter == 0);
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public Iterator<T> iterator(){

        try {
            return new ListIterator<T>(this);
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }finally{
            return  null;
        }

    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < size(); i++) {
            s = s + "[" + list[i].toString() + "]";
        }
        return s;
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

    class ListIterator<T> implements Iterator<T> {
        Node<T> current;

        // initialize pointer to head of the list for iteration
        public ListIterator(ArrayList<T> list) throws EmptyCollectionException {
            current = (Node<T>) list.first();
        }

        // returns false if next element does not exist
        public boolean hasNext()
        {
            return current != null;
        }

        // return current data and update pointer
        public T next()
        {
            T data = current.getData();
            current = current.getNext();
            return data;
        }

        public void remove()
        {



        }
    }

    // Constituent Node of Linked List
    class Node<T> {
        T data;
        Node<T> next;
        public Node(T data, Node<T> next)
        {
            this.data = data;
            this.next = next;
        }

        // Setter getter methods for Data and Next Pointer
        public void setData(T data)
        {
            this.data = data;
        }

        public void setNext(Node<T> next)
        {
            this.next = next;
        }

        public T getData()
        {
            return data;
        }

        public Node<T> getNext()
        {
            return next;
        }
    }
}
