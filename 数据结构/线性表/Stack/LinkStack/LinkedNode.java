package src.Stack.LinkStack;

public class LinkedNode<T> {
    private T element;
    private LinkedNode<T> next;

    //构造方法一
    public LinkedNode() {
        next = null;
        element = null;
    }
    //构造方法二
    public LinkedNode(T element) {
        this.element = element;
    }

    public void setNext(LinkedNode<T> next) {
        this.next = next;
    }

    public LinkedNode<T> getNext() {
        return next;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }
}