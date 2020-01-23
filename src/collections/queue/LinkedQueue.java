package collections.queue;

import collections.exceptions.EmptyCollectionException;
import collections.queue.adt.QueueADT;

public class LinkedQueue<T> implements QueueADT<T> {

    /** reference to the queue's first added element (first to get removed) */
    private LinearNode<T> front;
    /** reference to the queue's last added element (last to get removed) */
    private LinearNode<T> rear;
    /** counter of queue's elements */
    private int count = 0;

    /**
     * Creates an empty queue with zero nodes.
     */
    public LinkedQueue() {
        rear = null;
        front = null;
        count = 0;
    }

    /**
     * Creates a queue starting on a node with the specified element.
     *
     * @param element content for the node's element.
     */
    public LinkedQueue(T element) {
        rear = new LinearNode<>(element);
        front = new LinearNode<>(element);
        count = 1;
    }

    /** 
     * Adds one element to the rear of this queue.
     *
     * @param element the element to be added to the rear of this queue
     */
    @Override
    public void enqueue(T element) {
        LinearNode<T> new_elem = new LinearNode<>(element);
        if (isEmpty()) {
            front = new_elem;
        } else {
            rear.setNext(new_elem);
        }
        rear = new_elem;
        count++;
    }

    /** 
     * Removes and returns the element at the front of this queue.
     *
     * @return the element at the front of this queue
     * @throws collections.exceptions.EmptyCollectionException
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        LinearNode<T> temp = front;
        front = front.getNext();
        count--;
        return temp.getElement();
    }

    /** 
     * Returns without removing the element at the front of this queue.
     *
     * @return the first element in this queue
     * @throws collections.exceptions.EmptyCollectionException
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        return front.getElement();
    }

    /** 
     * Returns true if this queue contains no elements.
     *
     * @return true if this queue is empty
     */
    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    /** 
     * Returns the number of elements in this queue.
     *
     * @return the integer representation of the size of this queue
     */
    @Override
    public int size() {
        return count;
    }

    /** 
     * Returns a string representation of this queue.
     *
     * @return the string representation of this queue
     */
    @Override
    public String toString() {
        String s = "";
        if (!isEmpty()) {
            LinearNode<T> temp = front;
            while (temp.getNext() != null) {
                s = s + "[ThisNode: " + temp.hashCode()
                        + ";NextNode: " + temp.getNext().hashCode()
                        + "; ElementContent: " + temp.getElement() + "]\n";
                temp = temp.getNext();
            }
            s = s + "[ThisNode: " + temp.hashCode()
                    + "; NextNode: null"
                    + "; ElementContent: " + temp.getElement() + "]";
        }
        return s;
    }
}
