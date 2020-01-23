package collections.list.unordered;

import collections.exceptions.ElementNotFoundException;
import collections.list.adt.UnorderedListADT;
import collections.list.DoubleLinkedList;
import collections.list.DoubleNode;

public class DoubleLinkedUnorderedList<T> extends DoubleLinkedList<T> implements UnorderedListADT<T> {

    /**
     * Creates an empty list with zero nodes.
     */
    public DoubleLinkedUnorderedList() {
        super();
    }

    /**
     * Creates a list starting on a node with the specified element.
     *
     * @param element content for the node's element.
     */
    public DoubleLinkedUnorderedList(T element) {
        super(element);
    }

    @Override
    public void addToFront(T element) {
        DoubleNode<T> new_node = new DoubleNode<>(element);

        if (isEmpty()) {
            head = tail = new_node;
        } else {
            head.setPrevious(new_node);
            new_node.setNext(head);
            head = new_node;
        }
        
        count++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        DoubleNode<T> new_node = new DoubleNode<>(element);

        if (isEmpty()) {
            head = tail = new_node;
        } else {
            tail.setNext(new_node);
            new_node.setPrevious(tail);
            tail = new_node;
        }
        
        count++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) throws ElementNotFoundException {
        DoubleNode<T> new_node = new DoubleNode<>(element);
        
        DoubleNode<T> current;
        if (target.equals(head.getElement())) {
            current = head;
            
        } else if (target.equals(tail.getElement())) {
            current = tail;
            
        } else {
            boolean found = false;
            current = head.getNext();
            while (!found && current != null) {
                if (target.equals(current.getElement())) {
                    found = true;
                } else {
                    current = current.getNext();
                }
            }

            if (!found) {
                throw new ElementNotFoundException();
            }          
        }
        
        new_node.setPrevious(current);
        if (!current.equals(tail)) {
            new_node.setNext(current.getNext());
            current.getNext().setPrevious(new_node);

        } else {
            tail = new_node;
        }
        current.setNext(new_node);  
        
        count++;
        modCount++;
    }

}
