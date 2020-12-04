package src.Tree.HuffmanTree;
import java.util.*;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-03 22:06
 * @Version : 1.0
 */
public class HuffmanTree implements Tree{

    public static HuffmanNode createHuffmanTree(ArrayList<HuffmanNode> huffmanNodeList){

        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

        for(HuffmanNode  node : huffmanNodeList){
            pq.add(node);
        }

        while (pq.size()>1){

            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode root = new HuffmanNode(null, left.weight + right.weight, left, right);
            pq.add(root);
        }

        return pq.poll();

    }

    @Override
    public int size(HuffmanNode node) {
        if(node == null){
            return 0;
        }else{
            return size(node.right) + 1 + size(node.left);
        }
    }



    @Override
    public int height(HuffmanNode node) {
        if(node == null){
            return 0;
        }else{
            int left = height(node.left);
            int right = height(node.right);
            return (left - right) > 0 ? left + 1: right + 1; //加上当前层
        }
    }


    @Override
    public boolean isEmpty(HuffmanNode root) {
        return root == null;
    }


    @Override
    public String preOrder(HuffmanNode node) {

        StringBuffer sb = new StringBuffer();

        if (node ==null) {//递归结束条件
            return "";
        }
        //先访问根结点
        sb.append(node.weight+",");
        //遍历左子树
        sb.append(preOrder(node.left));
        //遍历右子树
        sb.append(preOrder(node.right));

        return sb.toString().substring(0, sb.length()-1);
    }



    @Override
    public String inOrder(HuffmanNode node) {

        StringBuffer sb = new StringBuffer();

        if(node==null){
            return "";
        }
        sb.append(inOrder(node.left));
        sb.append(node.weight + ",");
        sb.append(inOrder(node.right));
        return sb.toString().substring(0, sb.length()-1);
    }


    @Override
    public String postOrder(HuffmanNode node) {
        StringBuffer sb = new StringBuffer();
        if(node == null){
            return "";
        }

        sb.append(postOrder(node.left));
        sb.append(postOrder(node.right));
        sb.append(node.weight + ",");
        return sb.toString().substring(0, sb.length()-1);
    }


    @Override
    public  String levelOrder(HuffmanNode root) {

        LinkedList<HuffmanNode> queue = new LinkedList<>();
        StringBuffer sb = new StringBuffer();
        HuffmanNode node = root;

        while(node!=null){

            sb.append(node.weight + ",");

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



    public static void main(String[] args) {

        HuffmanTree huffmanTree = new HuffmanTree();
        ArrayList<HuffmanNode> list = new ArrayList<>();
        list.add(new HuffmanNode("A", 1));
        list.add(new HuffmanNode("B", 1));
        list.add(new HuffmanNode("C", 3));
        list.add(new HuffmanNode("D", 4));
        list.add(new HuffmanNode("E", 5));
        list.add(new HuffmanNode("F", 6));
        HuffmanNode root = createHuffmanTree(list);

        System.out.println(huffmanTree.levelOrder(root));
    }
}
