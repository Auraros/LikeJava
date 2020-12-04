package src.Tree.HuffmanTree;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-03 22:00
 * @Version : 1.0
 */
public class HuffmanNode<T> implements Comparable<HuffmanNode>{

    public T data;  //节点的权
    public HuffmanNode<T> left;  //左子节点
    public HuffmanNode<T> right;  //右子节点
    public int weight;

    public  HuffmanNode(){};


    public HuffmanNode(T data, int weight, HuffmanNode left, HuffmanNode right){
        this.data = data;
        this.left = left;
        this.right = right;
        this.weight = weight;
    }

    public HuffmanNode(T data, int weight){
        this(data, weight, null, null);
    }

    @Override
    public String toString() {
        return "TreeNode[data=" + data + ", weight=" + weight + "]";
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return this.weight - o.weight;
    }
}
