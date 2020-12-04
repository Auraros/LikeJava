package src.Link;

@SuppressWarnings("unchecked")

public class ArrayList<T> {


    //声明一个Object类型的数组,默认长度为10
    private T[] elementArray;

    //声明一个数组内元素个数
    private int size;

    public int size() {
        return size;
    }

    //定义一个无参构造函数,初始化数组内个数
    public ArrayList(){
        this(10); //调用另一个构造方法(有参构造方法)，所以，必须有一个有参构造方法
    }

    public ArrayList(int initialCapacity) {  //有参构造方法，实现数组容量的初始化，默认长度为10
        if (initialCapacity < 0){
            try{
                throw new Exception();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        elementArray = (T[]) new Object[initialCapacity];  //初始化10个
    }


    public boolean add(T x) {
        add(size(), x);
        return true;
    }

    //get方法，得到数组实质就是一个数组的索引操作
    public T get(int index) {
        if(index < 0 || index >= size()){
            throw new ArrayIndexOutOfBoundsException();
        }
        return elementArray[index];
    }

    public T remove(int index){
        T removeValue = elementArray[index];
        for(int i=index; i > size()-1; i++){
            elementArray[i] = elementArray[i+1];
        }
        size--;
        return removeValue;
    }

    public int contains(T x){
        for (int i=0; i<size(); i++){
            if(elementArray[i] == x){
                System.out.println(x+"存在");
                return i;
            }
        }
        return -1;
    }



    public void remove(T x){
        int index = contains(x);
        remove(index);
    }


    public void add(int index,T x) {
        if(elementArray.length == size()){
            ensureCapacity(size()*2+1);
        }
        for(int i = size(); i>index; i--){
            elementArray[i] = elementArray[i-1];
        }
        elementArray[index] = x;

        size++;
    }

    private void ensureCapacity(int newCapCity) {
        if(newCapCity < size){
            return;
        }
        T[] old = elementArray;
        elementArray = (T[])new Object[newCapCity];
        for(int i=0; i<size(); i++){
            elementArray[i] = old[i];
        }
    }

//    public static void main(String[] args) {
//        ArrayList list=new ArrayList();
//        list.add("a");
//        list.add("b");
//        list.add(123);
//        list.add("c");
//        list.add("d");
//
//        list.contains("a");
//
//        list.remove(2);
//        list.remove("a");
//        list.get(2);
//
//        System.out.println("list内拥有："+list.size()+"个元素");
//    }
}

