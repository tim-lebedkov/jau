package com.googlecode.jau;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * .equals() for maps.
 * Less or greater than 0 values returned by <code>compare</code> are
 * meaningless.
 */
class MapComparator<K, V, T extends java.util.Map<K, V>>
        implements Comparator<T> {
    /** Comparator for maps. Can only be used for .equals(). */
    public static final MapComparator INSTANCE = new MapComparator();

    @Override
    public int compare(T a, T o) {
        Map<K, V> m = (Map<K, V>) o;
        if (m.size() != a.size()) {
            return -1;
        }

        try {
            Iterator<Entry<K, V>> i = a.entrySet().iterator();
            while (i.hasNext()) {
                Entry<K, V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key))) {
                        return -1;
                    }
                } else {
                    if (!JAU.equals(value, m.get(key))) {
                        return -1;
                    }
                }
            }
        } catch (ClassCastException unused) {
            return -1;
        } catch (NullPointerException unused) {
            return -1;
        }

        return 0;
    }
}
