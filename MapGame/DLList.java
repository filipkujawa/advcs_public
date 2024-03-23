import java.io.Serializable;

public class DLList<E> implements Serializable {

    private Node<E> head;
    private Node<E> tail;
    private int size;
    private String concatenation;

    public DLList() {
        head = new Node<E>(null);
        tail = new Node<E>(null);
        head.setNext(tail);
        head.setPrev(null);
        tail.setNext(null);
        tail.setPrev(head);
        size = 0;
    }

    private Node<E> getNode(int loc) {
        Node<E> curNode;
        if (loc < size / 2) {
            curNode = head.next();
            for (int i = 0; i < size; i++) {
                if (i == loc)
                    return curNode;
                curNode = curNode.next();
            }
        } else {
            curNode = tail.prev();
            for (int i = size - 1; i >= 0; i--) {
                if (i == loc)
                    return curNode;
                curNode = curNode.prev();
            }
        }

        return null;
    }

    public void add(E data) {
        Node<E> newNode = new Node<E>(data);

        newNode.setPrev(tail.prev());
        newNode.prev().setNext(newNode);
        tail.setPrev(newNode);
        newNode.setNext(tail);

        size++;
    }

    public void add(int loc, E data) {
        Node<E> node = getNode(loc);
        Node<E> newNode = new Node<E>(data);
        node.prev().setNext(newNode);
        newNode.setPrev(node.prev());
        newNode.setNext(node);
        node.setPrev(newNode);
        size++;
    }

    public E get(int index) {
        Node<E> node = getNode(index);
        try {
            return node.get();
        } catch (Exception e) {
            return null;
        }
    }

    public void set(int loc, E data) {
        Node<E> node = getNode(loc);
        Node<E> newNode = new Node<E>(data);
        node.prev().setNext(newNode);
        node.next().setPrev(newNode);
        newNode.setPrev(node.prev());
        newNode.setNext(node.next());
    }

    public void remove(int index) {
        Node<E> node = getNode(index);
        node.prev().setNext(node.next());
        node.next().setPrev(node.prev());
        size--;
    }

    public void remove(E el) {
        Node<E> node = findNodeWithElement(el);
        node.prev().setNext(node.next());
        node.next().setPrev(node.prev());
        size--;
    }

    private Node<E> findNodeWithElement(E el) {
        Node<E> curNode = head.next();
        for (int i = 0; i < size; i++) {
            if (curNode.get().equals(el))
                return curNode;
            curNode = curNode.next();
        }
        return null;
    }

    public int findIndexWithElement(E el) {
        Node<E> curNode = head.next();
        for (int i = 0; i < size; i++) {
            if (curNode.get().equals(el))
                return i;
            curNode = curNode.next();
        }
        return -1;
    }

    public int size() {
        return size;
    }

    public boolean empty() {
        return size == 0;
    }

    public String toString() {
        concatenation = "";
        Node<E> currNode = head.next();
        for (int index = 0; index < size; index++) {
            concatenation += currNode.get() + ", ";
            currNode = currNode.next();
        }
        return concatenation;
    }

    public void clear() {
        head.setNext(tail);
        tail.setPrev(head);
        size = 0;
    }
}
