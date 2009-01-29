package com.googlecode.jau;

/**
 * Computes hash code for a StringBuffer.
 */
class StringBufferHashCoder implements HashCoder<StringBuffer> {
    @Override
    public int hashCode(StringBuffer obj) {
        return obj.toString().hashCode();
    }
}
