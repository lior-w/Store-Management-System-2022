package DataAccessLayer;


import java.util.HashMap;
import java.util.Map;

public class IdentityMap<T> {
    private final Map<Integer, T> cache;

    public IdentityMap() {
        cache = new HashMap<>();
    }

    public T retrieve(int key) {
        if (cache.containsKey(key))
            return cache.get(key);
        return null;
    }

    public void cache(int key, T value) {
        cache.put(key, value);
    }

    public boolean contains(int key) {
        return cache.containsKey(key);
    }

    public void remove(int key) {
        cache.remove(key);
    }
}
