package DataStructures;

import ADT.BinaryTreeADT;
import ADT.ElementNotFoundException;
import ADT.EmptyCollectionException;
import ADT.LinkedQueue;

import java.util.Iterator;

public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {

    int count;
    BinaryTreeNode<T> root;

    /**
     * Creates an empty binary tree.
     */
    public LinkedBinaryTree() {
        count = 0;
        root = null;
    }

    /**
     * Creates a binary tree with the specified element as its root.    *    * @param element  the element that will become the root of the     * new binary tree
     */
    public LinkedBinaryTree(T element) {
        count = 1;
        root = new BinaryTreeNode<T>(element);
    }

    @Override
    public T getRoot() {
        return (T) this.root;
    }

    @Override
    public boolean isEmpty() {
        return (root==null);
    }

    @Override
    public int size() {
        return root.numChildren()+1;
    }

    @Override
    public boolean contains(Object targetElement) throws ElementNotFoundException {
        return  (find((T)targetElement)!=null);
    }

    @Override
    public T find(Object targetElement) throws ElementNotFoundException {
        BinaryTreeNode<T> current = findAgain((T)targetElement, root);
        if (current == null) throw new ElementNotFoundException("binary tree");
        return (current.element);
    }

    /**
     * Returns a reference to the specified target element if it is    * found in this binary tree.    *    * @param targetElement  the element being sought in this tree    * @param next           the element to begin searching from
     */
    private BinaryTreeNode<T> findAgain(T targetElement, BinaryTreeNode<T> next) {
        if (next == null) return null;
        if (next.element.equals(targetElement)) return next;
        BinaryTreeNode<T> temp = findAgain(targetElement, next.left);
        if (temp == null) temp = findAgain(targetElement, next.right);
        return temp;
    }

    @Override
    public Iterator iteratorInOrder() {
        ArrayUnorderedList<T> temp = new ArrayUnorderedList<T>();

        inorder(root, temp);

        return temp.iterator();

    }

    public void inorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> temp){
        if(node!=null){
            inorder(node.leftchild(), temp);
            temp.addToRear(node.element);
            inorder(node.leftright(), temp);
        }
    }

    @Override
    public Iterator iteratorPreOrder() {
        ArrayUnorderedList<T> temp = new ArrayUnorderedList<T>();

        preorder(root, temp);

        return temp.iterator();
    }

    private void preorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> temp){
        if(node!=null){
            temp.addToRear(node.element);
            preorder(node.leftchild(), temp);
            preorder(node.leftright(), temp);
        }
    }

    public Iterator iteratorPostOrder() {
        ArrayUnorderedList<T> temp = new ArrayUnorderedList<T>();

        postorder(root, temp);

        return temp.iterator();
    }

    private void postorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> temp){
        if(node!=null){
            postorder(node.leftchild(), temp);
            postorder(node.leftright(), temp);
            temp.addToRear(node.element);
        }
    }

    public Iterator iteratorlevelorder() throws EmptyCollectionException {

        ArrayUnorderedList<T> temp = new ArrayUnorderedList<>();
        levelorder(root, temp);

        return temp.iterator();

    }

    private void levelorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> temp) throws EmptyCollectionException {

        LinkedQueue<BinaryTreeNode<T>> queue = new LinkedQueue<>();
        BinaryTreeNode<T> temp2;
        if (node!=null){
            queue.enqueue(node);
            while (!queue.isEmpty()){
                temp2 = queue.dequeue();
                if (temp2 != null) {
                    temp.addToRear(temp2.getElement());
                    if (node.leftchild()!=null )
                        queue.enqueue(temp2.leftchild());
                    if (node.leftright()!=null)
                        queue.enqueue(temp2.leftright());
                } else {
                    temp.addToRear(null);
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        return null;
    }
}
