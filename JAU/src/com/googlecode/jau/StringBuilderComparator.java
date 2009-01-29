package com.googlecode.jau;

import java.util.Comparator;

/**
 * Comparator for StringBuilder.
 */
class StringBuilderComparator implements Comparator<StringBuilder> {
    @Override
    public int compare(StringBuilder o1, StringBuilder o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
