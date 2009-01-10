package com.googlecode.jau;

@JAUEquals
@JAUHashCode
@JAUToString
public class PrivateField {
    private int value;

    public void setValue(int value) {
        this.value = value;
    }
}
