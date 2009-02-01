package com.googlecode.jau;

/**
 * Computes .toString() for a class
 */
public interface Stringifier<T> {
    /**
     * Computes .toString() for an object
     *
     * @param a an object
     * @return string representation
     */
    public String toString(T a);
}
