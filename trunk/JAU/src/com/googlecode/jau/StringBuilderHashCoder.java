package com.googlecode.jau;

/**
 * Computes hash code for a StringBuffer.
 */
class StringBuilderHashCoder implements HashCoder<StringBuilder> {
    @Override
    public int hashCode(StringBuilder obj) {
        return obj.toString().hashCode();
    }
}
