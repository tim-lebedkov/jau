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
public class IntFields16 {
    public int a0;
    public int a1;
    public int a2;
    public int a3;
    public int a4;
    public int a5;
    public int a6;
    public int a7;
    public int a8;
    public int a9;
    public int a10;
    public int a11;
    public int a12;
    public int a13;
    public int a14;
    public int a15;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntFields16 other = (IntFields16) obj;
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
        if (this.a8 != other.a8) {
            return false;
        }
        if (this.a9 != other.a9) {
            return false;
        }
        if (this.a10 != other.a10) {
            return false;
        }
        if (this.a11 != other.a11) {
            return false;
        }
        if (this.a12 != other.a12) {
            return false;
        }
        if (this.a13 != other.a13) {
            return false;
        }
        if (this.a14 != other.a14) {
            return false;
        }
        if (this.a15 != other.a15) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.a0;
        hash = 67 * hash + this.a1;
        hash = 67 * hash + this.a2;
        hash = 67 * hash + this.a3;
        hash = 67 * hash + this.a4;
        hash = 67 * hash + this.a5;
        hash = 67 * hash + this.a6;
        hash = 67 * hash + this.a7;
        hash = 67 * hash + this.a8;
        hash = 67 * hash + this.a9;
        hash = 67 * hash + this.a10;
        hash = 67 * hash + this.a11;
        hash = 67 * hash + this.a12;
        hash = 67 * hash + this.a13;
        hash = 67 * hash + this.a14;
        hash = 67 * hash + this.a15;
        return hash;
    }
}
