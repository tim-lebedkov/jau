package com.googlecode.jau;

@JAUEquals
@JAUHashCode
@JAUToString
final class IncludeFalse {
    @JAUEquals(include = false)
    @JAUHashCode(include = false)
    @JAUToString(include = false)
    public int value;
}
