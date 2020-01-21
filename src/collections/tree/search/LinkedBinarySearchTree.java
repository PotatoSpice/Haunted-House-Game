package collections.tree.search;

import collections.tree.BinaryTreeNode;
import collections.tree.LinkedBinaryTree;
import collections.tree.adt.BinarySearchTreeADT;
import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;

public class LinkedBinarySearchTree<T> extends LinkedBinaryTree<T> implements BinarySearchTreeADT<T> {
    
    /**
     * Creates an empty binary search tree.
     */
    public LinkedBinarySearchTree() {
        super();
    }

    /**
     * Creates a binary search with the specified element as its root.
     *
     * @param element the element that will be the root of the new binary search
     * tree
     */
    public LinkedBinarySearchTree(T element) {
        super(element);
    }

    /**
     * Adds the specified object to the binary search tree in the appropriate
     * position according to its key value. Note that equal elements are added
     * to the right.
     *
     * @param element the element to be added to the binary search tree
     */
    @Override
    public void addElement(T element) {
        BinaryTreeNode<T> temp = new BinaryTreeNode<>(element);
        Comparable<T> comparableElement = (Comparable<T>) element;
        if (isEmpty()) {
            root = temp;
        } else {
            BinaryTreeNode<T> current = root;
            boolean added = false;
            while (!added) {
                if (comparableElement.compareTo(current.element) < 0) {
                    /** go left */
                    if (current.left == null) {
                        current.left = temp;
                        added = true;
                    } else {
                        current = current.left;
                    }
                } else {
                    /** go right */
                    if (current.right == null) {
                        current.right = temp;
                        added = true;
                    } else {
                        current = current.right;
                    }
                }
            }
        }
        count++;
    }

    /**
     * Removes the first element that matches the specified target element from
     * the binary search tree and returns a reference to it. Throws a
     * ElementNotFoundException if the specified target element is not found in
     * the binary search tree.
     *
     * @param targetElement the element being sought in the binary search tree
     * @throws ElementNotFoundException if an element not found exception occurs
     */
    @Override
    public T removeElement(T targetElement) throws ElementNotFoundException, EmptyCollectionException {
        T result = null;
        if (isEmpty()) {
            throw new EmptyCollectionException();
        } else {
            if (((Comparable) targetElement).equals(root.element)) {
                result = root.element;
                root = replacement(root);
                count--;
            } else {
                BinaryTreeNode<T> current, parent = root;
                boolean found = false;
                if (((Comparable) targetElement).compareTo(root.element) < 0) {
                    current = root.left;
                } else {
                    current = root.right;
                }
                while (current != null && !found) {
                    if (targetElement.equals(current.element)) {
                        found = true;
                        count--;
                        result = current.element;
                        if (current == parent.left) {
                            parent.left = replacement(current);
                        } else {
                            parent.right = replacement(current);
                        }
                    } else {
                        parent = current;
                        if (((Comparable) targetElement).compareTo(current.element) < 0) {
                            current = current.left;
                        } else {
                            current = current.right;
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
     * @param node the node to be removeed
     * @return a reference to the replacing node
     */
    protected BinaryTreeNode<T> replacement(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> result;
        if ((node.left == null) && (node.right == null)) {
            result = null;
        } else if ((node.left != null) && (node.right == null)) {
            result = node.left;
        } else if ((node.left == null) && (node.right != null)) {
            result = node.right;
        } else {
            BinaryTreeNode<T> current = node.right;
            BinaryTreeNode<T> parent = node;
            while (current.left != null) {
                parent = current;
                current = current.left;
            }
            if (node.right == current) {
                current.left = node.left;
            } else {
                parent.left = current.right;
                current.right = node.right;
                current.left = node.left;
            }
            result = current;
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
        T result;
        
        if (root.left == null) {
            result = root.element;
            root = root.right;
            
        } else {
            BinaryTreeNode<T> current = root.left, parent = root;
            while (current.left != null) {
                parent = current;
                current = current.left;
            }
            result = current.element;
            parent.left = current.right;
        }
        
        count--;
        return result;
    }

    @Override
    public T removeMax() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        T result;
        
        if (root.right == null) {
            result = root.element;
            root = root.left;
            
        } else {
            BinaryTreeNode<T> current = root.right, parent = root;
            while (current.right != null) {
                parent = current;
                current = current.right;
            }
            result = current.element;
            parent.right = current.left;
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
        BinaryTreeNode<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        result = current.element;
        
        return result;
    }

    @Override
    public T findMax() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }

        T result;
        // Get the rightmost element in the tree
        BinaryTreeNode<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        result = current.element;

        return result;
    }

}
