package com.googlecode.jau;

/**
 * Computes hash code for an object.
 */
public interface HashCoder<T> {
    /**
     * Computes a hash code
     *
     * @param obj an object
     * @return computed hash code
     */
    public int hashCode(T obj);
}
