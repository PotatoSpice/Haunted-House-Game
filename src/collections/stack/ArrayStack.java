package collections.stack;

import collections.exceptions.EmptyCollectionException;
import collections.stack.adt.StackADT;

public class ArrayStack<T> implements StackADT<T> {

    /** constant to represent the default capacity of the array */
    private final int DEFAULT_CAPACITY = 100;
    /** int that represents the next available position in the array as well as the number of elements */
    private int top;
    /** array of generic elements to represent the stack */
    private T[] stack;

    /**
     * Creates an empty stack using the default capacity.
     */
    public ArrayStack() {
        top = 0;
        stack = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates an empty stack using the specified capacity.
     *
     * @param initialCapacity represents the specified capacity
     */
    public ArrayStack(int initialCapacity) {
        top = 0;
        stack = (T[]) (new Object[initialCapacity]);
    }

    /**
     * Adds the specified element to the top of this stack, expanding the
     * capacity of the stack array if necessary.
     *
     * @param element generic element to be pushed onto stack
     */
    @Override
    public void push(T element) {
        if (size() == stack.length) {
            expandCapacity();
        }
        stack[top] = element;
        top++;
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
            throw new EmptyCollectionException("Stack");
        }
        top--;
        T result = stack[top];
        stack[top] = null;
        return result;
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
            throw new EmptyCollectionException("Stack");
        }
        return stack[top - 1];
    }

    /**
     * Tests if the stack has any element.
     *
     * @return true if stack is empty, false if not.
     */
    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return number of elements in the stack.
     */
    @Override
    public int size() {
        return top;
    }

    @Override
    public String toString() {
        String s = "ArrayStackElements: ";
        for (int i = 0; i < top; i++) {
            s = s + "[" + this.stack[i].toString() + "]";
        }
        return s;
    }

    /**
     * Increases the size of the array used by the Stack by DEFAULT_CAPACITY.
     */
    private void expandCapacity() {
        T[] new_stack = (T[]) (new Object[this.stack.length + DEFAULT_CAPACITY]);
        for (int i = 0; i < top; i++) {
            new_stack[i] = this.stack[i];
        }
        this.stack = new_stack;
    }
}
