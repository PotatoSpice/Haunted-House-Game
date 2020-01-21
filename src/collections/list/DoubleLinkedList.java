package collections.list;

import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.list.adt.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class DoubleLinkedList<T> implements ListADT<T> {

    /** reference to the queue's first added element (first to get removed) */
    protected DoubleNode<T> head;
    /** reference to the queue's last added element (last to get removed) */
    protected DoubleNode<T> tail;
    /** counter of queue's elements */
    protected int count = 0;

    /** for use in Iterator */
    protected int modCount;

    /**
     * Creates an empty queue with zero nodes.
     */
    public DoubleLinkedList() {
        tail = null;
        head = null;
        count = 0;
        modCount = 0;
    }

    /**
     * Creates a queue starting on a node with the specified element.
     *
     * @param element content for the node's element.
     */
    public DoubleLinkedList(T element) {
        tail = new DoubleNode<>(element);
        head = new DoubleNode<>(element);
        count = 1;
        modCount = 0;
    }

    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        DoubleNode<T> temp = head;
        
        if (size() == 1) {
            // remove both head and tail references
            head = tail = null;
        } else {
            // set the next node as the new head of the list
            head = head.getNext();
            head.setPrevious(null);
        }
        
        count--;
        modCount++;
        return temp.getElement();
    }

    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        DoubleNode<T> temp = tail;

        if (size() == 1) {
            // remove both head and tail references
            head = tail = null;
        } else {
            // set the previous node as the new tail of the list
            tail = tail.getPrevious();
            tail.setNext(null);

        }
        
        count--;
        modCount++;
        return temp.getElement();
    }

    @Override
    public T remove(T element) throws EmptyCollectionException, ElementNotFoundException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        
        if (count == 1) {
            head = tail = null;
            
        } else if (element.equals(head)) {
            head = head.getNext();
            head.setPrevious(null);
            
        } else if (element.equals(tail)) {
            tail = tail.getPrevious();
            tail.setNext(null);
            
        } else {
            // Search for the element node that's being removed
            boolean found = false;
            DoubleNode<T> current = head;
            while (!found && current != null) {
                if (element.equals(current.getElement())) {
                    found = true;
                } else {
                    current = current.getNext();
                }
            }

            if (!found) {
                throw new ElementNotFoundException();
            }
            
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
        }
        
        count--;
        modCount++;
        return element;
    }

    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        // Return the element on the head of the list
        return head.getElement();
    }

    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        // Return the element on the tail of the list
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        if (target.equals(head)) {
            return true;
        } else if (target.equals(tail)) {
            return true;
        } else {
            boolean found = false;
            DoubleNode<T> current = head;
            while (!found && current.getNext() != null) {
                if (target.equals(current.getElement())) {
                    found = true;
                } else {
                    current = current.getNext();
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
        if (!isEmpty()) {
            DoubleNode<T> temp = head;
            for (int i = 0; i < count; i++) {
                s = s + "[PreviousNode: " + ((temp.getPrevious() != null)
                        ? temp.getPrevious().hashCode() : "null")
                        + "; ThisNode: " + temp.hashCode()
                        + "; NextNode: " + ((temp.getNext() != null)
                        ? temp.getNext().hashCode() : "null")
                        + "; ElementContent: " + temp.getElement() + "]\n";
                temp = temp.getNext();
            }
        }
        return s;
    }

    @Override
    public Iterator<T> iterator() {
        return new BasicIterator();
    }

    /**
     * Basic Iterator for this Collection elements.
     *
     * @param <T> type generic variable from the parent class
     */
    private class BasicIterator implements Iterator {

        private DoubleNode<T> previous;
        private DoubleNode<T> current;
        private int expectedModCount;
        private boolean okToRemove;

        public BasicIterator() {
            previous = null;
            current = head;
            expectedModCount = modCount;
            okToRemove = true;
        }

        @Override
        public boolean hasNext() throws ConcurrentModificationException {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (current != tail);
        }

        @Override
        public T next() {
            if (!hasNext()) {
                // exception
                return null;
            }

            okToRemove = true;
            previous = current;
            current = current.getNext();

            return previous.getElement();
        }

        @Override
        public void remove() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (okToRemove) {
                try {
                    DoubleLinkedList.this.remove(previous.getElement());
                } catch (EmptyCollectionException | ElementNotFoundException ex) {
                }

                expectedModCount = modCount;
                okToRemove = false;
            }
        }
    }
}
