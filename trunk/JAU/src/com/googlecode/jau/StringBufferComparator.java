package com.googlecode.jau;

import java.util.Comparator;

/**
 * Comparator for StringBuffer.
 */
class StringBufferComparator implements Comparator<StringBuffer> {
    @Override
    public int compare(StringBuffer o1, StringBuffer o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
