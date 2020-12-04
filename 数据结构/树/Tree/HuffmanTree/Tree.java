package src.Tree.HuffmanTree;


/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-11-25 2:27
 * @Version : 1.0
 */
public interface Tree<T extends Comparable> {

    /**
     * 判断是否为空
     */
    boolean isEmpty(HuffmanNode node);

    /**
     * 二叉树结点个数
     */
    int size(HuffmanNode node);

    /**
     * 返回二叉树的高度或者深度，即结点的最大层次
     */
    int height(HuffmanNode node);

    /**
     * 前序遍历
     *
     */
    String preOrder(HuffmanNode node);

    /**
     * 中序遍历
     *
     */
    String inOrder(HuffmanNode node);

    /**
     * 后序遍历
     */
    String postOrder(HuffmanNode node);

    /**
     * 层次遍历
     */
    String levelOrder( HuffmanNode node);

}
