class Node<E> {
    E data;
    Node<E> left;
    Node<E> right;
    Node<E> parent;


    public Node(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
    }

    public void set(E data) {
        this.data = data;
    }

    public E get() {
        return data;
    }

    public Node<E> getLeft() {
        return left;
    }

    public Node<E> getRight() {
        return right;
    }

    public Node<E> getParent() {
        return parent;
    }

    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }

    public void setParent(Node<E> parent) {
        this.parent = parent;
    }
}