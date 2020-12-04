package src.Stack.ArrayStack;

public class Array<E> {
    //叫它静态数组
    //private int[] data;
    private E[] data;
    private int size;
    //构造函数
    public Array(int capacity) {
        data = (E[])new Object[capacity];
        size = 0;
    }
    //无参数的构造函数，默认数组的容量为10
    public Array() {
        this(10);
    }
    public int getSize() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int getCapacity() {
        return data.length;
    }
    // O(1)
    public void addLast(E e) {
        add(size, e);
    }
    // O(n)
    public void addFirst(E e) {
        add(0, e);
    }
    // O(n/2) = O(n)
    public void add(int index, E e) {
        if(size>=data.length)
            resize(2 *data.length);
        if(index<0 || index>size)
            throw new IllegalArgumentException("Add failed.index is error.");
        for(int i=size-1;i>=index;i--) {
            data[i+1] = data[i];
        }
        data[index] = e;
        size++;
    }
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("Array: size = %d, capacity = %d\n", size, data.length));
        res.append("[");
        for(int i = 0 ; i<size ; i++) {
            res.append(data[i]);
            if(i != size - 1)
                res.append(", ");
        }
        res.append("]");
        return res.toString();
    }
    public E get(int index) {
        if(index < 0 || index >= size)
            throw new IllegalArgumentException("Get failed. Index is illegal");
        return data[index];
    }
    public E getFirst() {
        return get(size - 1);
    }
    public E getLast() {
        return get(0);
    }
    void set(int index, E e) {
        if(index < 0 || index >= size)
            throw new IllegalArgumentException("Get failed. Index is illegal");
        data[index] = e;
    }
    public boolean contains(E e) {
        for(int i = 0; i < size; i++) {
            if(data[i].equals(e))
                return true;
        }
        return false;
    }
    public int find(E e) {
        for(int i = 0; i < size; i++) {
            if(data[i].equals(e))
                return i;
        }
        return -1;
    }
    public E remove(int index) {
        if(index < 0 || index >= size)
            throw new IllegalArgumentException("Get failed. Index is illegal");
        E res = data[index];
        for(int i = index; i<size; i++) {
            data[i] = data[i+1];
        }
        size--;
        //释放空间，也可以不写
        //loitering objects != memory leak
        data[size] = null;
        if(size == data.length / 4 && data.length / 2 != 0)
            resize(data.length / 2);
        return res;
    }
    public E removeFirst() {
        return remove(0);
    }
    public E removeLast() {
        return remove(size-1);
    }
    //只删除了一个e，并不能保证删除了全部e
    public void removeElement(E e) {
        int index = find(e);
        if(index != -1)
            remove(index);
    }
    private void resize(int newCapacity) {
        E[] newData = (E[]) new Object[newCapacity];
        for(int i=0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }
}