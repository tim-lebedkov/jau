package com.googlecode.jau;

/**
 * Collection of classes with increasing number of integer fields.
 */
@JAUEquals
@JAUCompareTo
@JAUHashCode
@JAUToString
@JAUCopy
@JAUToMap
public class IntFields2 {
    public int a0;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntFields2 other = (IntFields2) obj;
        if (this.a0 != other.a0) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.a0;
        return hash;
    }
}
