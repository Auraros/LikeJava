package src.Link;

@SuppressWarnings("unchecked")

public class ArrayList<AnyType> {


    //声明一个Object类型的数组
    private Object[] elementDate;

    //声明一个数组内元素个数
    private int size;

    public int size() {
        return size;
    }

    //定义一个无参构造函数,初始化数组内个数
    public ArrayList(){
        this(10);//调用另一个构造方法(有参构造方法)，所以，必须有一个有参构造方法
    }

    public ArrayList(int initialCapacity) {//有参构造方法，实现数组容量的初始化
        if(initialCapacity<0) {
            try {
                throw new Exception();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        elementDate=new Object[initialCapacity];//初始化，容量为10    与前面的数组声明对应
    }
    //重点***********
    public void add(Object obj) {
        //首先，判断数组是否装满，是则扩容
        elementDate[size++]=obj;
        if(size==elementDate.length) {
            //创建一个新数组
            Object[] newArray=new Object[2*size+1];
            //将老数组拷贝到新数组内
            System.arraycopy(elementDate, 0, newArray, 0, elementDate.length);
            //再将新数组赋值给老数组
            elementDate=newArray;
        }
    }

    //get方法，得到数组实质就是一个数组的索引操作
    public Object get(int index) {
        //检查下标是否越界
        rangCheck(index);
        return elementDate[index];

    }

    public void remove(int index){
        rangCheck(index);

        //删除指定位置对象，删除某位置，相当于 将后往前挪:
        int numMoved = size-index-1;
        if(numMoved>0){
            System.arraycopy(elementDate, index+1, elementDate, index, numMoved);
        }
    }
    public void remove(Object obj){
        for(int i=0;i<size;i++){
            if(get(i).equals(obj)){   //注意底层调用的equals方法而不是==。
                remove(i);
            }
        }
    }

    private void rangCheck(int index) {
        if(index<0||index>size) {
            try {
                throw new Exception();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void add(int index,Object obj) {
        rangCheck(index);
        ensureCapacity();//扩容
        System.arraycopy(elementDate,index,elementDate, index+1, size-index);
        elementDate[index]=obj;
        size++;
    }

    private void ensureCapacity() {
        if(size==elementDate.length) {
            Object[] newarray=new Object[size*2+1];
            System.arraycopy(elementDate, 0, newarray, 0, elementDate.length);
            elementDate=newarray;
        }

    }

    public static void main(String[] args) {
        ArrayList list=new ArrayList();
        list.add("温暖");
        list.add("依然");
        list.add("wk");
        list.add("wk1");
        list.add("wk2");
        System.out.println("list内拥有："+list.size()+"个元素");
    }
}

