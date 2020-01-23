package collections.queue;

import collections.exceptions.EmptyCollectionException;
import collections.queue.adt.QueueADT;

public class ArrayQueue<T> implements QueueADT<T> {

    /** array index of the queue's first added element (first to get removed) */
    private int front;
    /** array index of the queue's last added element (last to get removed) */
    private int rear;
    /** counter of queue's elements */
    private int count;
    /** array of generic elements to represent the queue */
    private T[] queue;
    /** constant to represent the default capacity of the array */
    private final int DEFAULT_CAPACITY = 100;

    /**
     * Creates an empty queue using the default capacity.
     */
    public ArrayQueue() {
        front = 0;
        rear = 0;
        count = 0;
        queue = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates an empty queue using the specified capacity.
     *
     * @param initialCapacity represents the specified capacity
     */
    public ArrayQueue(int initialCapacity) {
        front = 0;
        rear = 0;
        count = 0;
        queue = (T[]) (new Object[initialCapacity]);
    }

    /** 
     * Adds one element to the rear of this queue.
     *
     * @param element the element to be added to the rear of this queue
     */
    @Override
    public void enqueue(T element) {
        if (size() == queue.length) {
            expandCapacity();
        }
        
        if (isEmpty()) {
            // queue is empty, define the new element as the front
            queue[front] = element;
        }
        queue[rear] = element;
        // calculate the next index for the queue's rear end
        rear = (rear + 1) % queue.length;
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
        T temp = queue[front];
        
        queue[front] = null;
        front++;
        count--;
        
        return temp;
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
        return queue[front];
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

            // - Variável 'j' corresponde à iteração sobre todos os elementos;
            // - Variável 'i' itera sobre todos os elementos existentes na queue 
            // anterior, sendo um array circular (i = (i + 1) % queue.length)
            for (int j = 0, i = front; j < count; j++, i = (i + 1) % queue.length) {
                s = s + "[" + queue[i].toString() + ",pos: " + i + "]";
            }

        }
        return s;
    }

    /**
     * Increases the size of the array used by the Queue by DEFAULT_CAPACITY.
     */
    private void expandCapacity() {
        // Instanciate a new array to work as the new queue
        T[] new_queue = (T[]) (new Object[queue.length + DEFAULT_CAPACITY]);

        for (int j = 0, i = front; j < count; j++, i = (i + 1) % queue.length) {
            new_queue[j] = queue[i];
        }

        // Restart front and rear indexes and define the new array as the queue
        front = 0;
        rear = count;
        queue = new_queue;
    }
}
