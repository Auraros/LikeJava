package src.Tree.BinaryTree;


import java.beans.BeanInfo;
import java.net.StandardSocketOptions;
import java.util.EmptyStackException;
import java.util.LinkedList;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-11-25 2:44
 * @Version : 1.0
 */
public class BinarySearchTree<T extends Comparable> implements Tree<T> {

    protected BinaryNode<T> root;

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public int size() {
        return size(root);
    }

    public int size(BinaryNode<T> node){

        if(node == null){
            return 0;
        }else{
            return size(node.right) + 1 + size(node.left);
        }

    }

    @Override
    public int height() {
        return height(root);
    }

    public int height(BinaryNode<T> node){

        if(root == null){
            return 0;
        }else{
            int left = height(node.left);
            int right = height(node.right);
            return (left - right) > 0 ? left + 1: right + 1; //加上当前层
        }

    }

    @Override
    public String preOrder() {
        String sb = preOrder(root);

        if(sb.length() > 0){ //删除尾部的,

            sb = sb.substring(0, sb.length()-1);
        }

        return sb;
    }

    public String preOrder(BinaryNode<T> node){
        StringBuffer sb = new StringBuffer();

        if (node ==null) {//递归结束条件
            return "";
        }
            //先访问根结点
            sb.append(node.data+",");
            //遍历左子树
            sb.append(preOrder(node.left));
            //遍历右子树
            sb.append(preOrder(node.right));

        return sb.toString();

    }

    @Override
    public String inOrder() {
        String sb  = inOrder(root);
        if(sb.length() > 0){

            //删除尾部节点
            sb = sb.substring(0, sb.length()-1);

        }
        return sb;
    }

    public String inOrder(BinaryNode<T> node){

        StringBuffer sb = new StringBuffer();

        if(node==null){
            return "";
        }
            sb.append(inOrder(node.left));
            sb.append(node.data + ",");
            sb.append(inOrder(node.right));
        return sb.toString();
    }

    @Override
    public String postOrder() {
        String sb = postOrder(root);
        if(sb.length() > 0){
            sb = sb.substring(0, sb.length()-1);
        }
        return sb;
    }

    public String postOrder(BinaryNode<T> node){
        StringBuffer sb = new StringBuffer();
        if(node == null){
            return "";
        }

        sb.append(postOrder(node.left));
        sb.append(postOrder(node.right));
        sb.append(node.data + ",");
        return sb.toString();
    }

    @Override
    public String levelOrder() {

        LinkedList<BinaryNode<T>> queue = new LinkedList<>();
        StringBuffer sb = new StringBuffer();
        BinaryNode<T> node = root;

        while(node!=null){

            sb.append(node.data + ",");

            if(node.left!=null){
                queue.add(node.left);
            }
            if(node.right!= null){
                queue.add(node.right);
            }
            node = queue.poll();
        }

        return sb.toString().substring(0, sb.length()-1);
    }

    @Override
    public void insert(T data) {

        if(data == null)
            throw new RuntimeException("data can\'Comparable be null");
        //插入操作
        root = insert(data, root);

    }

    /**
     * 插入操作
     * @param data
     */
    private BinaryNode<T> insert(T data, BinaryNode<T> node){
        if(node == null){
            node = new BinaryNode<T>(data, null, null);
        }

        //比较插入节点的值，决定插入方向
        int compareResult = data.compareTo(node.data);

        if(compareResult < 0){
            node.left = insert(data, node.left);
        }else if(compareResult > 0){
            node.right = insert(data, node.right);
        }else{
            ;//
        }
        return node;
    }

    @Override
    public void remove(T data) {

        if(data == null)
            throw new RuntimeException("data can\'Comparable be null");

        //删除操作
        root = remove(data, root);

    }

    private BinaryNode<T> remove(T data, BinaryNode<T> node){

        if(node == null){
            return node;
        }

        int CompareResult = data.compareTo(node.data);

        if(CompareResult < 0){
            node.left = remove(data, node.left);
        }else if(CompareResult > 0){
            node.right = remove(data, node.right);
        }else if(node.left != null && node.right != null){  //判断是否有两个子节点

            //将要删除的节点用该节点右节点的最小值替换
            node.data = findMin(node.right).data;
            //移除用于替换的节点
            node.right = remove(node.data, node.right);

        }else{  //拥有一个孩子节点

            node = (node.left != null) ? node.left : node.right;
        }

        return node;

    }


    /**
     * 非递归删除
     * @return
     */
    public T removeUnrecure(T data){

        if(data == null)
            throw new RuntimeException("data can\'Comparable be null !");

        //从根节点开始查找
        BinaryNode<T> current = this.root;
        //记录父节点
        BinaryNode<T> parent = this.root;

        //判断左右孩子的flag
        boolean isLeft = true;

        //找到要删除的节点
        while( data.compareTo(current.data)!=0){
            //更新父节点
            parent = current;
            int result = data.compareTo(current.data);

            if(result < 0){ //从左子树
                isLeft = true;
                current = current.left;
            }else if(result > 0){
                isLeft = false;
                current = current.right;
            }

            if(current == null){
                return null;
            }
        }

        //找到了要删除的节点

        //删除的是叶子节点
        if (current.left == null && current.right == null){
            if(current == this.root){
                this.root = null;
            }else if(isLeft){
                parent.left= null;
            }else{
                parent.right = null;
            }
        }


        //删除带有一个孩子的节点，当current的right不为null
        else if(current.left == null){

            if(current == root){
                this.root = current.right;
            }else if(isLeft){ //current为parent的左子节点

                parent.left = current.right;

            }else{

                parent.right = current.right;
            }

        }

        //删除带有一个孩子的节点，当current的left不为null
        else if (current.right == null){

            if(current == root){
                this.root = current.left;
            }else if(isLeft) {
                parent.left = current.left;
            }else{
                parent.right = current.left;
            }
        }

        //删除两个节点的
        else{

            //找到当前要删除节点current的右子树中的最小值
            BinaryNode<T> successor = finSuccessor(current);

            if(current == root){
                this.root = successor;
            }else  if(isLeft){
                parent.left = successor;
            }else{
                parent.right = successor;
            }
            //把当前要删除的结点的左孩子赋值给successor
            successor.left = current.left;

        }

        return current.data;
    }

    /**
     * 查找右子节点最小值
     * @return
     */
    public BinaryNode<T> finSuccessor(BinaryNode<T> delNode){

        BinaryNode<T> successor = delNode;
        BinaryNode<T> successorParent = delNode;
        BinaryNode<T> current = delNode.right;

        //不断查找左结点,直到为空,则successor为最小值结点
        while(current != null) {
            successorParent = successor;
            successor = current;
            current = current.left;
        }
        //如果要删除结点的右孩子与successor不相等,则执行如下操作(如果相当,则说明删除结点)
        if(successor != delNode.right) {
            successorParent.left = successor.right;
            //把中继结点的右孩子指向当前要删除结点的右孩子
            successor.right = delNode.right;
        }
        return successor;
    }

    @Override
    public T findMax() {
        if(isEmpty())
            throw new RuntimeException("BinarySearchTree is empty!");
        return findMax(root).data;

    }

    public BinaryNode<T> findMax(BinaryNode<T> node){

        if(node == null){
            return null;
        }if(node.right == null){
            return node;
        }

        return findMax(node.right);

    }


    @Override
    public T findMin() {
        if(isEmpty())
            throw new RuntimeException("BinarySearchTree is empty!");

        return findMin(root).data;
    }

    /**
     * 查找最小值节点
     * @param node
     * @return
     */
    public BinaryNode<T> findMin(BinaryNode<T> node){

        if(node == null){
            return null;
        }else if(node.left == null){  //如果没有左节点
            return node;
        }
        return findMin(node.left);

    }

    @Override
    public BinaryNode findNode(T data) {

        BinaryNode res = findNode(data, root);

        if (res == null) {
            System.out.println("not found");
            return new BinaryNode(-1);
        } else {
            return res;

        }
    }

    public BinaryNode findNode(T data, BinaryNode<T> node){

        if(node == null){
            return null;
    }

        int compareResult = data.compareTo(node.data);

        //System.out.println(compareResult);

        if(compareResult < 0){
            findNode(data, node.left);
        }else if(compareResult > 0){
            findNode(data, node.right);
        }else{
            return node;
        }

        return null;
    }

    @Override
    public boolean contains(T data) {

        boolean res = contains(data, root);

        if (!res) {
            System.out.println("not found");
            return false;
        } else {
            return true;

        }

    }

    public boolean contains(T data, BinaryNode<T> node){

        if(node == null){
            return false;
        }

        int compareResult = data.compareTo(node.data);

        //System.out.println(compareResult);

        if(compareResult < 0){
            contains(data, node.left);
        }else if(compareResult > 0){
            contains(data, node.right);
        }else{
            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

    public static void main(String[] args) {
        BinaryNode<Integer> root = new BinaryNode<>(2);
        BinarySearchTree<Integer> bTree = new BinarySearchTree<Integer>();
        bTree.insert(3);
        bTree.insert(4);
        bTree.insert(2);
        bTree.insert(1);

        bTree.remove(3);

        System.out.println(bTree.preOrder());  //3,2,1,4
        System.out.println(bTree.inOrder());  //1,2,3,4
        System.out.println(bTree.postOrder());  //1,2,4,3
        System.out.println(bTree.levelOrder());  //3,2,4,1
        System.out.println(bTree.findNode(3).data);  //-1
        System.out.println(bTree.contains(3));  //-1
    }

}
