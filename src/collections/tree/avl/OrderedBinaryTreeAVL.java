package collections.tree.avl;

import collections.tree.LinkedBinaryTree;
import collections.tree.adt.BinarySearchTreeADT;
import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;

public class OrderedBinaryTreeAVL<T> extends LinkedBinaryTree<T> implements BinarySearchTreeADT<T> {

    /**
     * Creates an empty AVL binary search tree.
     */
    public OrderedBinaryTreeAVL() {
        super();
    }
    
    /**
     * Creates an AVL binary search with the specified element as its root.
     *
     * @param element the element that will be the root of the new binary search
     * tree
     */
    public OrderedBinaryTreeAVL(T element) {
        super(element);
    }

    private int height(AVLBinaryTreeNode<T> node) {  
        return (node == null ? -1 : node.height);
    }
    
    private int balance(AVLBinaryTreeNode<T> node){ 
        return (node.left.height - node.right.height);
    }
    
    private int maxHeight(int height1, int height2) { 
        return (height1 > height2 ? height1 : height2); 
    }

    private void updateBalanceandHeight(AVLBinaryTreeNode<T> node) {
        node.height = maxHeight(node.left.height, node.right.height) + 1;
        node.balance = balance(node);
    }

    private AVLBinaryTreeNode<T> rotateWithLeftChild(AVLBinaryTreeNode<T> k2) {
        AVLBinaryTreeNode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.left = k2;
        updateBalanceandHeight(k2);
        updateBalanceandHeight(k1);
        return k1;
    }

    private AVLBinaryTreeNode<T> rotateWithRightChild(AVLBinaryTreeNode<T> k1) {
        AVLBinaryTreeNode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.right = k1;
        updateBalanceandHeight(k2);
        updateBalanceandHeight(k1);
        return k1;
    }

    private AVLBinaryTreeNode<T> rebalance(AVLBinaryTreeNode<T> node){
        AVLBinaryTreeNode<T> nnode = null;
        if (node.balance == 2)
            if (node.left.balance == 1)
                nnode = rotateWithRightChild(node);
            else {
                node.left = rotateWithLeftChild(node);
                nnode = rotateWithRightChild(node);
            } 
        else if (node.balance == -2)
            if (node.right.balance == -1)
                nnode = rotateWithLeftChild(node);
            else {
                node.right = rotateWithRightChild(node.right);
                nnode = rotateWithLeftChild(node);
            }
        
        return nnode;
    }

    private AVLBinaryTreeNode<T> doubleRightChildRotation(AVLBinaryTreeNode<T> k3){
        k3.right = rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }

    private AVLBinaryTreeNode<T> doubleLeftChildRotation(AVLBinaryTreeNode<T> k3){
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    @Override
    public void addElement(T element) {
        AVLBinaryTreeNode<T> temp = new AVLBinaryTreeNode<>(element);
        Comparable<T> comparableElement = (Comparable<T>) element;
        AVLBinaryTreeNode<T> current = null;
        if (isEmpty())
            this.root = temp;
        else {
            current = (AVLBinaryTreeNode<T>) this.root;
            boolean added = false;
            while (!added) {
                if (comparableElement.compareTo(current.element) < 0) {
                    if (current.left == null) {
                        current.left = temp;
                        added = true;
                    } else
                        current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = temp;
                        added = true;
                    } else
                        current = current.right;
                }
            }
            current = fixTreeNodes(current);
        }
        current.height = maxHeight(
                height(current.left), height(current.right) ) + 1;
        count++;
    }

    private AVLBinaryTreeNode<T> fixTreeNodes(AVLBinaryTreeNode<T> node){
        updateBalanceandHeight(node);
        if(node.balance == 2 || node.balance == -2)
            node = rebalance(node);
        return node;
    }

    @Override
    public T removeElement(T targetElement) throws ElementNotFoundException {
        // TODO
        return null;
    }
    
    private AVLBinaryTreeNode<T> replacement(AVLBinaryTreeNode<T> node) {
        AVLBinaryTreeNode<T> result;
        if ((node.left == null) && (node.right == null))
            result = null;
        else if ((node.left != null) && (node.right == null))
            result = node.left;
        else if ((node.left == null) && (node.right != null))
            result = node.right;
        else {
            AVLBinaryTreeNode<T> current = node.right;
            AVLBinaryTreeNode<T> parent = node;
            while (current.left != null) {
                parent = current;
                current = current.left;
            }
            if (node.right == current)
                current.left = node.left;
            else {
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
            AVLBinaryTreeNode<T> current = (AVLBinaryTreeNode<T>) root.left, 
                    parent = (AVLBinaryTreeNode<T>) root;
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
            AVLBinaryTreeNode<T> current = (AVLBinaryTreeNode<T>) root.right, 
                    parent = (AVLBinaryTreeNode<T>) root;
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
        AVLBinaryTreeNode<T> current = (AVLBinaryTreeNode<T>) root;
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
        AVLBinaryTreeNode<T> current = (AVLBinaryTreeNode<T>) root;
        while (current.right != null) {
            current = current.right;
        }
        result = current.element;

        return result;
    }

}
