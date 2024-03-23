public class DLList<E> {
    Node<E> Head;
    Node<E> Tail;
    int size;

    public DLList() {
        Head = new Node<E>(null);
        Tail = new Node<E>(null);
        size = 0;
    }

    public Node<E> getNode(int index) {
        Node<E> current = Head.next();
        for (int i = 0; i < index; i++) {
            current = current.next();
        }
        return current;
    }

    public E get(int index) {
        return getNode(index).get();
    }

    public boolean add(E element) {
        Node<E> newNode = new Node<E>(element);
        if (size == 0) {
            Head.setNext(newNode);
            Tail.setPrevious(newNode);
            newNode.setPrevious(Head);
            newNode.setNext(Tail);
        } else {
            newNode.setPrevious(Tail.previous());
            newNode.setNext(Tail);
            Tail.previous().setNext(newNode);
            Tail.setPrevious(newNode);
        }
        size++;
        return true;
    }

    public boolean add(E element, int index) {
        Node<E> newNode = new Node<E>(element);
        if (index == 0) {
            newNode.setNext(Head.next());
            newNode.setPrevious(Head);
            Head.next().setPrevious(newNode);
            Head.setNext(newNode);
        } else if (index == size) {
            newNode.setPrevious(Tail.previous());
            newNode.setNext(Tail);
            Tail.previous().setNext(newNode);
            Tail.setPrevious(newNode);
        } else {
            newNode.setPrevious(getNode(index - 1));
            newNode.setNext(getNode(index));
            getNode(index - 1).setNext(newNode);
            getNode(index).setPrevious(newNode);
        }
        size++;
        return true;
    }

    public E remove(int index) {
        Node<E> current = getNode(index);
        current.previous().setNext(current.next());
        current.next().setPrevious(current.previous());
        size--;
        return current.get();
    }

    public void remove(E element) {
        Node<E> current = Head.next();
        for (int i = 0; i < size; i++) {
            if (current.get().equals(element)) {
                current.previous().setNext(current.next());
                current.next().setPrevious(current.previous());
                size--;
                break;
            }
            current = current.next();
        }
    }

    public void shuffle() {
        for (int i = 0; i < size; i++) {
            int random = (int) (Math.random() * size);
            E temp = get(i);
            set(get(random), i);
            set(temp, random);
        }
    }

    public void set(E element, int index) {
        getNode(index).set(element);
    }

    public int size() {
        return size;
    }

    public void clear() {
        Head.setNext(Tail);
        Tail.setPrevious(Head);
        size = 0;
    }

    public String toString() {
        String result = "[";
        for (int i = 1; i < size; i++) {
            result += get(i);
            if (i != size - 1) {
                result += ", ";
            }
        }
        result += "]";
        return result;
    }

}
