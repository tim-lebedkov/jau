package com.googlecode.jau;

/**
 * Copies fields from one object to another.
 */
public interface Copier<T> {
    /**
     * Copies content of one object to another. Field values are deeply
     * cloned and put into another object.
     *
     * @param a source
     * @param b target
     */
    public void copy(T a, T b);
}
