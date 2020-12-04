package src.Tree.BPlusTree;

import java.util.List;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-04 22:14
 * @Version : 1.0
 */
public class BPlusNode {

    //节点的子节点
    private List<BPlusNode> BPlusNodes;
    //节点的键值对
    private List<KeyAndValue> keyAndValue;
    //节点的后节点
    private BPlusNode nextBPlusNode;
    //节点的前节点
    private BPlusNode previousBPlusNode;
    //节点的父节点
    private BPlusNode parantBPlusNode;

    public BPlusNode( List<BPlusNode> BPlusNodes, List<KeyAndValue> keyAndValue, BPlusNode nextBPlusNode,BPlusNode previousBPlusNode, BPlusNode parantBPlusNode) {
        this.BPlusNodes = BPlusNodes;
        this.keyAndValue = keyAndValue;
        this.nextBPlusNode = nextBPlusNode;
        this.parantBPlusNode = parantBPlusNode;
        this.previousBPlusNode = previousBPlusNode;
    }

    boolean isLeaf() {
        return BPlusNodes==null;
    }

    boolean isHead() {
        return previousBPlusNode == null;
    }

    boolean isTail() {
        return nextBPlusNode == null;
    }

    boolean isRoot() {
        return parantBPlusNode == null;
    }


    List<BPlusNode> getBPlusNodes() {
        return BPlusNodes;
    }

    void setBPlusNodes(List<BPlusNode> BPlusNodes) {
        this.BPlusNodes = BPlusNodes;
    }


    List<KeyAndValue> getKeyAndValue() {
        return keyAndValue;
    }

//    public void setKeyAndValue(List<KeyAndValue> KeyAndValue) {
//        this.keyAndValue = KeyAndValue;
//    }

    BPlusNode getNextBPlusNode() {
        return nextBPlusNode;
    }

    void setNextBPlusNode(BPlusNode nextBPlusNode) {
        this.nextBPlusNode = nextBPlusNode;
    }

    BPlusNode getParantBPlusNode() {
        return parantBPlusNode;
    }

    void setParantBPlusNode(BPlusNode parantBPlusNode) {
        this.parantBPlusNode = parantBPlusNode;
    }

    BPlusNode getPreviousBPlusNode() {
        return previousBPlusNode;
    }

    void setPreviousBPlusNode(BPlusNode previousBPlusNode) {
        this.previousBPlusNode = previousBPlusNode;
    }
}