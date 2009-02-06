package com.googlecode.jau;

import java.util.Iterator;
import java.util.Map.Entry;

/**
 * HashCoder for a map.
 */
class MapHashCoder<K, V, T extends java.util.Map<K, V>>
        implements HashCoder<T> {
    /** computes hashCode() for a Map */
    public static final MapHashCoder INSTANCE = new MapHashCoder();

    @Override
    public int hashCode(T map) {
        int h = 0;
        Iterator<Entry<K,V>> i = map.entrySet().iterator();
        while (i.hasNext()) {
            Entry<K, V> next = i.next();
            Object key = next.getKey();
            Object value = next.getValue();
            int hc = (key == null ? 0 : JAU.hashCode(key)) ^
                (value == null ? 0 : JAU.hashCode(value));
            h += hc;
        }
        return h;
    }
}
