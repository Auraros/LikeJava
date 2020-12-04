package src.Tree.AVLTree;

import src.Link.ArrayList;
import src.Tree.BinaryTree.BinaryNode;
import src.Tree.BinaryTree.BinarySearchTree;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-03 20:00
 * @Version : 1.0
 */
public class AVLTree <T extends Comparable> implements Tree<T> {

    public AVLNode<T> root;

    @Override
    public boolean isEmpty() {
        return root==null;
    }


    @Override
    public int size() {
        return size(root);
    }


    public int size(AVLNode<T> subtree){
        if(subtree==null){
            return 0;
        }else {
            return size(subtree.left) + 1 + size(subtree.right);
        }


    }


    @Override
    public int height() {
        return height(root);
    }


    /**
     * @param p
     * @return
     */
    public int height(AVLNode<T> p){
        return p == null ? -1 : p.height;
    }


    @Override
    public String preOrder() {
        String sb=preOrder(root);
        if(sb.length()>0){
            //去掉尾部","号
            sb=sb.substring(0,sb.length()-1);
        }
        return sb;
    }

    /**
     * 先根遍历
     * @param subtree
     * @return
     */
    public String preOrder(AVLNode<T> subtree){
        StringBuilder sb =new StringBuilder();
        if (subtree!=null) {
            //先访问根结点
            sb.append(subtree.data).append(",");
            //访问左子树
            sb.append(preOrder(subtree.left));
            //访问右子树
            sb.append(preOrder(subtree.right));
        }
        return sb.toString();
    }

    @Override
    public String inOrder() {
        String sb=inOrder(root);
        if(sb.length()>0){
            //去掉尾部","号
            sb=sb.substring(0,sb.length()-1);
        }
        return sb;
    }

    /**
     * 中根遍历
     * @param subtree
     * @return
     */
    private String inOrder(AVLNode<T> subtree){
        StringBuilder sb =new StringBuilder();
        if (subtree!=null) {
            //访问左子树
            sb.append(inOrder(subtree.left));
            //访问根结点
            sb.append(subtree.data).append(",");
            //访问右子树
            sb.append(inOrder(subtree.right));
        }
        return sb.toString();
    }

    @Override
    public String postOrder() {
        String sb=postOrder(root);
        if(sb.length()>0){
            //去掉尾部","号
            sb=sb.substring(0,sb.length()-1);
        }
        return sb;
    }

    /**
     * 后根遍历
     * @param subtree
     * @return
     */
    private String postOrder(AVLNode<T> subtree){
        StringBuilder sb =new StringBuilder();
        if (subtree!=null){
            //访问左子树
            sb.append(postOrder(subtree.left));
            //访问右子树
            sb.append(postOrder(subtree.right));
            //访问根结点
            sb.append(subtree.data).append(",");
        }
        return sb.toString();
    }

    @Override
    public String levelOrder() {
        /**
         * @see BinarySearchTree#levelOrder()
         * @return
         */
        return null;
    }


    @Override
    public void insert(T data) {

        if(data == null)
            throw new RuntimeException("data is not be null");

        this.root = insert(data, root);

    }


    public AVLNode<T> insert(T data, AVLNode<T> node){

        if(node == null){
            node = new AVLNode<T>(data);
        }

        int result = data.compareTo(node.data);

        if(result < 0){
            node.left = insert(data, node.left);

            //插入后计算子树的高度，等于2则需要重新恢复平衡；由于是左边插入，左子树的高度肯定大于右子树的高度
            if(height(node.left) - height(node.right) == 2){

                //判断插入节点是左还是右
                if(data.compareTo(node.left.data) < 0){
                    //进行LL旋转
                    node = singleRotateRight(node);
                }else {
                    //进行左右旋转
                    node = doubleRotateWithLeft(node);
                }
            }

        }else if(result > 0){

            node.right = insert(data, node.right);

            if(height(node.right) - height(node.left) == 2){

                if(data.compareTo(node.right.data) < 0){

                    node = doubleRotateWithRight(node);
                }else{
                    node = singleRotateRight(node);
                }
            }

        }

        else ;

        node.height = Math.max(height(node.left), height(node.right)) + 1;

        return node;
    }


    /**
     * 查找最小值结点
     * @param p
     * @return
     */
    private AVLNode<T> findMin(AVLNode<T> p){
        if (p==null)//结束条件
            return null;
        else if (p.left==null)//如果没有左结点,那么t就是最小的
            return p;
        return findMin(p.left);
    }

    @Override
    public T findMin() {
        return findMin(root).data;
    }

    @Override
    public T findMax() {
        return findMax(root).data;
    }

    /**
     * 查找最大值结点
     * @param p
     * @return
     */
    private AVLNode<T> findMax(AVLNode<T> p){
        if (p==null)
            return null;
        else if (p.right==null)//如果没有右结点,那么t就是最大的
            return p;
        return findMax(p.right);
    }

    @Override
    public BinaryNode findNode(T data) {
        /**
         * @see BinarySearchTree#findNode(Comparable)
         * @return
         */
        return null;
    }

    @Override
    public boolean contains(T data) {
        return data != null && contain(data, root);
    }

    public boolean contain(T data , AVLNode<T> subtree){

        if (subtree==null)
            return false;

        int result =data.compareTo(subtree.data);

        if (result<0){
            return contain(data,subtree.left);
        }else if(result>0){
            return contain(data,subtree.right);
        }else {
            return true;
        }
    }

    @Override
    public void clear() {
        this.root=null;
    }


    /**
     * 左左单旋转（LL旋转）W变为x的根节点，x变为w的右子树
     * @param x
     * @return
     */
    private AVLNode<T> singleRotateLeft(AVLNode<T> x){

        //把w结点旋转为根节点
        AVLNode<T> w = x.left;
        //同时把w的右子树变成x的左子树
        x.left = w.right;
        //把x变成w的右子树
        w.right = x;
        //重新解释x/w的高度
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        w.height = Math.max(height(w.left), x.height)+1;
        return w;
    }

    /**
     * 右右单旋转(RR旋转) x变为w的根结点, w变为x的左子树
     * @return
     */
    private AVLNode<T> singleRotateRight(AVLNode<T> w){

        //把w的右节点为根节点
        AVLNode<T> x = w.right;
        //将x的左子树给w的右子树
        w.right = x.left;
        //将x的左子树指向w
        x.left = w;

        //重新计算x/w的高度
        w.height=Math.max(height(w.left),height(w.right))+1;
        x.height=Math.max(height(x.left),w.height)+1;

        //返回新的根结点
        return x;
    }

    /**
     * 左右旋转(LR旋转) x(根) w y 结点 把y变成根结点
     * @return
     */
    private AVLNode<T> doubleRotateWithLeft(AVLNode<T> x){
        //w先进行RR旋转
        x.left=singleRotateRight(x.left);
        //再进行x的LL旋转
        return singleRotateLeft(x);
    }


    /**
     * 右左旋转(RL旋转)
     * @param x
     * @return
     */
    private AVLNode<T> doubleRotateWithRight(AVLNode<T> x){
        //先进行LL旋转
        x.right=singleRotateLeft(x.right);
        //再进行RR旋转java
        return singleRotateRight(x);
    }


    @Override
    public void remove(T data) {
        if(data == null)
            throw new RuntimeException("data can not be null");

        this.root = remove(data, root);
    }

    public AVLNode<T> remove(T data, AVLNode<T> node){
        if(node == null)
            return null;

        int result = data.compareTo(node.data);

        if(result < 0) {
            node.left = remove(data, node.left);

            //检测是否平衡
            if (height(node.right) - height(node.left) == 2) {
                AVLNode<T> currentNode = node.right;
                //判断需要那种旋转
                if (height(currentNode.left) > height(currentNode.right)) {
                    //RL
                    node = doubleRotateWithRight(node);
                } else {
                    //RR
                    node = singleRotateRight(node);
                }
            }
        }

        //从右子树查找需要删除的元素
        else if(result>0){
            node.right=remove(data,node.right);
            //检测是否平衡
            if(height(node.left)-height(node.right)==2){
                AVLNode<T> currentNode=node.left;
                //判断需要那种旋转
                if(height(currentNode.right)>height(currentNode.left)){
                    //LR
                    node=doubleRotateWithLeft(node);
                }else{
                    //LL
                    node=singleRotateLeft(node);
                }
            }

        }

        //已找到需要删除的元素,并且要删除的结点拥有两个子节点
        else if(node.right!=null&&node.left!=null){
            //寻找替换结点
            node.data=findMin(node.right).data;
            //移除用于替换的结点
            node.right = remove( node.data, node.right );
        }
        else {
            //只有一个孩子结点或者只是叶子结点的情况
            node=(node.left!=null)? node.left:node.right;
        }

        //更新高度值
        if(node!=null)
            node.height = Math.max( height(node.left ), height( node.right ) ) + 1;
        return node;
    }


    private void printTree( AVLNode<T> t ) {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.data );
            printTree( t.right );
        }
    }

    /**
     * 测试
     * @param arg
     */
    public  static void main(String arg[]){

        AVLTree<Integer> avlTree=new AVLTree<>();

        for (int i = 1; i <18 ; i++) {
            avlTree.insert(i);
        }

        avlTree.printTree(avlTree.root);
        //删除11,8以触发旋转平衡操作
        avlTree.remove(11);
        avlTree.remove(8);

        System.out.println("================");

        avlTree.printTree(avlTree.root);

        System.out.println("findMin:"+avlTree.findMin());

        System.out.println("findMax:"+avlTree.findMax());

        System.out.println("15 exist or not : " + avlTree.contains(15) );

        System.out.println("先根遍历:"+avlTree.preOrder());

        System.out.println("中根遍历:"+avlTree.inOrder());

        System.out.println("后根遍历:"+avlTree.postOrder());

    }

}

