package com.googlecode.jau;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Information about a class.
 */
final class ClassInfo {
    public static final int INTEGER_TYPE = 0;
    public static final int BYTE_TYPE = 1;
    public static final int SHORT_TYPE = 2;
    public static final int LONG_TYPE = 3;
    public static final int FLOAT_TYPE = 4;
    public static final int DOUBLE_TYPE = 5;
    public static final int CHARACTER_TYPE = 6;
    public static final int OTHER_TYPE = 7;

    /** is the class annotated (possibly through the package)? */
    public boolean annotated;

    /** annotation for the class */
    public Annotation annotation;

    /** fields used for .equals() */
    public Field[] fields;

    /** offsets for Unsafe */
    public long[] offsets;

    /** types of fields */
    public int[] types;

    /** should superclass be considered? */
    boolean useParent;
}
