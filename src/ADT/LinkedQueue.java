package ADT;

public class LinkedQueue<T> implements  QueueADT<T> {

private LinearNode last, head;
private int count=0;

    @Override
    public void enqueue(T element) {

        LinearNode<T> node = new LinearNode<>(element);
        if (this.last == null) {
            this.head = this.last = node;
        } else {
           last.setNext(node);
           node = this.last;
        }
        count++;
    }

    @Override
    public T dequeue() throws EmptyCollectionException {

        if (isEmpty())
            throw new EmptyCollectionException("STACK");
        LinearNode<T> node = last;

        if (this.count == 1) {
            this.head = null;
            this.last= null;
        } else {
            this.head = this.head.getNext();
        }
        count--;
        return node.getNext().getElement();
    }

    @Override
    public T first() {
        LinearNode node = head;
        return (T) node.getElement();
    }

    @Override
    public boolean isEmpty() {

        return (count == 0);


    }

    @Override
    public int size() {
        return count;
    }
}
