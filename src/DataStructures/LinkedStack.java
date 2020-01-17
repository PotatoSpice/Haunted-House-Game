package DataStructures;

import ADT.EmptyCollectionException;
import ADT.StackADT;

public class LinkedStack <T> implements StackADT<T> {

    private LinearNode head;
    private int count=0;

    @Override
    public void push(T element) {

        LinearNode<T> node = new LinearNode<>(element);
        if (this.head == null) {
            this.head= node;
        } else {
            node.setNext(this.head);
            this.head = node;
        }

        count++;
    }


    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("STACK");
        LinearNode<T> node = head;

        if (this.count == 1) {
            this.head = null;

        } else {

            this.head = this.head.getNext();

        }

        count--;
        return node.getNext().getElement();

    }

    @Override
    public T peek() throws EmptyCollectionException {

        if (isEmpty())
            throw new EmptyCollectionException("STACK");

        LinearNode<T> node = head;
        return node.getElement();
    }

    @Override
    public boolean isEmpty() {
       return (count==0);
    }

    @Override
    public int size() {
        return count;
    }
}
