package collections.tree.avl;

public class AVLBinaryTreeNode<T> {

    protected int height = 0;
    protected int balance = 0;
    protected T element;
    protected AVLBinaryTreeNode<T> left, right;

    /**
     * Creates a new tree node with the specified data.
     *
     * @param obj the element that will become a part of the new tree node
     */
    AVLBinaryTreeNode(T obj) {
        element = obj;
        left = null;
        right = null;
    }

    public boolean isInternal(){
        return (left != null) || (right != null);
    }
    
    /**
     * Returns the number of non-null children of this node. This method may be
     * able to be written more efficiently.
     *
     * @return the integer number of non-null children of this node
     */
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
