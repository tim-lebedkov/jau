package com.googlecode.jau;

/**
 * Interface for JAU implementations.
 */
interface JAUInterface {
    /**
     * Compares 2 objects using reflection.
     *
     * @param cil class information
     * @param a first object
     * @param b second object
     * @return true = objects are equals
     */
    public boolean equals(ClassInfo cil, Object a, Object b);

    /**
     * Updates ClassInfo (caches some information)
     *
     * @param ci class information
     */
    public void update(ClassInfo ci);
}
