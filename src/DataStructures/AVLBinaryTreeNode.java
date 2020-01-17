package DataStructures;

public class AVLBinaryTreeNode<T> extends BinaryTreeNode<T> {

    protected T element;
    protected AVLBinaryTreeNode<T> left, right;
    int height=0;
    int balance = 0;

    /**
     * Creates a new tree node with the specified data.    *    * @param obj  the element that will become a part of     * the new tree node
     *
     * @param obj
     */
    public AVLBinaryTreeNode(T obj) {
        super(obj);
    }

    /**
     * Creates a new tree node with the specified data.    *    * @param obj  the element that will become a part of     * the new tree node
     */

    /**
     * Returns the number of non-null children of this node.    * This method may be able to be written more efficiently.    *    * @return  the integer number of non-null children of this node
     */
    public int numChildren() {
        int children = 0;
        if (left != null) children = 1 + left.numChildren();
        if (right != null) children = children + 1 + right.numChildren();
        return children;
    }

    public AVLBinaryTreeNode leftchild() {return this.left; }
    public AVLBinaryTreeNode leftright() {return this.right; }
    public T getElement(){ return  this.element;}

    public void setElement(T element){this.element=element;}
    public boolean isInternal(){
        return (this.leftchild()!=null) || (this.leftright() != null);
    }
}
