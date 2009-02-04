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
public class IntFields1 {
    public int a0;
    public int a1;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntFields1 other = (IntFields1) obj;
        if (this.a0 != other.a0) {
            return false;
        }
        if (this.a1 != other.a1) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.a0;
        hash = 23 * hash + this.a1;
        return hash;
    }
}
