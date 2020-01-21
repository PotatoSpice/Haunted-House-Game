package collections.tree;

import java.util.Iterator;
import collections.tree.adt.BinaryTreeADT;
import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.queue.ArrayQueue;
import collections.list.unordered.ArrayUnorderedList;

public class ArrayBinaryTree<T> implements BinaryTreeADT<T> {

    protected int counter;
    protected T[] tree;
    private final int DEFAULT_CAPACITY = 50;

    /**
     * Creates an empty binary tree.
     */
    public ArrayBinaryTree() {
        counter = 0;
        tree = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates a binary tree with the specified element as its root.
     *
     * @param element the element which will become the root of the new tree
     */
    public ArrayBinaryTree(T element) {
        counter = 1;
        tree = (T[]) (new Object[DEFAULT_CAPACITY]);
        tree[0] = element;
    }

    @Override
    public T find(T targetElement) throws ElementNotFoundException, EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        T target = findAgain(targetElement, 0);
        if (target == null) {
            throw new ElementNotFoundException("binary tree");
        }
        return target;
    }

    @Override
    public boolean contains(T targetElement) throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        return (findElement(targetElement) != null);
    }
    
    /**
     * Returns a reference to the specified target element if it is found in
     * this binary tree.
     *
     * @param targetElement the element being sought in this tree
     * @param next the element to begin searching from
     */
    private T findAgain(T targetElement, int next) {
        if (tree[next] == null) {
            return null;
        }
        if (tree[next].equals(targetElement)) {
            return tree[next];
        }
        T temp = findAgain(targetElement, next * 2 + 1);
        if (temp == null) {
            temp = findAgain(targetElement, (next + 1) * 2);
        }
        return temp;
    }

    /**
     * Returns a reference to the specified target element if it is found in
     * this binary tree. (Linear Search)
     *
     * @param targetElement the element being sought in this tree
     */
    private T findElement(T targetElement) {
        /*
        NOTA: 
        Este método serve só de exemplo em como se poderia pesquisar linearmente 
        pelo array. É pouco eficiente, tendo em conta a abordagem utilizada para 
        a organização dos nós no array. Isto é, poderão existir posições "vazias".
        */
        T temp = null;
        boolean found = false;
        for (int i = 0; i < counter && !found; i++) {
            if (tree[i] != null && targetElement.equals(tree[i])) {
                temp = tree[i];
                found = true;
            }
        }
        return temp;
    }

    @Override
    public T getRoot() {
        return tree[0];
    }

    @Override
    public boolean isEmpty() {
        return (counter == 0);
    }

    @Override
    public int size() {
        return counter;
    }

    @Override
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<>();
        inorder(0, templist);
        return templist.iterator();
    }
    
    /**
     * Performs a recursive inorder traversal.
     *
     * @param node the node used in the traversal
     * @param templist the temporary list used in the traversal
     */
    protected void inorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length) {
            if (tree[node] != null) {
                inorder(node * 2 + 1, templist);
                templist.addToRear(tree[node]);
                inorder((node + 1) * 2, templist);
            }
        }
    }

    @Override
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<>();
        preorder(0, templist);
        return templist.iterator();
    }
    
    /**
     * Performs a recursive preorder traversal.
     *
     * @param node the node used in the traversal
     * @param templist the temporary list used in the traversal
     */
    protected void preorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length) {
            if (tree[node] != null) {
                templist.addToRear(tree[node]);
                preorder(node * 2 + 1, templist);
                preorder((node + 1) * 2, templist);
            }
        }
    }

    @Override
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<>();
        postorder(0, templist);
        return templist.iterator();
    }
    
    /**
     * Performs a recursive postorder traversal.
     *
     * @param node the node used in the traversal
     * @param templist the temporary list used in the traversal
     */
    protected void postorder(int node, ArrayUnorderedList<T> templist) {
        if (node < tree.length) {
            if (tree[node] != null) {
                postorder(node * 2 + 1, templist);
                postorder((node + 1) * 2, templist);
                templist.addToRear(tree[node]);
            }
        }
    }

    @Override
    public Iterator<T> iteratorLevelOrder() {
        ArrayQueue<Integer> nodes = new ArrayQueue<>();
        ArrayUnorderedList<T> results = new ArrayUnorderedList<>();
        if (!isEmpty()) {
            nodes.enqueue(0);
            Integer temp;
            try {
                while (!nodes.isEmpty()) {
                    // Queue will never be empty in the next line, so no need
                    // to check if 'temp' would be 'null'!
                    temp = nodes.dequeue();
                    results.addToRear(tree[temp]);
                    int left = temp * 2 + 1, right = (temp + 1) * 2;
                    // Check if 'temp' has children, to add to the queue
                    if (tree[left] != null)
                        nodes.enqueue(left);
                    if (tree[right] != null)
                        nodes.enqueue(right);
                }
            } catch (EmptyCollectionException exc) {
                // 'nodes.dequeue()' will never throw this exception.
                // But since the compiler doesn't know that, we still need to catch it.
            }
        }
        return results.iterator();
    }
    
    @Override
    public String toString() {
        String s = "";
        if (!isEmpty()) {
            ArrayQueue<Integer> nodes = new ArrayQueue<>();
            nodes.enqueue(0);
            Integer temp;
            try {
                while (!nodes.isEmpty()) {
                    // Queue will never be empty in the next line, so no need
                    // to check if 'temp' would be 'null'!
                    temp = nodes.dequeue();
                    int left = temp * 2 + 1, right = (temp + 1) * 2;
                    s = s + "[ElementIndex: " + temp + "; Element: " + tree[temp]
                            + "\n\tLeftChildIndex: " + left 
                            + "; LeftChild: " + ((tree[left] != null)
                                    ? tree[left] : "null")
                            + "\n\tRightChildIndex: " + right
                            + "; RightChild: " + ((tree[right] != null)
                                    ? tree[right] : "null") + "]\n";
                    // Check if 'temp' has children, to add to the queue
                    if (tree[left] != null)
                        nodes.enqueue(left);
                    if (tree[right] != null)
                        nodes.enqueue(right);
                }
            } catch (EmptyCollectionException exc) {
                // 'nodes.dequeue()' will never throw this exception.
                // But since the compiler doesn't know that, we still need to catch it.
            }
        }
        return s;
    }
}
