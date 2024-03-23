import java.io.Serializable;

public class MyHashTable<K, V> implements Serializable {

    private DLList<V>[] table;
    private MyHashSet<K> keySet;

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        table = new DLList[100000];
        keySet = new MyHashSet<K>();
    }

    public void put(K key, V value) {
        int index = key.hashCode() % table.length;
        if (table[index] == null) {
            table[index] = new DLList<V>();
        }
        table[index].add(value);
        keySet.add(key);
    }

    public DLList<V> get(K key) {
        int index = key.hashCode() % table.length;
        if (table[index] == null) {
            return null;
        }
        return table[index];
    }

    public MyHashSet<K> keySet() {
        return keySet;
    }

    public String toString() {
        // Return a string representation of the hash table keys and size of value.
        String result = "";
        DLList<K> keyList = keySet.toDLList();
        for (int i = 0; i < keyList.size(); i++) {
            K key = keyList.get(i);
            result += key.toString() + " " + get(key).size() + "\n";
        }

        return result;
    }

    public void remove(K k, V v) {
        int index = k.hashCode() % table.length;
        if (table[index] == null) {
            return;
        }
        table[index].remove(v);
        if (table[index].size() == 0) {
            table[index] = null;
            keySet.remove(k);
        }
    }

    public void remove(K k) {
        int index = k.hashCode() % table.length;
        if (table[index] == null) {
            return;
        }
        table[index] = null;
        keySet.remove(k);
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        table = new DLList[100000];
        keySet.clear();
    }

}