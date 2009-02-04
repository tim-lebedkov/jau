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
public class IntFields4 {
    public int a0;
    public int a1;
    public int a2;
    public int a3;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntFields4 other = (IntFields4) obj;
        if (this.a0 != other.a0) {
            return false;
        }
        if (this.a1 != other.a1) {
            return false;
        }
        if (this.a2 != other.a2) {
            return false;
        }
        if (this.a3 != other.a3) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.a0;
        hash = 97 * hash + this.a1;
        hash = 97 * hash + this.a2;
        hash = 97 * hash + this.a3;
        return hash;
    }
}
