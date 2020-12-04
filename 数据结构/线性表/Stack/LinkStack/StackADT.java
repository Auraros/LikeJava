package src.Stack.LinkStack;

public interface StackADT<T> {
    //压栈方法
    public void push(T element);
    //弹栈方法
    public T pop();
    //查看栈顶元素方法
    public T peek();
    //栈判空方法
    public boolean isEmpty();

}