package com.googlecode.jau;

@JAUEquals
@JAUCompareTo
@JAUHashCode
@JAUToString
@JAUCopy
// this class should *not* be public to test a special case where
// the class does not seem to have a constructor
final class IncludeFalse {
    @JAUEquals(include = false)
    @JAUCompareTo(include = false)
    @JAUHashCode(include = false)
    @JAUToString(include = false)
    @JAUCopy(include = false)
    public int value;
}
