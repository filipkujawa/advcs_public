class BinaryTree<E extends Comparable<E>> {
    Node<E> root;
    private int passes;

    public BinaryTree() {
        root = null;
    }

    public void remove(E data) {
        remove(data, root);
    }

    private Node<E> remove(E data, Node<E> current) {
        if (current == null) {
            return null;
        }

        int comparisonResult = data.compareTo(current.get());

        if (comparisonResult < 0) {
            current.setLeft(remove(data, current.getLeft()));
        } else if (comparisonResult > 0) {
            current.setRight(remove(data, current.getRight()));
        } else {

            if (current.getLeft() == null) {
                return current.getRight();
            } else if (current.getRight() == null) {
                return current.getLeft();
            }
            current.set(findMin(current.getRight()));
            current.setRight(remove(current.get(), current.getRight()));
        }

        return current;
    }

    private E findMin(Node<E> node) {
        // Find the leftmost leaf node
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node.get();
    }

    public void add(E data) {
        passes = 0; // Reset the number of passes
        root = add(data, root);
    }

    private Node<E> add(E data, Node<E> current) {
        passes++; // Increment the number of passes

        if (current == null) {
            return new Node<E>(data);
        } else if (data.compareTo(current.get()) < 0) {
            Node<E> leftChild = add(data, current.getLeft());
            current.setLeft(leftChild);
            leftChild.setParent(current);
        } else if (data.compareTo(current.get()) > 0) {
            Node<E> rightChild = add(data, current.getRight());
            current.setRight(rightChild);
            rightChild.setParent(current);
        }

        return current;
    }

    public String toString() {
        return inOrderString(root);
    }

    public String inOrderString(Node<E> current) {
        String s = "";
        if (current != null) {
            s = s + inOrderString(current.getLeft());
            s = s + current.get().toString() + "\n";
            s = s + inOrderString(current.getRight());
        }

        return s;
    }

    public String toStringPreOrder() {
        return preOrderString(root);
    }

    public String preOrderString(Node<E> current) {
        String s = "";
        if (current != null) {
            s = s + current.get().toString() + " ";
            s = s + preOrderString(current.getLeft());
            s = s + preOrderString(current.getRight());
        }

        return s;

    }

    public boolean contains(E data) {
        return contains(data, root);
    }

    private boolean contains(E data, Node<E> current) {
        if (current == null) {
            return false;
        } else if (data.compareTo(current.get()) < 0) {
            return contains(data, current.getLeft());
        } else if (data.compareTo(current.get()) > 0) {
            return contains(data, current.getRight());
        } else {
            return true;
        }
    }

    public void clear() {
        root = null;
    }

    public E get(E data) {
        passes = 0; // Reset the passes counter
        return get(data, root);
    }

    private E get(E data, Node<E> current) {
        if (current == null) {
            return null;
        } else if (data.compareTo(current.get()) < 0) {
            passes++; // Increment the passes counter
            return get(data, current.getLeft());
        } else if (data.compareTo(current.get()) > 0) {
            passes++; // Increment the passes counter
            return get(data, current.getRight());
        } else {
            passes++; // Increment the passes counter
            return current.get();
        }
    }


    public int getPasses() {
        return passes;
    }

    public void resetPasses() {
        passes = 0;
    }

}