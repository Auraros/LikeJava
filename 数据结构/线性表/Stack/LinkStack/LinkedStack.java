package src.Stack.LinkStack;

public class LinkedStack<T> implements StackADT<T> {
    private int count;
    private LinkedNode<T> top;

    public LinkedStack() {
        count = 0;
        top = null;
    }
    //压栈方法
    @Override
    public void push(T element) {
        LinkedNode<T> temp = new LinkedNode<T> (element);
        temp.setNext(top);
        top = temp;
        count++;
    }

    //弹栈方法
    @Override
    public T pop() throws EmptyCollectionException{
        if (isEmpty())
            throw new EmptyCollectionException("Stack");
        T temp = top.getElement();
        top = top.getNext();
        count--;
        return temp;
    }
    //查看栈顶元素方法
    @Override
    public T peek() throws EmptyCollectionException{
        if (isEmpty())
            throw new EmptyCollectionException("Stack");
        T temp = top.getElement();
        return temp;
    }
    //栈判空方法
    @Override
    public boolean isEmpty() {
        if (count <= 0)
            return true;
        else
            return false;
    }

}

//新建异常类
class EmptyCollectionException extends RuntimeException{
    public EmptyCollectionException(String collection) {
        super("The "+collection+" is empty.");
    }
}