import java.io.Serializable;

public class MyHashSet<E> implements Serializable {
    Object[] hashArray = new Object[100000];
    int size = 0;

    public boolean add(E element) {
        int location = element.hashCode();
        if (hashArray[location] == null) {
            hashArray[location] = element;
            size++;
            return true;
        }
        return false;
    }

    public void clear() {
        hashArray = new Object[100000];
    }

    @SuppressWarnings("unchecked")
    public DLList<E> toDLList() {
        DLList<E> list = new DLList<E>();

        for (Object each : hashArray) {
            if (each != null) {
                list.add((E) (each));
            }
        }
        return list;
    }

    public boolean contains(Object obj) {
        int location = obj.hashCode();
        if (hashArray[location] == null) {
            return false;
        }
        return true;
    }

    public boolean remove(Object obj) {
        int location = obj.hashCode();
        if (hashArray[location] != null) {
            hashArray[location] = null;
            size--;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String concatenation = "";
        for (int i = 0; i < hashArray.length; i++) {
            if (hashArray[i] != null) {
                concatenation += hashArray[i].toString() + "\n";
            }
        }
        return concatenation;
    }

    public int size() {
        return size;
    }
}