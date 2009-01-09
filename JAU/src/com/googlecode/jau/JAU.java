package com.googlecode.jau;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Annotation based implementation of common methods.
 */
public class JAU {
    /**
     * Generates hash code for an object {@link Object#hashCode()}.
     *
     * @param an object or null (17 is returned for null)
     * @return generated hash code. If same fields in a class are marked
     *     with @JAUEquals and @JAUHashCode, the value returned by this
     *     function and by {@link #equals(java.lang.Object, java.lang.Object)}
     *     are consistent.
     */
    public static int hashCode(Object a) {
        if (a == null)
            return 17;

        Class ca = a.getClass();

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);

            int result = 23;
            for (int i = 0; i < lengtha; i++) {
                Object ela = Array.get(a, i);
                result += hashCode(ela) * 29;
            }
            return result;
        }

        JAUHashCode eqclass = (JAUHashCode) ca.getAnnotation(
                JAUHashCode.class);
        if (eqclass != null && eqclass.include())
            return hashCodeAnnotated(a, ca, eqclass);

        return a.hashCode();
    }

    private static int hashCodeAnnotated(Object a,
            Class ca, JAUHashCode classAnnotation) {
        Field[] fields = ca.getDeclaredFields();
        int result = 11;
        for (Field f: fields) {
            boolean include = classAnnotation.allFields();
            JAUHashCode an = (JAUHashCode) f.getAnnotation(JAUHashCode.class);
            if (an != null)
                include &= an.include();

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

            classAnnotation = (JAUHashCode) parent.getAnnotation(JAUHashCode.class);
            if (classAnnotation != null && classAnnotation.include())
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
     *     for classes annotated with JAUEquals only fields annotated with
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

        JAUEquals eqclass = (JAUEquals) ca.getAnnotation(JAUEquals.class);
        if (eqclass != null && eqclass.include())
            return equalsAnnotated(a, b, ca, eqclass);

        return a.equals(b);
    }

    /**
     * Compares 2 objects annotated by @JAUEquals
     *
     * @param a first object
     * @param b second object
     * @param ca only fields from this class are considered
     * @param eqclass annotation
     * @return true = equals
     */
    private static boolean equalsAnnotated(Object a, Object b,
            Class ca, JAUEquals eqclass) {
        Field[] fields = ca.getDeclaredFields();
        for (Field f: fields) {
            boolean include = eqclass.allFields();
            JAUEquals an = (JAUEquals) f.getAnnotation(JAUEquals.class);
            if (an != null)
                include &= an.include();

            if (include) {
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
                return true;

            eqclass = (JAUEquals) parent.getAnnotation(JAUEquals.class);
            if (eqclass != null && eqclass.include())
                return equalsAnnotated(a, b, parent, eqclass);
            else
                return false;
        }

        return true;
    }
}
