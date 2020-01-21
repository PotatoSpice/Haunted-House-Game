package collections.list;

/**
 * DoubleNode represents a node in a doubly linked list.
 * @param <T> type for the element stored in the node
 */
public class DoubleNode<T> {

    private DoubleNode<T> next;
    private T element;
    private DoubleNode<T> previous;

    /**
     * Creates an empty node.
     */
    public DoubleNode() {
        next = null;
        element = null;
        previous = null;
    }

    /**
     * Creates a node storing the specified element.
     *
     * @param elem the element to be stored into the new node
     */
    public DoubleNode(T elem) {
        next = null;
        element = elem;
        previous = null;
    }
    
    /**
     * Sets the node that follows this one.
     *
     * @param node the node to be set as the one to follow the current one
     */
    public void setNext(DoubleNode<T> node) {
        next = node;
    }

    /**
     * Returns the node that follows this one.
     *
     * @return the node that follows the current one
     */
    public DoubleNode<T> getNext() {
        return next;
    }

    /**
     * Sets the node that precedes this one.
     *
     * @param node the node to be set as the one to precede the current one
     */
    public void setPrevious(DoubleNode<T> node) {
        previous = node;
    }
    
    /**
     * Returns the node that precedes this one.
     *
     * @return the node that precedes the current one
     */
    public DoubleNode<T> getPrevious() {
        return previous;
    }

    /**
     * Returns the element stored in this node.
     *
     * @return the element stored in this node
     */
    public T getElement() {
        return element;
    }

    /**
     * Sets the element stored in this node.
     *
     * @param elem the element to be stored in this node
     */
    public void setElement(T elem) {
        element = elem;
    }
}
