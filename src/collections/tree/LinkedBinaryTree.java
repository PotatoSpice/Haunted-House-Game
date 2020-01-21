package collections.tree;

import java.util.Iterator;
import collections.tree.adt.BinaryTreeADT;
import collections.exceptions.ElementNotFoundException;
import collections.exceptions.EmptyCollectionException;
import collections.queue.ArrayQueue;
import collections.list.unordered.ArrayUnorderedList;

public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {

    protected int count;
    protected BinaryTreeNode<T> root;

    /**
     * Creates an empty binary tree.
     */
    public LinkedBinaryTree() {
        count = 0;
        root = null;
    }

    /**
     * Creates a binary tree with the specified element as its root.
     *
     * @param element the element that will become the root of the new binary
     * tree
     */
    public LinkedBinaryTree(T element) {
        count = 1;
        root = new BinaryTreeNode<>(element);
    }

    @Override
    public T find(T targetElement) throws ElementNotFoundException, EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        BinaryTreeNode<T> target = findAgain(targetElement, root);
        if (target == null) {
            throw new ElementNotFoundException("binary tree");
        }
        return (target.element);
    }
    
    @Override
    public boolean contains(T targetElement) throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        return (findAgain(targetElement, root) != null);
    }
    
    /**
     * Returns a reference to the specified target element if it is found in
     * this binary tree.
     *
     * @param targetElement the element being sought in this tree
     * @param next the element to begin searching from
     */
    private BinaryTreeNode<T> findAgain(T targetElement, BinaryTreeNode<T> next) {
        if (next == null) {
            return null;
        }
        if (next.element.equals(targetElement)) {
            return next;
        }
        BinaryTreeNode<T> temp = findAgain(targetElement, next.left);
        if (temp == null) {
            temp = findAgain(targetElement, next.right);
        }
        return temp;
    }

    @Override
    public T getRoot() {
        return root.element;
    }

    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    @Override
    public int size() {
        return count;
    }

    /**
     * Performs an inorder traversal on this binary tree by calling an
     * overloaded, recursive inorder method that starts with the root.
     *
     * @return an in order iterator over this binary tree
     */
    @Override
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();
        inorder(root, tempList);
        return tempList.iterator();
    }
    
    /**
     * Performs a recursive inorder traversal.
     *
     * @param node the node to be used as the root for this traversal
     * @param tempList the temporary list for use in this traversal
     */
    protected void inorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            inorder(node.left, tempList);
            tempList.addToRear(node.element);
            inorder(node.right, tempList);
        }
    }

    @Override
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();
        preorder(root, tempList);
        return tempList.iterator();
    }
    
    /**
     * Performs a recursive preorder traversal.
     *
     * @param node the node to be used as the root for this traversal
     * @param tempList the temporary list for use in this traversal
     */
    protected void preorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            tempList.addToRear(node.element);
            preorder(node.left, tempList);
            preorder(node.right, tempList);
        }
    }

    @Override
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> tempList = new ArrayUnorderedList<>();
        postorder(root, tempList);
        return tempList.iterator();
    }
    
    /**
     * Performs a recursive postorder traversal.
     *
     * @param node the node to be used as the root for this traversal
     * @param tempList the temporary list for use in this traversal
     */
    protected void postorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tempList) {
        if (node != null) {
            postorder(node.left, tempList);
            postorder(node.right, tempList);
            tempList.addToRear(node.element);
        }
    }

    @Override
    public Iterator<T> iteratorLevelOrder() {
        ArrayQueue<BinaryTreeNode<T>> nodes = new ArrayQueue<>();
        ArrayUnorderedList<T> results = new ArrayUnorderedList<>();
        if (!isEmpty()) {
            nodes.enqueue(root);
            BinaryTreeNode<T> temp;
            try {
                while (!nodes.isEmpty()) {
                    // Queue will never be empty in the next line, so no need
                    // to check if 'temp' would be 'null'!
                    temp = nodes.dequeue();
                    results.addToRear(temp.element);
                    // Check if 'temp' has children, to add to the queue
                    if (temp.left != null)
                        nodes.enqueue(temp.left);
                    if (temp.right != null)
                        nodes.enqueue(temp.right);
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
            ArrayQueue<BinaryTreeNode<T>> nodes = new ArrayQueue<>();
            nodes.enqueue(root);
            BinaryTreeNode<T> temp;
            try {
                while (!nodes.isEmpty()) {
                    // Queue will never be empty in the next line, so no need
                    // to check if 'temp' would be 'null'!
                    temp = nodes.dequeue();
                    s = s + "[ThisNode: " + temp.hashCode()
                            + "; LeftChild: " + ((temp.left != null)
                                    ? temp.left.hashCode() : "null")
                            + "; RightChild: " + ((temp.right != null)
                                    ? temp.right.hashCode() : "null")
                            + "; Element: " + temp.element + "]\n";
                    // Check if 'temp' has children, to add to the queue
                    if (temp.left != null) 
                        nodes.enqueue(temp.left);
                    if (temp.right != null)
                        nodes.enqueue(temp.right);
                }
            } catch (EmptyCollectionException exc) {
                // 'nodes.dequeue()' will never throw this exception.
                // But since the compiler doesn't know that, we still need to catch it.
            }
        }
        return s;
    }
}
