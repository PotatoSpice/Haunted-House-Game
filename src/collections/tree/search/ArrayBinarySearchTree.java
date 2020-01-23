package collections.tree.search;

import collections.tree.ArrayBinaryTree;
import collections.tree.adt.BinarySearchTreeADT;
import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;

public class ArrayBinarySearchTree<T> extends ArrayBinaryTree<T> implements BinarySearchTreeADT<T> {

    protected int height;
    /** index of the last added non null tree node */
    protected int maxIndex;

    /**
     * Creates an empty binary search tree.
     */
    public ArrayBinarySearchTree() {
        super();
        height = 0;
        maxIndex = -1;
    }

    /**
     * Creates a binary search with the specified element as its root
     *
     * @param element the element that will become the root of the new tree
     */
    public ArrayBinarySearchTree(T element) {
        super(element);
        height = 1;
        maxIndex = 0;
    }

    /**
     * Adds the specified object to this binary search tree in the appropriate
     * position according to its key value. Note that equal elements are added
     * to the right.
     *
     * Also note that the index of the left child of the current index can be
     * found by doubling the current index and adding 1. Finding the index of
     * the right child can be calculated by doubling the current index and
     * adding 2.
     *
     * @param element the element to be added to the search tree
     */
    @Override
    public void addElement(T element) {
        // (maxIndex * 2 + 2) -> índice do filho à direita do último nó
        // \anterior (+ 1) -> + 1 porque o array começa em índice 0
        // no fundo, expande-se quando o tamanho do array é menor que o índice 
        // do último filho (da direita) do último nó não nulo
        if (tree.length < maxIndex * 2 + 3) {
            expandCapacity();
        }
        Comparable<T> comparableElement = (Comparable<T>) element;
        if (isEmpty()) {
            tree[0] = element;
            maxIndex = 0;
        } else {
            boolean added = false;
            int currentIndex = 0;
            while (!added) {
                if (comparableElement.compareTo((tree[currentIndex])) < 0) {
                    /** go left */
                    if (tree[currentIndex * 2 + 1] == null) {
                        tree[currentIndex * 2 + 1] = element;
                        added = true;
                        if (currentIndex * 2 + 1 > maxIndex) {
                            maxIndex = currentIndex * 2 + 1;
                        }
                    } else {
                        currentIndex = currentIndex * 2 + 1;
                    }
                } else {
                    /** go right */
                    if (tree[currentIndex * 2 + 2] == null) {
                        tree[currentIndex * 2 + 2] = element;
                        added = true;
                        if (currentIndex * 2 + 2 > maxIndex) {
                            maxIndex = currentIndex * 2 + 2;
                        }
                    } else {
                        currentIndex = currentIndex * 2 + 2;
                    }
                }
            }
        }
        height = (int) (Math.log(maxIndex + 1) / Math.log(2)) + 1;
        count++;
    }

    @Override
    public T removeElement(T targetElement) throws EmptyCollectionException, ElementNotFoundException {
        T result = null;
        if (isEmpty()) {
            throw new EmptyCollectionException();
        } else {
            if (((Comparable) targetElement).equals(tree[0])) {
                result = tree[0];
                tree[0] = replacement(0);
                count--;
            } else {
                int currentIndex, parentIndex = 0;
                boolean found = false;
                if (((Comparable) targetElement).compareTo(tree[0]) < 0) {
                    currentIndex = 1; // 0 * 2 + 1
                } else {
                    currentIndex = 2; // 0 * 2 + 2
                }
                while (tree[currentIndex] != null && !found) {
                    if (targetElement.equals(tree[currentIndex])) {
                        found = true;
                        count--;
                        result = tree[currentIndex];
                        if (tree[currentIndex].equals(tree[parentIndex * 2 + 1])) {
                            tree[parentIndex * 2 + 1] = replacement(currentIndex);
                        } else {
                            tree[parentIndex * 2 + 2] = replacement(currentIndex);
                        }
                    } else {
                        parentIndex = currentIndex;
                        if (((Comparable) targetElement).compareTo(tree[currentIndex]) < 0) {
                            currentIndex = currentIndex * 2 + 1;
                        } else {
                            currentIndex = currentIndex * 2 + 2;
                        }
                    }
                } //while            
                if (!found) {
                    throw new ElementNotFoundException("binary search tree");
                }
            }
        } // end outer if      
        return result;
    }

    /**
     * Returns a reference to a node that will replace the one specified for
     * removal. In the case where the removed node has two children, the inorder
     * successor is used as its replacement.
     *
     * @param nodeIndex index of the node to be removed
     * @return the replacing node element
     */
    protected T replacement(int nodeIndex) {
        T result;
        if ((tree[nodeIndex * 2 + 1] == null) && (tree[nodeIndex * 2 + 2] == null)) {
            result = null;
        } else if ((tree[nodeIndex * 2 + 1] != null) && (tree[nodeIndex * 2 + 2] == null)) {
            result = tree[nodeIndex * 2 + 1];
            tree[nodeIndex * 2 + 1] = null;
        } else if ((tree[nodeIndex * 2 + 1] == null) && (tree[nodeIndex * 2 + 2] != null)) {
            result = tree[nodeIndex * 2 + 2];
            tree[nodeIndex * 2 + 2] = null;
        } else {
            int current = nodeIndex * 2 + 2;
            int parent = nodeIndex;
            while (tree[current * 2 + 1] != null) {
                parent = current;
                current = current * 2 + 1;
            }
            if (tree[nodeIndex * 2 + 2] == tree[current]) {
                tree[current * 2 + 1] = tree[nodeIndex * 2 + 1];
            } else {
                tree[parent * 2 + 1] = tree[current * 2 + 2];
                tree[current * 2 + 2] = tree[nodeIndex * 2 + 2];
                tree[current * 2 + 1] = tree[nodeIndex * 2 + 1];
            }
            result = tree[current];
            tree[current] = null;
        }
        return result;
    }

    @Override
    public void removeAllOccurrences(T targetElement) 
            throws ElementNotFoundException, EmptyCollectionException {
        removeElement(targetElement); // this first call tries the exceptions
        
        while (contains(targetElement)) {
            // Both this calls (contains and remove) will never throw exceptions.
            removeElement(targetElement);
        }
    }

    @Override
    public T removeMin() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        
        // NOTE:
        // root.left == 0 * 2 + 1
        // root.right == 0 * 2 + 2
        T result;
        if (tree[0 * 2 + 1] == null) {
            result = tree[0];
            tree[0] = tree[0 * 2 + 2];
            
        } else {
            int currentIndex = 0 * 2 + 1, parentIndex = 0;
            while (tree[currentIndex * 2 + 1] != null) {
                parentIndex = currentIndex;
                currentIndex = currentIndex * 2 + 1;
            }
            result = tree[currentIndex];
            tree[parentIndex * 2 + 1] = tree[currentIndex * 2 + 2];
        }
        
        count--;
        return result;
    }

    @Override
    public T removeMax() throws EmptyCollectionException {
        // NOTE:
        // root.left == 0 * 2 + 1
        // root.right == 0 * 2 + 2
        T result;
        if (tree[0 * 2 + 2] == null) {
            result = tree[0];
            tree[0] = tree[0 * 2 + 1];
            
        } else {
            int currentIndex = 0 * 2 + 2, parentIndex = 0;
            while (tree[currentIndex * 2 + 2] != null) {
                parentIndex = currentIndex;
                currentIndex = currentIndex * 2 + 2;
            }
            result = tree[currentIndex];
            tree[parentIndex * 2 + 2] = tree[currentIndex * 2 + 1];
        }
        
        count--;
        return result;
    }

    @Override
    public T findMin() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        T result;
        // Get the leftmost element in the tree
        int current = 0;
        while (tree[current * 2 + 1] != null) {
            current = current * 2 + 1;
        }
        result = tree[current];

        return result;
    }

    @Override
    public T findMax() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        T result;
        // Get the rightmost element in the tree
        int current = 0;
        while (tree[current * 2 + 2] != null) {
            current = current * 2 + 2;
        }
        result = tree[current];

        return result;
    }
}
