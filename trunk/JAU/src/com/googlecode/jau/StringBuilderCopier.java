package com.googlecode.jau;

/**
 * Copier for StringBuilder.
 */
class StringBuilderCopier implements Copier<StringBuilder> {
    @Override
    public void copy(StringBuilder a, StringBuilder b) {
        b.setLength(0);
        b.append(a);
    }
}
