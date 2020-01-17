package DataStructures;

import ADT.BinarySearchTreeADT;
import ADT.ElementNotFoundException;

public class LinkedBinarySearchTree<T> extends LinkedBinaryTree implements BinarySearchTreeADT {

    boolean found;
    BinaryTreeNode<T> genericnode;
    /**
     * Creates an empty binary search tree.
     */
    public LinkedBinarySearchTree() {
        super();
    }

    /**
     * Creates a binary search with the specified element as its root.
     *
     * @param element the element that will be the root of the new
     *                binary search tree
     */
    public LinkedBinarySearchTree(T element) {
        super(element);
    }

    @Override
    public void addElement(Object element) {
        BinaryTreeNode<T> temp = new BinaryTreeNode<T>((T) element);
        Comparable<T> comparableElement = (Comparable<T>) element;
        if (isEmpty())
            root = temp;
        else {
            BinaryTreeNode<T> current = root;
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
        }
        count++;
    }

    @Override
    public Object removeElement(Object targetElement) throws ElementNotFoundException {
        T result = null;
        if (!isEmpty()) {
            if (((Comparable) targetElement).equals(root.element)) {
                result = (T) root.element;
                root = replacement(root);
                count--;
            } else {
                BinaryTreeNode<T> current, parent = root;
                found = false;
                if (((Comparable) targetElement).compareTo(root.element) < 0)
                    current = root.left;
                else
                    current = root.right;
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

                        if (((Comparable) targetElement).compareTo(current.element) < 0)
                            current = current.left;
                        else
                            current = current.right;
                    }
                } //while

                if (!found)
                    throw new ElementNotFoundException("binary search tree");

            }
        }// end outer if
        return result;
    }

    protected BinaryTreeNode<T> replacement(BinaryTreeNode<T> node) {
        BinaryTreeNode<T> result = null;
        if ((node.left == null) && (node.right == null))
            result = null;

        else if ((node.left != null) && (node.right == null))
            result = node.left;

        else if ((node.left == null) && (node.right != null))
            result = node.right;
        else {
            BinaryTreeNode<T> current = node.right;
            BinaryTreeNode<T> parent = node;
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
    public void removeAllOccurrences(Object targetElement) throws ElementNotFoundException {

        //Demasiado aldrabado e de grande complexidade
        //No exercicio fala de distinguir o primeiro das sucessivas. Como? Do while?
        do{
            removeElement(targetElement);
        }while(found==true);
        //Com o ciclo while assim, quando não encontrar (found==false) no fim da iteração da função,

    }

    @Override
    public Object removeMin() {
        T result =  (T)findMin();
        count--;
        replacement(getANode());
        return result;

    }

    @Override
    public Object removeMax() {
        T result =  (T)findMax();
        replacement(getANode());
        count--;
        return result;
    }

    @Override
    public Object findMin() {
        T result = null;
        BinaryTreeNode<T> node = root;
        if(root.getElement()!=null) {
            while (node.leftchild() != null) {
                node=node.leftchild();
            }
            setANode(node);
            result = node.getElement();
       /*     if(node.isInternal())
                node = node.right; */

        }
        return result;
    }

    private void setANode(BinaryTreeNode<T> btn){
        this.genericnode = btn;
    }

    private BinaryTreeNode<T> getANode(){
        BinaryTreeNode<T> tempnode = genericnode;
        genericnode= null;
        return tempnode;
    }

    @Override
    public Object findMax() {
        T result = null;
        BinaryTreeNode<T> node = root;
        if(root.getElement()!=null) {
            while (node.right != null) {
                node=node.right;
            }
            setANode(node);
            result = node.getElement();
       /*     if(node.isInternal())
                node = node.right; */

        }
        return result;
    }
}
