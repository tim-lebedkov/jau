package com.googlecode.jau;

@JAUEquals
@JAUCompareTo
@JAUHashCode
@JAUToString
final class IncludeFalse {
    @JAUEquals(include = false)
    @JAUCompareTo(include = false)
    @JAUHashCode(include = false)
    @JAUToString(include = false)
    public int value;
}
