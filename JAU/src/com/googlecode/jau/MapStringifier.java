package com.googlecode.jau;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Stringifier for java.util.Map
 */
class MapStringifier<K, V> implements Stringifier<Map<K, V>> {
    /** an instance of this class */
    public static MapStringifier INSTANCE = new MapStringifier();

    @Override
    public String toString(Map a) {
        Iterator<Entry<K,V>> i = a.entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Entry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key == this ? "(this Map)" : JAU.toString(key));
            sb.append('=');
            sb.append(value == this ? "(this Map)" : JAU.toString(value));
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(", ");
        }
    }
}
