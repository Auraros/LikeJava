package src.Tree.BPlusTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author : Auraros
 * @Description :
 * @Data : 2020-12-04 22:23
 * @Version : 1.0
 */
public class BPlusTree {

    private static final String  NODE = " NODE";
    static final String INT = "INT";
    private static final String PREBPlusNode = "PREBPlusNode";
    private static final String NEXTBPlusNode = "NEXTBPlusNode";
    //B+树的阶数
    private int rank;
    //根节点
    private BPlusNode root;
    //头结点
    private BPlusNode head;

    BPlusTree(int rank) {
        this.rank = rank;
    }

    public BPlusNode getRoot() {
        return root;
    }

    public void insert(KeyAndValue entry) {
        List<KeyAndValue> keyAndValues1 = new ArrayList<>();
        //插入第一个节点
        if (head == null) {
            keyAndValues1.add(entry);
            head = new BPlusNode(null, keyAndValues1, null, null, null);
            root = new BPlusNode(null, keyAndValues1, null, null, null);
        } else {
            BPlusNode BPlusNode = head;
            //遍历链表，找到插入键值对对应的节点
            while (BPlusNode != null) {
                List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
                int exitFlag = 0;
                //如果插入的键的值和当前节点键值对集合中的某个键的值相等，则直接替换value
                for (KeyAndValue KV : keyAndValues) {
                    if (KV.getKey() == entry.getKey()) {
                        KV.setValue(entry.getValue());
                        exitFlag = 1;
                        break;
                    }
                }
                //如果插入的键已经有了，则退出循环
                if (exitFlag == 1) {
                    break;
                }
                //如果当前节点是最后一个节点或者要插入的键值对的键的值小于下一个节点的键的最小值，则直接插入当前节点
                if (BPlusNode.getNextBPlusNode() == null || BPlusNode.getNextBPlusNode().getKeyAndValue().get(0).getKey() >= entry.getKey()) {
                    splidBPlusNode(BPlusNode, entry);
                    break;
                }
                //移动指针
                BPlusNode = BPlusNode.getNextBPlusNode();
            }
        }
    }


    //判断是否需要拆分节点
    private void splidBPlusNode(BPlusNode BPlusNode, KeyAndValue addkeyAndValue) {
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();

        if (keyAndValues.size() == rank - 1) {
            //先插入待添加的节点
            keyAndValues.add(addkeyAndValue);
            Collections.sort(keyAndValues);
            //取出当前节点的键值对集合
            //取出原来的key-value集合中间位置的下标
            int mid = keyAndValues.size() / 2;
            //取出原来的key-value集合中间位置的键
            int midKey = keyAndValues.get(mid).getKey();
            //构造一个新的键值对，不是叶子节点的节点不存储value的信息
            KeyAndValue midKeyAndValue = new KeyAndValue(midKey, "");
            //将中间位置左边的键值对封装成集合对象
            List<KeyAndValue> leftKeyAndValues = new ArrayList<>();
            for (int i = 0; i < mid; i++) {
                leftKeyAndValues.add(keyAndValues.get(i));
            }
            //将中间位置右边边的键值对封装成集合对象
            List<KeyAndValue> rightKeyAndValues = new ArrayList<>();
            //如果是叶子节点则在原节点中保留上移的key-value，否则原节点删除上移的key-value
            int k;
            if (BPlusNode.isLeaf()) {
                k = mid;
            } else {
                k = mid + 1;
            }
            for (int i = k; i < rank; i++) {
                rightKeyAndValues.add(keyAndValues.get(i));
            }
            //对左右两边的元素重排序
            Collections.sort(leftKeyAndValues);
            Collections.sort(rightKeyAndValues);
            //以mid为界限将当前节点分列成两个节点，维护前指针和后指针
            BPlusNode rightBPlusNode;
            BPlusNode leftBPlusNode;
//            if (BPlusNode.isLeaf()) {
            //如果是叶子节点维护前后指针
            rightBPlusNode = new BPlusNode(null, rightKeyAndValues, BPlusNode.getNextBPlusNode(), null, BPlusNode.getParantBPlusNode());
            leftBPlusNode = new BPlusNode(null, leftKeyAndValues, rightBPlusNode, BPlusNode.getPreviousBPlusNode(), BPlusNode.getParantBPlusNode());
            rightBPlusNode.setPreviousBPlusNode(leftBPlusNode);
//            } else {
//                //如果不是叶子不维护前后指针
//                rightBPlusNode = new BPlusNode(null, rightKeyAndValues, null, null, BPlusNode.getParantBPlusNode());
//                leftBPlusNode = new BPlusNode(null, leftKeyAndValues, null, null, BPlusNode.getParantBPlusNode());
//            }
            //如果当前分裂的节点有孩子节点,设置分裂后节点和孩子节点的关系
            if (BPlusNode.getBPlusNodes() != null) {
                //取得所有地孩子节点
                List<BPlusNode> BPlusNodes = BPlusNode.getBPlusNodes();
                List<BPlusNode> leftBPlusNodes = new ArrayList<>();
                List<BPlusNode> rightBPlusNodes = new ArrayList<>();
                for (BPlusNode childBPlusNode : BPlusNodes) {
                    //取得当前孩子节点的最大键值
                    int max = childBPlusNode.getKeyAndValue().get(childBPlusNode.getKeyAndValue().size() - 1).getKey();
                    if (max < midKeyAndValue.getKey()) {
                        //小于mid处的键的数是左节点的子节点
                        leftBPlusNodes.add(childBPlusNode);
                        childBPlusNode.setParantBPlusNode(leftBPlusNode);
                    } else {
                        //大于mid处的键的数是右节点的子节点
                        rightBPlusNodes.add(childBPlusNode);
                        childBPlusNode.setParantBPlusNode(rightBPlusNode);
                    }
                }
                leftBPlusNode.setBPlusNodes(leftBPlusNodes);
                rightBPlusNode.setBPlusNodes(rightBPlusNodes);
            }

            //当前节点的前节点
            BPlusNode preBPlusNode = BPlusNode.getPreviousBPlusNode();
            //分裂节点后将分裂节点的前节点的后节点设置为左节点
            if (preBPlusNode != null) {
                preBPlusNode.setNextBPlusNode(leftBPlusNode);
            }

            //当前节点的后节点
            BPlusNode nextBPlusNode = BPlusNode.getNextBPlusNode();
            //分裂节点后将分裂节点的后节点的前节点设置为右节点
            if (nextBPlusNode != null) {
                nextBPlusNode.setPreviousBPlusNode(rightBPlusNode);
            }

            //如果由头结点分裂，则分裂后左边的节点为头节点
            if (BPlusNode == head) {
                head = leftBPlusNode;
            }

            //父节点的子节点
            List<BPlusNode> childBPlusNodes = new ArrayList<>();
            childBPlusNodes.add(rightBPlusNode);
            childBPlusNodes.add(leftBPlusNode);
            //分裂
            //当前节点无父节点
            if (BPlusNode.getParantBPlusNode() == null) {
                //父节点的键值对
                List<KeyAndValue> parentKeyAndValues = new ArrayList<>();
                parentKeyAndValues.add(midKeyAndValue);
                //构造父节点
                BPlusNode parentBPlusNode = new BPlusNode(childBPlusNodes, parentKeyAndValues, null, null, null);
                //将子节点与父节点关联
                rightBPlusNode.setParantBPlusNode(parentBPlusNode);
                leftBPlusNode.setParantBPlusNode(parentBPlusNode);
                //当前节点为根节点
                root = parentBPlusNode;
            } else {
                BPlusNode parentBPlusNode = BPlusNode.getParantBPlusNode();
                //将原来的孩子节点（除了被拆分的节点）和新的孩子节点（左孩子和右孩子）合并之后与父节点关联
                childBPlusNodes.addAll(parentBPlusNode.getBPlusNodes());
                //移除正在被拆分的节点
                childBPlusNodes.remove(BPlusNode);
                //将子节点与父节点关联
                parentBPlusNode.setBPlusNodes(childBPlusNodes);
                rightBPlusNode.setParantBPlusNode(parentBPlusNode);
                leftBPlusNode.setParantBPlusNode(parentBPlusNode);
                if (parentBPlusNode.getParantBPlusNode() == null) {
                    root = parentBPlusNode;
                }
                //当前节点有父节点,递归调用拆分的方法,将父节点拆分
                splidBPlusNode(parentBPlusNode, midKeyAndValue);
            }
        } else {
            keyAndValues.add(addkeyAndValue);
            //排序
            Collections.sort(keyAndValues);
        }
    }


    //打印B+树
    void printBPlusTree(BPlusNode root) {
        if (root == this.root) {
            //打印根节点内的元素
            printBPlusNode(root);
            System.out.println();
        }
        if (root == null) {
            return;
        }

        //打印子节点的元素
        if (root.getBPlusNodes() != null) {
            //找到最左边的节点
            BPlusNode leftBPlusNode = null;
            BPlusNode tmpBPlusNode = null;
            List<BPlusNode> childBPlusNodes = root.getBPlusNodes();
            for (BPlusNode BPlusNode : childBPlusNodes) {
                if (BPlusNode.getPreviousBPlusNode() == null) {
                    leftBPlusNode = BPlusNode;
                    tmpBPlusNode = BPlusNode;
                }
            }

            while (leftBPlusNode != null) {
                //从最左边的节点向右打印
                printBPlusNode(leftBPlusNode);
                System.out.print("|");
                leftBPlusNode = leftBPlusNode.getNextBPlusNode();
            }
            System.out.println();
            printBPlusTree(tmpBPlusNode);
        }
    }

    //打印一个节点内的元素
    private void printBPlusNode(BPlusNode BPlusNode) {
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
        for (int i = 0; i < keyAndValues.size(); i++) {
            if (i != (keyAndValues.size() - 1)) {
                System.out.print(keyAndValues.get(i).getKey() + ",");
            } else {
                System.out.print(keyAndValues.get(i).getKey());
            }
        }
    }

    public Object search(int key, BPlusNode BPlusNode, String mode) {

        //如果是叶子节点则直接取值
        if (BPlusNode.isLeaf()) {
            List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
            for (KeyAndValue keyAndValue : keyAndValues) {
                if (keyAndValue.getKey() == key) {
                    switch (mode) {
                        case NODE:
                            return BPlusNode;
                        case INT:
                            return keyAndValue.getValue();
                    }
                }
            }
            return null;
        }


        List<BPlusNode> BPlusNodes = BPlusNode.getBPlusNodes();
        //如果寻找的key小于节点的键的最小值
        int minKey = BPlusNode.getKeyAndValue().get(0).getKey();
        if (key < minKey) {
            for (BPlusNode n : BPlusNodes) {
                List<KeyAndValue> keyAndValues = n.getKeyAndValue();
                //找到子节点集合中最大键小于父节点最小键节点
                if (keyAndValues.get(keyAndValues.size() - 1).getKey() < minKey) {
                    return search(key, n, mode);
                }
            }
        }
        //如果寻找的key大于节点的键的最大值
        int maxKey = getMaxKeyInBPlusNode(BPlusNode);
        if (key >= maxKey) {
            for (BPlusNode n : BPlusNodes) {
                List<KeyAndValue> keyAndValues = n.getKeyAndValue();
                //找到子节点集合中最小键大于等于父节点最小大键节点
                if (keyAndValues.get(0).getKey() >= maxKey) {
                    return search(key, n, mode);
                }
            }
        }

        //如果寻找的key在最大值和最小值之间，首先定位到最窄的区间
        int min = getLeftBoundOfKey(BPlusNode, key);
        int max = getRightBoundOfKey(BPlusNode, key);


        //去所有的子节点中找键的范围在min和max之间的节点
        for (BPlusNode n : BPlusNodes) {
            List<KeyAndValue> kvs = n.getKeyAndValue();
            //找到子节点集合中键的范围在min和max之间的节点
            if (kvs.get(0).getKey() >= min && kvs.get(kvs.size() - 1).getKey() < max) {
                return search(key, n, mode);
            }
        }
        return null;
    }


    public boolean delete(int key) {
        System.out.println("delete:" + key);
        System.out.println();

        //首先找到要删除的key所在的节点
        BPlusNode deleteBPlusNode = (BPlusNode) search(key, root, NODE);
        //如果没找到则删除失败
        if (deleteBPlusNode == null) {
            return false;
        }

        if (deleteBPlusNode == root) {
            delKeyAndValue(root.getKeyAndValue(), key);
            return true;
        }

        if (deleteBPlusNode == head && isNeedMerge(head)) {
            head = head.getNextBPlusNode();
        }

        return merge(deleteBPlusNode, key);
    }


    //平衡当前节点和前节点或者后节点的数量，使两者的数量都满足条件
    private boolean balanceBPlusNode(BPlusNode BPlusNode, BPlusNode bratherBPlusNode, String BPlusNodeType) {
        if (bratherBPlusNode == null) {
            return false;
        }
        List<KeyAndValue> delKeyAndValues = BPlusNode.getKeyAndValue();
        if (isMoreElement(bratherBPlusNode)) {
            List<KeyAndValue> bratherKeyAndValues = bratherBPlusNode.getKeyAndValue();
            int bratherSize = bratherKeyAndValues.size();
            //兄弟节点删除挪走的键值对
            KeyAndValue keyAndValue = null;
            KeyAndValue keyAndValue1;
            switch (BPlusNodeType) {
                case PREBPlusNode:
                    keyAndValue = bratherKeyAndValues.remove(bratherSize - 1);
                    keyAndValue1 = getKeyAndValueinMinAndMax(BPlusNode.getParantBPlusNode(), keyAndValue.getKey(), getMinKeyInBPlusNode(BPlusNode));
                    keyAndValue1.setKey(keyAndValue.getKey());
                    break;
                case NEXTBPlusNode:
                    keyAndValue = bratherKeyAndValues.remove(0);
                    keyAndValue1 = getKeyAndValueinMinAndMax(BPlusNode.getParantBPlusNode(), getMaxKeyInBPlusNode(BPlusNode), keyAndValue.getKey());
                    keyAndValue1.setKey(bratherKeyAndValues.get(0).getKey());
                    break;
            }
            //当前节点添加从前一个节点得来的键值对
            delKeyAndValues.add(keyAndValue);

            //对键值对重排序
            Collections.sort(delKeyAndValues);
            return true;
        }
        return false;
    }

    public boolean merge(BPlusNode BPlusNode, int key) {
        List<KeyAndValue> delKeyAndValues = BPlusNode.getKeyAndValue();
        //首先删除该key-vaule
        delKeyAndValue(delKeyAndValues, key);
        //如果要删除的节点的键值对的数目小于节点最大键值对数目*填充因子
        if (isNeedMerge(BPlusNode)) {
            Boolean isBalance;
            //如果左节点有富余的键值对，则取一个到当前节点
            BPlusNode preBPlusNode = getPreviousBPlusNode(BPlusNode);
            isBalance = balanceBPlusNode(BPlusNode, preBPlusNode, PREBPlusNode);
            //如果此时已经平衡，则已经删除成功
            if (isBalance) return true;

            //如果右兄弟节点有富余的键值对，则取一个到当前节点
            BPlusNode nextBPlusNode = getNextBPlusNode(BPlusNode);
            isBalance = balanceBPlusNode(BPlusNode, nextBPlusNode, NEXTBPlusNode);

            return isBalance || mergeBPlusNode(BPlusNode, key);
        } else {
            return true;
        }
    }

    //合并节点
    //key 待删除的key
    private boolean mergeBPlusNode(BPlusNode BPlusNode, int key) {
        if (BPlusNode.isRoot()) {
            return false;
        }
        BPlusNode preBPlusNode;
        BPlusNode nextBPlusNode;
        BPlusNode parentBPlusNode = BPlusNode.getParantBPlusNode();
        List<BPlusNode> childBPlusNodes = parentBPlusNode.getBPlusNodes();
        List<BPlusNode> childBPlusNodes1 = BPlusNode.getBPlusNodes();
        List<KeyAndValue> parentKeyAndValue = parentBPlusNode.getKeyAndValue();
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();

        if (BPlusNode.isLeaf()) {
            if (parentKeyAndValue.size() == 1 && parentBPlusNode != root) {
                return true;
            }
            preBPlusNode = getPreviousBPlusNode(BPlusNode);
            nextBPlusNode = getNextBPlusNode(BPlusNode);
            if (preBPlusNode != null) {
                List<KeyAndValue> preKeyAndValues = preBPlusNode.getKeyAndValue();
                keyAndValues.addAll(preKeyAndValues);
                if (preBPlusNode.isHead()) {
                    head = BPlusNode;
                    BPlusNode.setPreviousBPlusNode(null);
                } else {
                    preBPlusNode.getPreviousBPlusNode().setNextBPlusNode(BPlusNode);
                    BPlusNode.setPreviousBPlusNode(preBPlusNode.getPreviousBPlusNode());
                }
                //将合并后节点的后节点设置为当前节点的后节点
                preBPlusNode.setNextBPlusNode(BPlusNode.getNextBPlusNode());
                KeyAndValue keyAndValue = getKeyAndValueinMinAndMax(parentBPlusNode, getMinKeyInBPlusNode(preBPlusNode), key);
                delKeyAndValue(parentKeyAndValue, keyAndValue.getKey());
                if (parentKeyAndValue.isEmpty()) {
                    root = BPlusNode;
                } else {
                    //删除当前节点
                    childBPlusNodes.remove(preBPlusNode);
                }
                Collections.sort(keyAndValues);
                merge(parentBPlusNode, key);
                return true;
            }

            if (nextBPlusNode != null) {
                List<KeyAndValue> nextKeyAndValues = nextBPlusNode.getKeyAndValue();
                keyAndValues.addAll(nextKeyAndValues);
                if (nextBPlusNode.isTail()) {
                    BPlusNode.setPreviousBPlusNode(null);
                } else {
                    nextBPlusNode.getNextBPlusNode().setPreviousBPlusNode(BPlusNode);
                    BPlusNode.setNextBPlusNode(nextBPlusNode.getNextBPlusNode());
                }

                KeyAndValue keyAndValue = getKeyAndValueinMinAndMax(parentBPlusNode, key, getMinKeyInBPlusNode(nextBPlusNode));
                delKeyAndValue(parentKeyAndValue, keyAndValue.getKey());
                if (parentKeyAndValue.isEmpty()) {
                    root = BPlusNode;
                    BPlusNode.setParantBPlusNode(null);
                } else {
                    //删除当前节点
                    childBPlusNodes.remove(nextBPlusNode);
                }
                Collections.sort(keyAndValues);
                merge(parentBPlusNode, key);
                return true;
            }
            //前节点和后节点都等于null那么是root节点
            return false;
        } else {
            preBPlusNode = getPreviousBPlusNode(BPlusNode);
            nextBPlusNode = getNextBPlusNode(BPlusNode);
            if (preBPlusNode != null) {
                //将前一个节点和当前节点还有父节点中的相应Key-value合并
                List<KeyAndValue> preKeyAndValues = preBPlusNode.getKeyAndValue();
                preKeyAndValues.addAll(keyAndValues);
                int min = getMaxKeyInBPlusNode(preBPlusNode);
                int max = getMinKeyInBPlusNode(BPlusNode);
                //父节点中移除这个key-value
                KeyAndValue keyAndValue = getKeyAndValueinMinAndMax(parentBPlusNode, min, max);
                parentKeyAndValue.remove(keyAndValue);
                if (parentKeyAndValue.isEmpty()) {
                    root = preBPlusNode;
                    BPlusNode.setParantBPlusNode(null);
                    preBPlusNode.setParantBPlusNode(null);
                } else {
                    childBPlusNodes.remove(BPlusNode);
                }
                assert nextBPlusNode != null;
                preBPlusNode.setNextBPlusNode(nextBPlusNode.getNextBPlusNode());
                //前节点加上一个当前节点的所有子节点中最小key的key-value
                KeyAndValue minKeyAndValue = getMinKeyAndValueInChildBPlusNode(BPlusNode);
                assert minKeyAndValue != null;
                KeyAndValue keyAndValue1 = new KeyAndValue(minKeyAndValue.getKey(), minKeyAndValue.getValue());
                preKeyAndValues.add(keyAndValue1);
                List<BPlusNode> preChildBPlusNodes = preBPlusNode.getBPlusNodes();
                preChildBPlusNodes.addAll(BPlusNode.getBPlusNodes());
                //将当前节点的孩子节点的父节点设为当前节点的后节点
                for (BPlusNode BPlusNode1 : childBPlusNodes1) {
                    BPlusNode1.setParantBPlusNode(preBPlusNode);
                }
                Collections.sort(preKeyAndValues);
                merge(parentBPlusNode, key);
                return true;
            }

            if (nextBPlusNode != null) {
                //将后一个节点和当前节点还有父节点中的相应Key-value合并
                List<KeyAndValue> nextKeyAndValues = nextBPlusNode.getKeyAndValue();
                nextKeyAndValues.addAll(keyAndValues);

                int min = getMaxKeyInBPlusNode(BPlusNode);
                int max = getMinKeyInBPlusNode(nextBPlusNode);
                //父节点中移除这个key-value
                KeyAndValue keyAndValue = getKeyAndValueinMinAndMax(parentBPlusNode, min, max);
                parentKeyAndValue.remove(keyAndValue);
                childBPlusNodes.remove(BPlusNode);
                if (parentKeyAndValue.isEmpty()) {
                    root = nextBPlusNode;
                    nextBPlusNode.setParantBPlusNode(null);
                } else {
                    childBPlusNodes.remove(BPlusNode);
                }
                nextBPlusNode.setPreviousBPlusNode(BPlusNode.getPreviousBPlusNode());
                //后节点加上一个当后节点的所有子节点中最小key的key-value
                KeyAndValue minKeyAndValue = getMinKeyAndValueInChildBPlusNode(nextBPlusNode);
                assert minKeyAndValue != null;
                KeyAndValue keyAndValue1 = new KeyAndValue(minKeyAndValue.getKey(), minKeyAndValue.getValue());
                nextKeyAndValues.add(keyAndValue1);
                List<BPlusNode> nextChildBPlusNodes = nextBPlusNode.getBPlusNodes();
                nextChildBPlusNodes.addAll(BPlusNode.getBPlusNodes());
                //将当前节点的孩子节点的父节点设为当前节点的后节点
                for (BPlusNode BPlusNode1 : childBPlusNodes1) {
                    BPlusNode1.setParantBPlusNode(nextBPlusNode);
                }
                Collections.sort(nextKeyAndValues);
                merge(parentBPlusNode, key);
                return true;
            }
            return false;
        }
    }

    //得到当前节点的前节点
    private BPlusNode getPreviousBPlusNode(BPlusNode BPlusNode) {
        if (BPlusNode.isRoot()) {
            return null;
        }

        BPlusNode parentBPlusNode = BPlusNode.getParantBPlusNode();
        //得到兄弟节点
        List<BPlusNode> BPlusNodes = parentBPlusNode.getBPlusNodes();
        List<KeyAndValue> keyAndValues = new ArrayList<>();
        for (BPlusNode n : BPlusNodes) {
            List<KeyAndValue> list = n.getKeyAndValue();
            int maxKeyAndValue = list.get(list.size() - 1).getKey();
            if (maxKeyAndValue < getMinKeyInBPlusNode(BPlusNode)) {
                keyAndValues.add(new KeyAndValue(maxKeyAndValue, n));
            }
        }
        Collections.sort(keyAndValues);
        if (keyAndValues.isEmpty()) {
            return null;
        }
        return (BPlusNode) keyAndValues.get(keyAndValues.size() - 1).getValue();
    }


    //得到当前节点的后节点
    private BPlusNode getNextBPlusNode(BPlusNode BPlusNode) {
        if (BPlusNode.isRoot()) {
            return null;
        }

        BPlusNode parentBPlusNode = BPlusNode.getParantBPlusNode();
        //得到兄弟节点
        List<BPlusNode> BPlusNodes = parentBPlusNode.getBPlusNodes();
        List<KeyAndValue> keyAndValues = new ArrayList<>();
        for (BPlusNode n : BPlusNodes) {
            List<KeyAndValue> list = n.getKeyAndValue();
            int minKeyAndValue = list.get(0).getKey();
            if (minKeyAndValue > getMaxKeyInBPlusNode(BPlusNode)) {
                keyAndValues.add(new KeyAndValue(minKeyAndValue, n));
            }
        }
        Collections.sort(keyAndValues);
        if (keyAndValues.isEmpty()) {
            return null;
        }
        return (BPlusNode) keyAndValues.get(0).getValue();
    }


    private int getMinKeyInBPlusNode(BPlusNode BPlusNode) {
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
        return keyAndValues.get(0).getKey();
    }

    private int getMaxKeyInBPlusNode(BPlusNode BPlusNode) {
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
        return keyAndValues.get(keyAndValues.size() - 1).getKey();
    }


    private int getLeftBoundOfKey(BPlusNode BPlusNode, int key) {
        int left = 0;
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
        for (int i = 0; i < keyAndValues.size(); i++) {
            if (keyAndValues.get(i).getKey() <= key && keyAndValues.get(i + 1).getKey() > key) {
                left = keyAndValues.get(i).getKey();
                break;
            }
        }
        return left;
    }

    private int getRightBoundOfKey(BPlusNode BPlusNode, int key) {
        int right = 0;
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
        for (int i = 0; i < keyAndValues.size(); i++) {
            if (keyAndValues.get(i).getKey() <= key && keyAndValues.get(i + 1).getKey() > key) {
                right = keyAndValues.get(i + 1).getKey();
                break;
            }
        }
        return right;
    }


    private void delKeyAndValue(List<KeyAndValue> keyAndValues, int key) {
        for (KeyAndValue keyAndValue : keyAndValues) {
            if (keyAndValue.getKey() == key) {
                keyAndValues.remove(keyAndValue);
                break;
            }
        }
    }

    //找到BPlusNode的键值对中在min和max中的键值对
    private KeyAndValue getKeyAndValueinMinAndMax(BPlusNode BPlusNode, int min, int max) {
        if (BPlusNode == null) {
            return null;
        }
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
        KeyAndValue keyAndValue = null;
        for (KeyAndValue k : keyAndValues) {
            if (k.getKey() > min && k.getKey() <= max) {
                keyAndValue = k;
                break;
            }
        }
        return keyAndValue;
    }


    private KeyAndValue getMinKeyAndValueInChildBPlusNode(BPlusNode BPlusNode) {
        if (BPlusNode.getBPlusNodes() == null || BPlusNode.getBPlusNodes().isEmpty()) {
            return null;
        }
        List<KeyAndValue> sortKeyAndValues = new ArrayList<>();
        List<BPlusNode> childBPlusNodes = BPlusNode.getBPlusNodes();
        for (BPlusNode childBPlusNode : childBPlusNodes) {
            List<KeyAndValue> keyAndValues = childBPlusNode.getKeyAndValue();
            KeyAndValue minKeyAndValue = keyAndValues.get(0);
            sortKeyAndValues.add(minKeyAndValue);
        }
        Collections.sort(sortKeyAndValues);
        return sortKeyAndValues.get(0);
    }

    private boolean isNeedMerge(BPlusNode BPlusNode) {
        if (BPlusNode == null) {
            return false;
        }
        List<KeyAndValue> keyAndValues = BPlusNode.getKeyAndValue();
        return keyAndValues.size() < rank / 2;
    }

    //判断一个节点是否有富余的键值对
    private boolean isMoreElement(BPlusNode BPlusNode) {
        return BPlusNode != null && (BPlusNode.getKeyAndValue().size() > rank / 2);
    }

    public static void main(String[] args) { 
        BPlusTree BPlusTree = new BPlusTree(4 );
        KeyAndValue keyAndValue = new KeyAndValue(1,"123");
        KeyAndValue keyAndValue1 = new KeyAndValue(2,"123");
        KeyAndValue keyAndValue2 = new KeyAndValue(3,"123");
        KeyAndValue keyAndValue3 = new KeyAndValue(4,"123");
        KeyAndValue keyAndValue4 = new KeyAndValue(5,"123");
        KeyAndValue keyAndValue5 = new KeyAndValue(6,"123");
        KeyAndValue keyAndValue6 = new KeyAndValue(7,"12300");
        KeyAndValue keyAndValue7 = new KeyAndValue(8,"546");
        KeyAndValue keyAndValue8 = new KeyAndValue(9,"123");
        KeyAndValue keyAndValue9 = new KeyAndValue(10,"123");
        KeyAndValue keyAndValue10 = new KeyAndValue(11,"123");
        KeyAndValue keyAndValue11 = new KeyAndValue(12,"123");
        KeyAndValue keyAndValue12 = new KeyAndValue(13,"123");
        KeyAndValue keyAndValue14 = new KeyAndValue(15,"12345");
        KeyAndValue keyAndValue15 = new KeyAndValue(16,"12345");
        KeyAndValue keyAndValue16 = new KeyAndValue(17,"12345");
        KeyAndValue keyAndValue17 = new KeyAndValue(18,"12345");
        KeyAndValue keyAndValue18 = new KeyAndValue(19,"12345");
        KeyAndValue keyAndValue19 = new KeyAndValue(20,"12345");
        KeyAndValue keyAndValue20 = new KeyAndValue(21,"12345");
        BPlusTree.insert(keyAndValue);
        BPlusTree.insert(keyAndValue5);
        BPlusTree.insert(keyAndValue9);
        BPlusTree.insert(keyAndValue1);
        BPlusTree.insert(keyAndValue7);
        BPlusTree.insert(keyAndValue10);
        BPlusTree.insert(keyAndValue17);
        BPlusTree.insert(keyAndValue2);
        BPlusTree.insert(keyAndValue14);
        BPlusTree.insert(keyAndValue16);
        BPlusTree.insert(keyAndValue11);
        BPlusTree.insert(keyAndValue12);
        BPlusTree.insert(keyAndValue3);
        BPlusTree.insert(keyAndValue8);
        BPlusTree.insert(keyAndValue18);
        BPlusTree.insert(keyAndValue15);
        BPlusTree.insert(keyAndValue4);
        BPlusTree.insert(keyAndValue19);
        BPlusTree.insert(keyAndValue6);
        BPlusTree.insert(keyAndValue20);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(1);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(0);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(2);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(11);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(3);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(4);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(5);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(9);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(6);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(13);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(7);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(10);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(18);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(8);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(12);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(20);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(19);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(15);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        BPlusTree.delete(17);
        BPlusTree.printBPlusTree(BPlusTree.getRoot());
        
    }
}