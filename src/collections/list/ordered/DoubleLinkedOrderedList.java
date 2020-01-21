package collections.list.ordered;

import collections.list.adt.ListADT;
import collections.list.adt.OrderedListADT;
import collections.list.DoubleNode;
import collections.list.DoubleLinkedList;
import collections.list.unordered.ArrayUnorderedList;

public class DoubleLinkedOrderedList<T> extends DoubleLinkedList<T> implements OrderedListADT<T> {

    /**
     * Creates an empty queue with zero nodes.
     */
    public DoubleLinkedOrderedList() {
        super();
    }

    /**
     * Creates a queue starting on a node with the specified element.
     *
     * @param element content for the node's element.
     */
    public DoubleLinkedOrderedList(T element) {
        super(element);
    }

    @Override
    public void add(T element) {
        DoubleNode<T> new_node = new DoubleNode<>(element);

        if (isEmpty()) {
            head = tail = new_node;
            
        } else {
            
            if (((Comparable) element).compareTo(head.getElement()) < 0) {
                head.setPrevious(new_node);
                new_node.setNext(head);
                head = new_node;
                
            } else if (((Comparable) element).compareTo(tail.getElement()) > 0) {
                tail.setNext(new_node);
                new_node.setPrevious(tail);
                tail = new_node;
                
            } else {
                DoubleNode<T> current = head.getNext();
                while (current.getNext() != null 
                        && ((Comparable) element).compareTo(current.getElement()) > 0) {
                    current = current.getNext();
                }

                new_node.setNext(current);
                new_node.setPrevious(current.getPrevious());
                current.getPrevious().setNext(new_node);
                current.setPrevious(new_node);
            }
        }
        
        ++count;
        ++modCount;
    }

    /**
     * Invert and return all elements on this ordered list.
     * 
     * @return a list with all elements inverted from this ordered list
     */
    public ListADT<T> invertList() {
        ArrayUnorderedList<T> invert = new ArrayUnorderedList<>(count);

        DoubleNode<T> temp = tail;
        while (temp != null) {
            invert.addToRear(temp.getElement());
            temp = temp.getPrevious();
        }

        return invert;
    }

}
