import java.io.Serializable;

public class Node<E> implements Serializable {
    private E data;
    private Node<E> next;
    private Node<E> prev;

    public Node(E obj) {
        data = obj;
        next = null;
        prev = null;
    }

    public E get() {
        return data;
    }

    public Node<E> next() {
        return next;
    }

    public Node<E> prev() {
        return prev;
    }

    public void setNext(Node<E> node) {
        next = node;
    }

    public void setPrev(Node<E> node) {
        prev = node;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        Node<E> node = (Node<E>) obj;
        return data.equals(node.get());
    }
}