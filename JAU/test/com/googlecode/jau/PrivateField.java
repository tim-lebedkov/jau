package com.googlecode.jau;

@JAUEquals
@JAUCompareTo
@JAUHashCode
@JAUToString
@JAUCopy
@JAUToMap
public class PrivateField {
    private int value;

    public void setValue(int value) {
        this.value = value;
    }
}
