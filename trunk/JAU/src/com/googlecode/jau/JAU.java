package com.googlecode.jau;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Annotation based implementation of common methods.
 */
public class JAU {
    /**
     * Compares 2 objects {@link Object#equals(java.lang.Object)}.
     *
     * @param a first object or null
     * @param b second object or null
     * @return
     *     true, if (a == null) && (b == null)
     *     false, if (a == null) && (b != null)
     *     false, if (a != null) && (b == null)
     *     true, if (a == b)
     *     false, if (a.getClass() != b.getClass())
     *     arrays are compared deeply using this method for each element
     *     for classes annotated with EqualsClass only fields annotated with
     *         EqualsProperty will be taken into account and compared
     *     a.equals(b) otherwise
     */
    public static long hashCode(Object a) {
        if (a == null)
            return 17;

        Class ca = a.getClass();

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);

            long result = 23;
            for (int i = 0; i < lengtha; i++) {
                Object ela = Array.get(a, i);
                result += hashCode(ela) * 29;
            }
            return result;
        }

        HashCodeClass eqclass = (HashCodeClass) ca.getAnnotation(
                HashCodeClass.class);
        if (eqclass != null)
            return hashCodeAnnotated(a, ca, eqclass);

        return a.hashCode();
    }

    private static long hashCodeAnnotated(Object a,
            Class ca, HashCodeClass classAnnotation) {
        Field[] fields = ca.getDeclaredFields();
        long result = 11;
        for (Field f: fields) {
            Annotation an = f.getAnnotation(EqualsProperty.class);
            if (an != null || classAnnotation.allFields()) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    Object fa = f.get(a);
                    result += 17 * hashCode(fa);
                } catch (IllegalArgumentException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                } catch (IllegalAccessException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                }
            }
        }
        if (classAnnotation.inherited()) {
            Class parent = ca.getSuperclass();
            if (parent == null || parent == Object.class)
                return result;

            classAnnotation = (HashCodeClass) parent.getAnnotation(HashCodeClass.class);
            if (classAnnotation != null)
                return hashCodeAnnotated(a, parent, classAnnotation);
            else
                return result;
        }

        return result;
    }

    /**
     * Compares 2 objects {@link Object#equals(java.lang.Object)}.
     *
     * @param a first object or null
     * @param b second object or null
     * @return
     *     true, if (a == null) && (b == null)
     *     false, if (a == null) && (b != null)
     *     false, if (a != null) && (b == null)
     *     true, if (a == b)
     *     false, if (a.getClass() != b.getClass())
     *     arrays are compared deeply using this method for each element
     *     for classes annotated with EqualsClass only fields annotated with
     *         EqualsProperty will be taken into account and compared
     *     a.equals(b) otherwise
     */
    public static boolean equals(Object a, Object b) {
        if (a == null && b == null)
            return true;
        else if (a == null || b == null)
            return false;
        else if (a == b)
            return true;

        Class ca = a.getClass();
        Class cb = b.getClass();
        if (ca != cb)
            return false;

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);
            int lengthb = Array.getLength(b);
            if (lengtha != lengthb)
                return false;

            for (int i = 0; i < lengtha; i++) {
                Object ela = Array.get(a, i);
                Object elb = Array.get(b, i);
                if (!equals(ela, elb))
                    return false;
            }
            return true;
        }

        EqualsClass eqclass = (EqualsClass) ca.getAnnotation(EqualsClass.class);
        if (eqclass != null)
            return equalsAnnotated(a, b, ca, eqclass);

        return a.equals(b);
    }

    private static boolean equalsAnnotated(Object a, Object b,
            Class ca, EqualsClass eqclass) {
        Field[] fields = ca.getDeclaredFields();
        for (Field f: fields) {
            Annotation an = f.getAnnotation(EqualsProperty.class);
            if (an != null || eqclass.allFields()) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    Object fa = f.get(a);
                    Object fb = f.get(b);
                    if (!equals(fa, fb))
                        return false;
                } catch (IllegalArgumentException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                } catch (IllegalAccessException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                }
            }
        }
        if (eqclass.inherited()) {
            Class parent = ca.getSuperclass();
            if (parent == null || parent == Object.class)
                return false;

            eqclass = (EqualsClass) parent.getAnnotation(EqualsClass.class);
            if (eqclass != null)
                return equalsAnnotated(a, b, parent, eqclass);
            else
                return false;
        }

        return true;
    }
}
