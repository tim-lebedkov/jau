package com.googlecode.jau;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Check whether a class is annotated for automatic copy() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic equals()
     */
    private static boolean annotatedForCopy(Class c) {
        // firstly, check package annotation
        Package p = c.getPackage();
        boolean include = false;
        if (p != null) {
            JAUCopy annotation = p.getAnnotation(JAUCopy.class);
            if (annotation != null && annotation.include())
                include = true;
        }

        // class annotation is more important if present
        JAUCopy annotation = (JAUCopy) c.getAnnotation(JAUCopy.class);
        if (annotation != null)
            include = annotation.include();

        return include;
    }

    /**
     * Copies all data from one object to another (deep copy)
     * Classes should be annotated using JAUCopy (directly or through the
     * corresponding package) to be compared using reflection.
     *
     * Static and synthetic fields will be ignored.
     * Nothing happens if a == b or one of the references is null.
     *
     * @param a first object or null (source)
     * @param b second object or null (target)
     */
    public static void copy(Object a, Object b) {
        if (a == b)
            return;
        if (a == null || b == null)
            return;

        Class ca = a.getClass();
        Class cb = b.getClass();
        if (ca != cb)
            throw new InternalError("Cannot copy " + ca + " to " + cb);

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);
            int lengthb = Array.getLength(b);
            if (lengtha != lengthb)
                throw new InternalError("Cannot copy arrays with different lengths");

            for (int i = 0; i < lengtha; i++) {
                Object ela = Array.get(a, i);
                if (ca.getComponentType().isPrimitive())
                    Array.set(b, i, ela);
                else
                    Array.set(b, i, clone(ela));
            }
            return;
        }

        if (annotatedForCopy(ca))
            copyAnnotated(a, b, ca,
                    (JAUCopy) ca.getAnnotation(JAUCopy.class));
        else
            throw new InternalError("Class " + ca + " is not annotated for copy");
    }

    /**
     * Firstly, this method creates an object: either by
     * - invoking clone if the class of a implements Cloneable
     * - invoking the default constructor (even if it is private)
     * - invoking the copy constructor if it exists (in this case there will
     *     be no special field copying)
     *
     * Copies of the following classes are not created:
     * String.class
     * Byte.class
     * Short.class
     * Integer.class
     * Long.class
     * Float.class
     * Double.class
     * Class.class
     * Object.class
     * BigDecimal.class
     * BigInteger.class
     * 
     * @param a an object or null
     * @return null, if a == null
     *    copy of a otherwise
     */
    public static Object clone(Object a) {
        if (a == null)
            return null;

        Class ca = a.getClass();

        Object result;
        if (ca == String.class || ca == Byte.class || ca == Short.class ||
                ca == Integer.class || ca == Long.class || ca == Float.class ||
                ca == Double.class || ca == Class.class || ca == Object.class ||
                ca == BigDecimal.class || ca == BigInteger.class) {
            result = a;
        } else if (ca.isArray()) {
            result = Array.newInstance(ca.getComponentType(),
                    Array.getLength(a));
        } else if (a instanceof Cloneable) {
            try {
                Method method = ca.getMethod("clone", (Class[]) null);
                result = method.invoke(a, (Object[]) null);
            } catch (Exception ex) {
                throw (InternalError) new InternalError(ex.getMessage()).
                        initCause(ex);
            }
        } else {
            Constructor<? extends Object> constructor;
            try {
                constructor = ca.getConstructor(new Class[0]);
            } catch (NoSuchMethodException ex) {
                constructor = null;
            } catch (SecurityException ex) {
                constructor = null;
            }
            if (constructor != null) {
                try {
                    if (!constructor.isAccessible())
                        constructor.setAccessible(true);
                    result = constructor.newInstance((Object[]) null);
                } catch (Exception ex) {
                    throw (InternalError) new InternalError(ex.getMessage()).
                            initCause(ex);
                }
            } else {
                try {
                    constructor = ca.getConstructor(new Class[] {ca});
                } catch (NoSuchMethodException ex) {
                    constructor = null;
                } catch (SecurityException ex) {
                    constructor = null;
                }
                if (constructor != null) {
                    if (!constructor.isAccessible())
                        constructor.setAccessible(true);
                    try {
                        return constructor.newInstance(new Object[] {a});
                    } catch (Exception ex) {
                        throw (InternalError) new InternalError(ex.getMessage()).
                                initCause(ex);
                    }
                } else {
                    try {
                        result = ca.newInstance();
                    } catch (Exception ex) {
                        throw (InternalError) new InternalError(ex.getMessage()).
                                initCause(ex);
                    }
                }
            }
        }
        copy(a, result);
        return result;
    }

    /**
     * Compares 2 objects annotated by JAUCopy
     *
     * @param a first object
     * @param b second object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @return true = equals
     */
    private static void copyAnnotated(Object a, Object b,
            Class ca, JAUCopy classAnnotation) {
        Field[] fields = ca.getDeclaredFields();
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;

            boolean include;
            if (classAnnotation == null)
                include = true;
            else
                include = classAnnotation.allFields();
            JAUCopy an = (JAUCopy) f.getAnnotation(JAUCopy.class);
            if (an != null)
                include &= an.include();

            if (include) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    Object fa = f.get(a);
                    if (f.getType().isPrimitive())
                        f.set(b, fa);
                    else
                        f.set(b, clone(fa));
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
                return;

            if (annotatedForCopy(parentClass))
                copyAnnotated(a, b, parentClass,
                        (JAUCopy) parentClass.getAnnotation(JAUCopy.class));
        }
    }

    /**
     * Check whether a class is annotated for automatic equals() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic equals()
     */
    private static boolean annotatedForCompare(Class c) {
        // firstly, check package annotation
        Package p = c.getPackage();
        boolean include = false;
        if (p != null) {
            JAUCompareTo annotation = p.getAnnotation(JAUCompareTo.class);
            if (annotation != null && annotation.include())
                include = true;
        }

        // class annotation is more important if present
        JAUCompareTo annotation = (JAUCompareTo) c.getAnnotation(JAUCompareTo.class);
        if (annotation != null)
            include = annotation.include();

        return include;
    }

    /**
     * Compares 2 objects {@link Object#equals(java.lang.Object)}. Classes
     * should be annotated using JAUCompareTo (directly or through the
     * corresponding package) to be compared using reflection. Otherwise if
     * a class implements Comparable it's compareTo method is used.
     *
     * Fields in a class are compared from to top bottom (e.g. first field is 
     * more important for comparison than the second one). Fields from
     * superclass are more important and will be compared first.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param a first object or null
     * @param b second object or null
     * @return
     *     < 0, if a < b
     *     > 0, if a > b
     *     0, if a = b
     *
     *     Special cases:
     *     0, if (a == null) && (b == null)
     *     -1, if (a == null) && (b != null)
     *     1, if (a != null) && (b == null)
     *     0, if (a == b)
     *     -1, if (a.getClass() != b.getClass())
     *     arrays are compared deeply using this method for each element
     *     for classes annotated with JAUCompareTo only fields annotated with
     *         JAUCompareTo will be taken into account and compared
     *     a.compareTo(b) otherwise
     */
    public static int compare(Object a, Object b) {
        if (a == b)
            return 0;
        if (a == null)
            return -1;
        if (b == null)
            return 1;

        Class ca = a.getClass();
        Class cb = b.getClass();
        if (ca != cb)
            return -1;

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);
            int lengthb = Array.getLength(b);

            for (int i = 0; i < Math.min(lengtha, lengthb); i++) {
                Object ela = Array.get(a, i);
                Object elb = Array.get(b, i);
                int r = compare(ela, elb);
                if (r != 0)
                    return r;
            }
            return lengtha - lengthb;
        }

        if (annotatedForCompare(ca))
            return compareAnnotated(a, b, ca,
                    (JAUCompareTo) ca.getAnnotation(JAUCompareTo.class));
        else
            if (a instanceof Comparable)
                return ((Comparable) a).compareTo(b);
            else
                return -1;
    }

    /**
     * Compares 2 objects annotated by JAUEquals
     *
     * @param a first object
     * @param b second object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @return true = equals
     */
    private static int compareAnnotated(Object a, Object b,
            Class ca, JAUCompareTo classAnnotation) {
        int inheritedCompare = 0;
        if (classAnnotation == null || classAnnotation.inherited()) {
            Class parentClass = ca.getSuperclass();
            if (parentClass == null || parentClass == Object.class) {
                // nothing
            } else {
                if (annotatedForCompare(parentClass))
                    inheritedCompare = compareAnnotated(a, b, parentClass,
                            (JAUCompareTo) parentClass.getAnnotation(
                            JAUCompareTo.class));
            }
        }
        if (inheritedCompare != 0)
            return inheritedCompare;

        Field[] fields = ca.getDeclaredFields();
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;

            boolean include;
            if (classAnnotation == null)
                include = true;
            else
                include = classAnnotation.allFields();
            JAUCompareTo an = (JAUCompareTo) f.getAnnotation(JAUCompareTo.class);
            if (an != null)
                include &= an.include();

            if (include) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    Object fa = f.get(a);
                    Object fb = f.get(b);
                    int r = compare(fa, fb);
                    if (r != 0)
                        return r;
                } catch (IllegalArgumentException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                } catch (IllegalAccessException ex) {
                    throw (InternalError) new InternalError(
                            ex.getMessage()).initCause(ex);
                }
            }
        }

        return 0;
    }

    /**
     * Check whether a class is annotated for automatic toString() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic toString()
     */
    private static boolean annotatedForToString(Class c) {
        // firstly, check package annotation
        Package p = c.getPackage();
        boolean include = false;
        if (p != null) {
            JAUToString annotation = p.getAnnotation(JAUToString.class);
            if (annotation != null && annotation.include())
                include = true;
        }

        // class annotation is more important if present
        JAUToString annotation = (JAUToString) c.getAnnotation(JAUToString.class);
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
    public static String toString(Object a) {
        if (a == null)
            return "null";

        Class ca = a.getClass();

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);

            StringBuilder sb = new StringBuilder();
            sb.append(ca.getComponentType().getCanonicalName()).append("[");
            for (int i = 0; i < lengtha; i++) {
                if (i != 0)
                    sb.append(", ");
                Object ela = Array.get(a, i);
                sb.append(toString(ela));
            }
            sb.append("]");
            return sb.toString();
        }

        if (ca == String.class)
            return "\"" + a + "\"";

        if (annotatedForToString(ca)) {
            StringBuilder sb = new StringBuilder();
            sb.append(ca.getCanonicalName()).append("@").
                    append(Integer.toHexString(
                    System.identityHashCode(a))).append("(");
            sb.append(toStringAnnotated(a, ca,
                    (JAUToString) ca.getAnnotation(JAUToString.class)));
            sb.append(")");
            return sb.toString();
        } else
            return a.toString();
    }

    /**
     * Computes hash code for an object annotated by @JAUToString
     *
     * @param a the object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @return string representation
     */
    private static String toStringAnnotated(Object a,
            Class ca, JAUToString classAnnotation) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = ca.getDeclaredFields();
        boolean first = true;
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;

            boolean include;
            if (classAnnotation != null)
                include = classAnnotation.allFields();
            else
                include = true;
            JAUToString an = (JAUToString) f.getAnnotation(JAUToString.class);
            if (an != null)
                include &= an.include();

            if (include) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    Object fa = f.get(a);
                    sb.append(f.getName()).append('=').append(toString(fa));
                    first = false;
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
            if (parentClass == null || parentClass == Object.class) {
                // nothing
            } else {
                if (annotatedForToString(parentClass)) {
                    if (!first)
                        sb.append(", ");
                    sb.append(toStringAnnotated(a, parentClass,
                            (JAUToString) parentClass.getAnnotation(
                            JAUToString.class)));
                }
            }
        }

        return sb.toString();
    }

    /**
     * Check whether a class is annotated for automatic toString() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic toString()
     */
    private static boolean annotatedForToMap(Class c) {
        // firstly, check package annotation
        Package p = c.getPackage();
        boolean include = false;
        if (p != null) {
            JAUToString annotation = p.getAnnotation(JAUToString.class);
            if (annotation != null && annotation.include())
                include = true;
        }

        // class annotation is more important if present
        JAUToString annotation = (JAUToString) c.getAnnotation(JAUToString.class);
        if (annotation != null)
            include = annotation.include();

        return include;
    }

    /**
     * Creates a map filled with properties from an object. Classes
     * should be annotated using JAUToMap (directly or through the
     * corresponding package) for this to work.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param an object or null
     * @return a map filled with the property values from <code>a</code>.
     *     An empty map is returned if <code>a</code> is null. Otherwise
     *     there will be an entry for every property from <code>a</code> with
     *     the value from the object. The returned map is mutable. An empty
     *     map is returned if a is an array or the class of <code>a</code> is
     *     not annotated.
     */
    public static Map<String, Object> toMap(Object a) {
        if (a == null)
            return new HashMap();

        Class ca = a.getClass();

        if (ca.isArray()) 
            return new HashMap();

        if (annotatedForToMap(ca)) {
            Map<String, Object> m = new HashMap<String, Object>();
            toMapAnnotated(m, a, ca,
                    (JAUToMap) ca.getAnnotation(JAUToMap.class));
            return m;
        } else
            return new HashMap();
    }

    /**
     * Fills the map with properties from an object. Classes
     * should be annotated using JAUToMap (directly or through the
     * corresponding package) for this to work.
     *
     * @param a the object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @return string representation
     */
    private static void toMapAnnotated(Map<String, Object> map,
            Object a,
            Class ca, JAUToMap classAnnotation) {
        Field[] fields = ca.getDeclaredFields();
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;

            boolean include;
            if (classAnnotation != null)
                include = classAnnotation.allFields();
            else
                include = true;
            JAUToMap an = (JAUToMap) f.getAnnotation(JAUToMap.class);
            if (an != null)
                include &= an.include();

            if (include) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    Object fa = f.get(a);
                    String name;
                    if (an != null) {
                        name = an.name();
                        if (name.length() == 0)
                            name = f.getName();
                    } else {
                        name = f.getName();
                    }

                    map.put(name, fa);
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
            if (parentClass == null || parentClass == Object.class) {
                // nothing
            } else {
                if (annotatedForToMap(parentClass)) {
                    toMapAnnotated(map, a, parentClass,
                            (JAUToMap) parentClass.getAnnotation(
                            JAUToMap.class));
                }
            }
        }
    }

    /**
     * Fills an object with properties stored in a map. Classes
     * should be annotated using JAUToMap (directly or through the
     * corresponding package) for this to work.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param an object or null
     * @param map a map filled with the property values, which will be
     *     stored in <code>a</code>.
     *     Nothing is done if <code>a</code> is null or the class of
     *     <code>a</code> is not annotated or an array. Otherwise
     *     properties stored in <code>a</code>.
     */
    public static void fromMap(Object a, Map<String, Object> map) {
        if (a == null)
            return;

        Class ca = a.getClass();

        if (ca.isArray())
            return;

        if (annotatedForToMap(ca)) {
            Map<String, Object> m = new HashMap<String, Object>();
            fromMapAnnotated(m, a, ca,
                    (JAUToMap) ca.getAnnotation(JAUToMap.class));
        }
    }

    /**
     * Fills an object with properties from a map. Classes
     * should be annotated using JAUToMap (directly or through the
     * corresponding package) for this to work.
     *
     * @param map property name -> property value
     * @param a the object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @return string representation
     */
    private static void fromMapAnnotated(Map<String, Object> map,
            Object a,
            Class ca, JAUToMap classAnnotation) {
        Field[] fields = ca.getDeclaredFields();
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;

            boolean include;
            if (classAnnotation != null)
                include = classAnnotation.allFields();
            else
                include = true;
            JAUToMap an = (JAUToMap) f.getAnnotation(JAUToMap.class);
            if (an != null)
                include &= an.include();

            if (include) {
                if (Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);
                try {
                    String name;
                    if (an != null) {
                        name = an.name();
                        if (name.length() == 0)
                            name = f.getName();
                    } else {
                        name = f.getName();
                    }

                    if (map.containsKey(name))
                        f.set(a, map.get(name));
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
            if (parentClass == null || parentClass == Object.class) {
                // nothing
            } else {
                if (annotatedForToMap(parentClass)) {
                    fromMapAnnotated(map, a, parentClass,
                            (JAUToMap) parentClass.getAnnotation(
                            JAUToMap.class));
                }
            }
        }
    }
}
