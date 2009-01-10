package com.googlecode.jau;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Annotation based implementation of common methods.
 */
public class JAU {
    /**
     * Check whether a class is annotated for automatic hashCode() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic hashCode()
     */
    private static boolean annotatedForHashCode(Class c) {
        // firstly, check package annotation
        Package p = c.getPackage();
        boolean include = false;
        if (p != null) {
            JAUHashCode annotation = p.getAnnotation(JAUHashCode.class);
            if (annotation != null && annotation.include())
                include = true;
        }

        // class annotation is more important if present
        JAUHashCode annotation = (JAUHashCode) c.getAnnotation(JAUHashCode.class);
        if (annotation != null)
            include = annotation.include();

        return include;
    }

    /**
     * Generates hash code for an object {@link Object#hashCode()}. Classes 
     * should be annotated using @JAUHashCode (directly or through the 
     * corresponding package) for automatic computation of hash code
     * via reflection.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param an object or null
     * @return generated hash code. If same fields in a class are marked
     *     with @JAUEquals and @JAUHashCode, the value returned by this
     *     function and by {@link #equals(java.lang.Object, java.lang.Object)}
     *     are consistent.
     */
    public static int hashCode(Object a) {
        return hashCode(a, 17, 37);
    }

    /**
     * Generates hash code for an object {@link Object#hashCode()}. Classes
     * should be annotated using @JAUHashCode (directly or through the
     * corresponding package) for automatic computation of hash code
     * via reflection.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param a an object or null
     * @param initialNonZeroOddNumber
     *            a non-zero, odd number used as the initial value
     * @param multiplierNonZeroOddNumber
     *            a non-zero, odd number used as the multiplier
     * @return generated hash code. If same fields in a class are marked
     *     with @JAUEquals and @JAUHashCode, the value returned by this
     *     function and by {@link #equals(java.lang.Object, java.lang.Object)}
     *     are consistent.
     */
    public static int hashCode(Object a, int initialNonZeroOddNumber,
            int multiplierNonZeroOddNumber) {
        if (initialNonZeroOddNumber == 0) {
            throw new IllegalArgumentException(
                    "HashCodeBuilder requires a non zero initial value");
        }
        if (initialNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException(
                    "HashCodeBuilder requires an odd initial value");
        }
        if (multiplierNonZeroOddNumber == 0) {
            throw new IllegalArgumentException(
                    "HashCodeBuilder requires a non zero multiplier");
        }
        if (multiplierNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException(
                    "HashCodeBuilder requires an odd multiplier");
        }

        if (a == null)
            return initialNonZeroOddNumber * multiplierNonZeroOddNumber;

        Class ca = a.getClass();

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);

            int result = initialNonZeroOddNumber * multiplierNonZeroOddNumber +
                    ca.hashCode();
            for (int i = 0; i < lengtha; i++) {
                Object ela = Array.get(a, i);
                result += hashCode(ela) * multiplierNonZeroOddNumber;
            }
            return result;
        }

        if (annotatedForHashCode(ca))
            return hashCodeAnnotated(a, ca, 
                    (JAUHashCode) ca.getAnnotation(JAUHashCode.class),
                    initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        else
            return a.hashCode();
    }

    /**
     * Computes hash code for an object annotated by @JAUEquals
     *
     * @param a the object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @param initialNonZeroOddNumber
     *            a non-zero, odd number used as the initial value
     * @param multiplierNonZeroOddNumber
     *            a non-zero, odd number used as the multiplier
     * @return hash code
     */
    private static int hashCodeAnnotated(Object a,
            Class ca, JAUHashCode classAnnotation, int initialNonZeroOddNumber,
            int multiplierNonZeroOddNumber) {
        Field[] fields = ca.getDeclaredFields();
        int result = initialNonZeroOddNumber;
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;

            boolean include;
            if (classAnnotation != null)
                include = classAnnotation.allFields();
            else
                include = true;
            JAUHashCode an = (JAUHashCode) f.getAnnotation(JAUHashCode.class);
            if (an != null)
                include &= an.include();

            if (include) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    Object fa = f.get(a);
                    result += multiplierNonZeroOddNumber * hashCode(fa);
                } catch (IllegalArgumentException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                } catch (IllegalAccessException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                }
            }
        }
        if (classAnnotation == null || classAnnotation.inherited()) {
            Class parentClass = ca.getSuperclass();
            if (parentClass == null || parentClass == Object.class)
                return result;

            if (annotatedForHashCode(parentClass))
                return result + hashCodeAnnotated(a, parentClass,
                        (JAUHashCode) parentClass.getAnnotation(JAUHashCode.class),
                        initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            else
                return result;
        }

        return result;
    }

    /**
     * Check whether a class is annotated for automatic equals() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic equals()
     */
    private static boolean annotatedForEquals(Class c) {
        // firstly, check package annotation
        Package p = c.getPackage();
        boolean include = false;
        if (p != null) {
            JAUEquals annotation = p.getAnnotation(JAUEquals.class);
            if (annotation != null && annotation.include())
                include = true;
        }

        // class annotation is more important if present
        JAUEquals annotation = (JAUEquals) c.getAnnotation(JAUEquals.class);
        if (annotation != null)
            include = annotation.include();

        return include;
    }

    /**
     * Compares 2 objects {@link Object#equals(java.lang.Object)}. Classes
     * should be annotated using @JAUEquals (directly or through the
     * corresponding package) to be compared using reflection.
     *
     * Static and synthetic fields will be ignored.
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
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;

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

        if (annotatedForEquals(ca))
            return equalsAnnotated(a, b, ca, 
                    (JAUEquals) ca.getAnnotation(JAUEquals.class));
        else
            return a.equals(b);
    }

    /**
     * Compares 2 objects annotated by @JAUEquals
     *
     * @param a first object
     * @param b second object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @return true = equals
     */
    private static boolean equalsAnnotated(Object a, Object b,
            Class ca, JAUEquals classAnnotation) {
        Field[] fields = ca.getDeclaredFields();
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;
            
            boolean include;
            if (classAnnotation == null)
                include = true;
            else
                include = classAnnotation.allFields();
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
        if (classAnnotation == null || classAnnotation.inherited()) {
            Class parentClass = ca.getSuperclass();
            if (parentClass == null || parentClass == Object.class)
                return true;

            if (annotatedForEquals(parentClass))
                return equalsAnnotated(a, b, parentClass,
                        (JAUEquals) parentClass.getAnnotation(JAUEquals.class));
            else
                return false;
        }

        return true;
    }
}
