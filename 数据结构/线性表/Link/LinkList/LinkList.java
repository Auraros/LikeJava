package src.Link;


class ListNode<T> {

    private int foot;		//根节点索引位置
    private int count;		//代表链表程度
    private Node root;		//标识根节点

    //链接点类,内部方法实现，外部使用
    private class Node{

        private T data;		//数据信息
        private Node next;	//下一个节点引用

        public Node(T data) {
            this.data = data;
        }

        //添加节点
        private void add(T data){
            if(this.next == null){
                this.next = new Node(data);		//如果当前节点的next为null,直接创建一个新的节点
            }else {
                this.next.add(data);			//否则进行递归调用，直到最后在某个为空的节点创建一个新节点
            }
        }

        //删除节点1
        public void remove(Node previous, int index){

            if(ListNode.this.foot++ == index){
                previous.next = this.next;	//this表示当前要删除的节点
                this.next = null;
                ListNode.this.count--;
                return;
            }else{
                this.next.remove(this,index);	//递归删除
            }

        }

        //删除节点2
        public void remove(Node previous, T data){
            if(this.data.equals(data)){
                previous.next = this.next;
                this.next = null;
                ListNode.this.count--;
                return ;
            }else{
                if(this.next != null){
                    this.next.remove(this,data);
                }else{
                    return;
                }
            }
        }

        //修改数据  -- 新数据替换旧数据
        public void replace(T oldData,T newData){
            if(this.data.equals(newData)){
                this.data = newData;
            }else{
                this.next.replace(oldData, newData);  //递归修改，寻找当前节点下一个节点，直到某个节点的值匹配入参
            }

        }

        //修改数据  -- 利用索引修改
        public void replace(int index,T newData){
            if(ListNode.this.foot++ == index){	//找到了某个值的索引和传入的索引相同，直接替换
                this.data = newData;
            }else{
                this.next.replace(index, newData);
            }
        }

        //查询
        public T get(int index){
            if(ListNode.this.foot++ == index){
                return this.data;
            }else{
                return this.next.get(index);
            }
        }

        //链表是否包含某个节点
        public boolean contains(T data){
            if(this.data.equals(data)){	//如果当前的这个data正好和传入的data匹配
                return true;
            }else{
                //如果当前的这个不匹配，则需要查找下一个节点
                if(this.next == null){
                    return false;
                }else{
                    return this.next.contains(data);
                }
            }
        }

    }

    public ListNode() {

    }

    //检查链表是否为空
    public boolean isEmpty(){
        if(count == 0 || this.root == null){
            return true;
        }else{
            return false;
        }
    }

    //获取链表的长度
    public int size(){
        return this.count;
    }

    //添加
    public void add(T data){
        if(this.isEmpty()){ //如果链表为空，新建一个节点
            this.root = new Node(data);
        }else{
            this.root.add(data);
        }
        this.count++;
    }

    //删除  -- 按照索引删除
    public void remove(int index){
        if(this.isEmpty()){
            return;
        }
        if(index < 0 || this.count <= index){
            return ;
        }
        if(index == 0){	//想要删除根节点
            Node temp = this.root;
            this.root = this.root.next;
            temp.next = null;
            this.count--;
            return ;
        }else{
            this.foot = 0;
            this.root.remove(this.root, index);
        }
    }

    //根据传入的数值删除
    public void remove(T data){
        if(this.isEmpty()){
            return;
        }
        if(this.root.data.equals(data)){	//如果删除的正好是根节点
            Node temp = this.root;
            this.root = this.root.next;
            temp.next = null;
            this.count--;
            return ;
        }else{
            this.root.remove(this.root, data);
        }
    }

    //修改  -- 根据索引修改
    public void replace(int index,T newData){
        if(this.isEmpty()){
            return;
        }
        if(index < 0 || this.count <= index){
            return ;
        }
        this.foot = 0;
        this.root.replace(index, newData);
    }

    //修改 -- 新老数据替换
    public void replace(T oldData,T newData){
        if(this.isEmpty()){
            return;
        }
        this.root.replace(oldData, newData);
    }

    //查询 --- 根据索引查找
    public T get(int index){
        if(this.isEmpty()){
            return null;
        }
        this.foot = 0;
        return this.root.get(index);
    }

    //是否包含
    public boolean contains(T data){
        if(this.isEmpty()){
            return false;
        }
        return this.root.contains(data);
    }

    //打印  toArray
    public Object[] toArray(){
        if(this.isEmpty()){
            return null;
        }
        int count = this.count;
        Object[] retVal = new Object[count];
        for(int i=0;i<count;i++){
            retVal[i] = this.get(i);
        }
        return retVal;
    }

    public static void main(String[] args) {
        ListNode<String> myList = new ListNode<String>();
        myList.add("a");
        myList.add("b");
        myList.add("c");
        myList.add("d");
        myList.add("e");
        myList.add("f");
        System.out.println("第三个元素是:" + myList.get(3));
        myList.remove(3);
        System.out.println("删除之后，第三个元素是:"+myList.get(3));

        System.out.println("-----------替换之后--------");

        myList.replace(1, "b11");
        System.out.println(myList.get(1));

    }

}
