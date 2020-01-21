package collections.stack;

import collections.exceptions.EmptyCollectionException;
import collections.stack.adt.StackADT;

public class LinkedStack<T> implements StackADT<T> {

    /** reference to the stack's last pushed node */
    private LinearNode<T> top;
    /** number of nodes inside the stack */
    private int count;

    /**
     * Creates an empty stack with zero nodes.
     */
    public LinkedStack() {
        top = null;
        count = 0;
    }

    /**
     * Creates a stack starting on a node with the specified element.
     *
     * @param element content for the node's element.
     */
    public LinkedStack(T element) {
        top = new LinearNode<>(element);
        count = 1;
    }

    /**
     * Adds the specified element to the top of this stack.
     *
     * @param element generic element to be pushed onto stack
     */
    @Override
    public void push(T element) {
        LinearNode<T> temp = new LinearNode<>(element);
        
        // Note: if it does not exist simply stores a null reference.
        temp.setNext(top);
        
        top = temp;
        count++;
    }

    /**
     * Removes the element at the top of this stack and returns a reference to
     * it. Throws an EmptyCollectionException if the stack is empty.
     *
     * @return T element removed from top of stack
     * @throws EmptyCollectionException if a pop is attempted on empty stack
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        LinearNode<T> temp = top;
        
        top = top.getNext();
        count--;
        
        return temp.getElement();
    }

    /**
     * Returns a reference to the element at the top of this stack. The element
     * is not removed from the stack. Throws an EmptyCollectionException if the
     * stack is empty.
     *
     * @return T element on top of stack
     * @throws EmptyCollectionException if a peek is attempted on empty stack
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        
        return top.getElement();
    }

    /**
     * Tests if the stack has any element.
     *
     * @return true if stack is empty, false if not.
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return number of elements in the stack.
     */
    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString() {
        String s = "LinkedListNodes: ";
        if (!isEmpty()) {
            LinearNode<T> temp = top;
            while (temp.getNext() != null) {
                s = s + "[NextNode: " + temp.getNext().hashCode()
                        + "; ElementContent: " + temp.getElement() + "]";
                temp = temp.getNext();
            }
            s = s + "[NextNode: null"
                    + "; ElementContent: " + temp.getElement() + "]";
        }
        return s;
    }
}
