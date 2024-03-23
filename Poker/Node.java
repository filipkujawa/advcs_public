public class Node<E> {
    private E data;
    private Node<E> next;
    private Node<E> previous;

    public Node(E data) {
        this.data = data;
        next = null;
        previous = null;
    }

    public E get() {
        return data;
    }

    public void set(E element) {
        data = element;
    }

    public Node<E> next() {
        return next;
    }

    public Node<E> previous() {
        return previous;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public void setPrevious(Node<E> previous) {
        this.previous = previous;
    }

    public String toString() {
        return data.toString();
    }
}