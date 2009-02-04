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
public class IntFields8 {
    public int a0;
    public int a1;
    public int a2;
    public int a3;
    public int a4;
    public int a5;
    public int a6;
    public int a7;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntFields8 other = (IntFields8) obj;
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
        if (this.a4 != other.a4) {
            return false;
        }
        if (this.a5 != other.a5) {
            return false;
        }
        if (this.a6 != other.a6) {
            return false;
        }
        if (this.a7 != other.a7) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.a0;
        hash = 97 * hash + this.a1;
        hash = 97 * hash + this.a2;
        hash = 97 * hash + this.a3;
        hash = 97 * hash + this.a4;
        hash = 97 * hash + this.a5;
        hash = 97 * hash + this.a6;
        hash = 97 * hash + this.a7;
        return hash;
    }
}
