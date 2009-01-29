package com.googlecode.jau;

/**
 * Copier for StringBuffer.
 */
class StringBufferCopier implements Copier<StringBuffer> {
    @Override
    public void copy(StringBuffer a, StringBuffer b) {
        b.setLength(0);
        b.append(a);
    }
}
