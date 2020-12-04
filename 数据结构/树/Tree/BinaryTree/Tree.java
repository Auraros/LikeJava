package src.Tree.BinaryTree;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-11-25 2:27
 * @Version : 1.0
 */
public interface Tree <T extends Comparable> {

    /**
     * 判断是否为空
     */
    boolean isEmpty();

    /**
     * 二叉树结点个数
     */
    int size();

    /**
     * 返回二叉树的高度或者深度，即结点的最大层次
     */
    int height();

    /**
     * 前序遍历
     *
     */
    String preOrder();

    /**
     * 中序遍历
     *
     */
    String inOrder();

    /**
     * 后序遍历
     */
    String postOrder();

    /**
     * 层次遍历
     */
    String levelOrder();

    /**
     * 将data插入
     */
    void insert(T data);

    /**
     * 删除
     */
    void remove(T data);

    /**
     * 查找最大值
     */
    T findMax();

    /**
     * 查找最小值
     */
    T findMin();

    /**
     * 根据值查找到结点
     */
    BinaryNode findNode(T data);

    /**
     * 是否包含某个值
     */
    boolean contains(T data);

    /**
     * 清空
     */
    void clear();
}
