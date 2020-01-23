package collections.tree.avl;

import collections.tree.BinaryTreeNode;

public class AVLBinaryTreeNode<T> extends BinaryTreeNode<T> {

    protected int height = 0;
    protected int balance = 0;
    protected AVLBinaryTreeNode<T> left, right;

    /**
     * Creates a new tree node with the specified data.
     *
     * @param obj the element that will become a part of the new tree node
     */
    AVLBinaryTreeNode(T obj) {
        super(obj);
        left = null;
        right = null;
    }

    public boolean isInternal(){
        return (left != null) || (right != null);
    }

    @Override
    public int numChildren() {
        int children = 0;
        if (left != null) {
            children = 1 + left.numChildren();
        }
        if (right != null) {
            children = children + 1 + right.numChildren();
        }
        return children;
    }
}