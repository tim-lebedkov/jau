package com.googlecode.jau;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Annotation based implementation of common methods.
 */
public class JAU {
    /**
     * -
     */
    private JAU() {
    }

    /**
     * Default comparator that uses 
     * {@link #compare(java.lang.Object, java.lang.Object) }
     * for object comparison.
     */
    public static final Comparator COMPARATOR = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            return JAU.compare(o1, o2);
        }
    };

    /**
     * Default comparator that uses
     * {@link #hashCode(java.lang.Object) } for hash code computing.
     */
    public static final HashCoder HASHCODER = new HashCoder() {
        @Override
        public int hashCode(Object obj) {
            return JAU.hashCode(obj);
        }
    };

    /**
     * Default copier that uses
     * {@link #copy(java.lang.Object, java.lang.Object) }
     * for copying objects.
     */
    public static final Copier COPIER = new Copier() {
        @Override
        public void copy(Object a, Object b) {
            JAU.copy(a, b);
        }
    };

    /**
     * Default implementation of .toString() that uses
     * {@link #toString(java.lang.Object) }.
     */
    public static final Stringifier STRINGIFIER = new Stringifier() {
        @Override
        public String toString(Object a) {
            return JAU.toString(a);
        }
    };

    private static final Map<Class, Copier> COPIERS =
            new Hashtable<Class, Copier>();
    private static final Map<Class, Comparator> COMPARATORS =
            new Hashtable<Class, Comparator>();
    private static final Map<Class, HashCoder> HASH_CODERS =
            new Hashtable<Class, HashCoder>();
    private static final Map<Class, Stringifier> STRINGIFIERS =
            new Hashtable<Class, Stringifier>();

    private static final Map<Class, Boolean> ANNOTATED_FOR_TO_STRING = 
            new Hashtable<Class, Boolean>();
    private static final Map<Class, Boolean> ANNOTATED_FOR_HASHCODE =
            new Hashtable<Class, Boolean>();
    private static final Map<Class, Boolean> ANNOTATED_FOR_EQUALS =
            new Hashtable<Class, Boolean>();
    private static final Map<Class, Boolean> ANNOTATED_FOR_COPY =
            new Hashtable<Class, Boolean>();
    private static final Map<Class, Boolean> ANNOTATED_FOR_COMPARE =
            new Hashtable<Class, Boolean>();
    private static final Map<Class, Boolean> ANNOTATED_FOR_TOMAP =
            new Hashtable<Class, Boolean>();

    private static final Map<Class, Field[]> TO_STRING_FIELDS =
            new Hashtable<Class, Field[]>();
    private static final Map<Class, Field[]> EQUALS_FIELDS =
            new Hashtable<Class, Field[]>();
    private static final Map<Class, Field[]> HASHCODE_FIELDS =
            new Hashtable<Class, Field[]>();
    private static final Map<Class, Field[]> COPY_FIELDS =
            new Hashtable<Class, Field[]>();
    private static final Map<Class, Field[]> COMPARE_FIELDS =
            new Hashtable<Class, Field[]>();
    private static final Map<Class, Field[]> TO_MAP_FIELDS =
            new Hashtable<Class, Field[]>();

    static {
        COPIERS.put(StringBuffer.class, new StringBufferCopier());
        COPIERS.put(StringBuilder.class, new StringBuilderCopier());
        COMPARATORS.put(StringBuffer.class, new StringBufferComparator());
        COMPARATORS.put(StringBuilder.class, new StringBuilderComparator());
        HASH_CODERS.put(StringBuffer.class, new StringBufferHashCoder());
        HASH_CODERS.put(StringBuilder.class, new StringBuilderHashCoder());
    }

    /**
     * Registers a user defined Copier for a class.
     * A copier cannot be registered for an array or a primitive type.
     * 
     * @param c a class
     * @param copier a copier for the class
     */
    public static <T> void registerCopier(Class<T> c, Copier<T> copier) {
        if (c.isArray())
            throw new IllegalArgumentException(
                    "Cannot register a copier for an array");
        if (c.isPrimitive())
            throw new IllegalArgumentException(
                    "Cannot register a copier for a primitive type");
        COPIERS.put(c, copier);
    }

    /**
     * Registers a user defined Stringifier for a class.
     * An implementation for computing string representation
     * cannot be registered for an
     * array, an enum or a primitive type.
     *
     * @param c a class
     * @param stringifier an implementation for computing string representation
     */
    public static <T> void registerStringifier(Class<T> c, Stringifier<T> stringifier) {
        if (c.isArray())
            throw new IllegalArgumentException(
                    "Cannot register a stringifier for an array");
        if (c.isPrimitive())
            throw new IllegalArgumentException(
                    "Cannot register a stringifier for a primitive type");
        if (c.isEnum())
            throw new IllegalArgumentException(
                    "Cannot register a stringifier for an enum type");
        STRINGIFIERS.put(c, stringifier);
    }

    /**
     * Registers a user defined Comparator for a class
     * A comparator cannot be registered for an array, a primitive type or
     * a type that implements {@link Comparable}
     *
     * @param c a class
     * @param copier a copier for the class
     */
    public static <T> void registerComparator(Class<T> c, Comparator<T> copier) {
        if (c.isArray())
            throw new IllegalArgumentException(
                    "Cannot register a comparator for an array");
        if (c.isPrimitive())
            throw new IllegalArgumentException(
                    "Cannot register a comparator for a primitive type");
        if (Comparable.class.isAssignableFrom(c))
            throw new IllegalArgumentException(
                "Cannot register a comparator for a class that implements " +
                "java.lang.Comparable");
        COMPARATORS.put(c, copier);
    }

    /**
     * Registers a user defined hash code algorithm for a class.
     * A coder cannot be registered for an array or a primitive type.
     *
     * @param c a class
     * @param hc a hash code algorithm for the class
     */
    public static <T> void registerHashCoder(Class<T> c, HashCoder<T> hc) {
        if (c.isArray())
            throw new IllegalArgumentException(
                    "Cannot register a coder for an array");
        if (c.isPrimitive())
            throw new IllegalArgumentException(
                    "Cannot register a coder for a primitive type");
        HASH_CODERS.put(c, hc);
    }

    /**
     * Returns the hash code value for this map.  The hash code of a map is
     * defined to be the sum of the hash codes of each entry in the map's
     * <tt>entrySet()</tt> view. This ensures that <tt>m1.equals(m2)</tt>
     * implies that <tt>m1.hashCode()==m2.hashCode()</tt> for any two maps
     * <tt>m1</tt> and <tt>m2</tt>, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * <p>This implementation iterates over <tt>entrySet()</tt>, calling
     * {@link Map.Entry#hashCode hashCode()} on each element (entry) in the
     * set, and adding up the results.
     *
     * @return the hash code value for this map
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    private static <K, V> int mapHashCode(Map<K, V> map) {
        int h = 0;
        Iterator<Entry<K,V>> i = map.entrySet().iterator();
        while (i.hasNext()) {
            Entry<K, V> next = i.next();
            Object key = next.getKey();
            Object value = next.getValue();
            int hc = (key == null ? 0 : hashCode(key)) ^
                (value == null ? 0 : hashCode(value));
            h += hc;
        }
        return h;
    }

    /**
     * Check whether a class is annotated for automatic computation of
     * hash code (directly or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic hashCode()
     */
    private static boolean annotatedForHashCode(Class c) {
        Boolean b = ANNOTATED_FOR_HASHCODE.get(c);
        if (b == null) {
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

            ANNOTATED_FOR_HASHCODE.put(c, Boolean.valueOf(include));
            return include;
        } else {
            return b.booleanValue();
        }
    }

    /**
     * Generates hash code for an object {@link Object#hashCode()}. Classes 
     * should be annotated using {@link JAUHashCode} (directly or through the
     * corresponding package) for automatic computation of hash code
     * via reflection. Another way
     * is to register a user defined object to compute hash codes via
     * {@link #registerHashCoder(java.lang.Class, com.googlecode.jau.HashCoder)}.
     *
     * Static and synthetic fields will be ignored.
     *
     * Default HashCoders are registered for the following classes:
     * <ul>
     *  <li>{@link java.lang.StringBuffer}</li>
     *  <li>{@link java.lang.StringBuilder}</li>
     * </ul>
     *
     * @param a object or null
     * @return generated hash code. If same fields in a class are marked
     *     with {@link JAUEquals} and {@link JAUHashCode},
     *     the value returned by this
     *     function and by {@link #equals(java.lang.Object, java.lang.Object)}
     *     are consistent.
     */
    public static int hashCode(Object a) {
        return hashCode(a, 17, 37);
    }

    /**
     * Generates hash code for an object {@link Object#hashCode()}. Classes
     * should be annotated using {@link JAUHashCode} (directly or through the
     * corresponding package) for automatic computation of hash code
     * via reflection. Another way
     * is to register a user defined object to compute hash codes via
     * {@link #registerHashCoder(java.lang.Class, com.googlecode.jau.HashCoder)}.
     *
     * Static and synthetic fields will be ignored.
     *
     * Default HashCoders are registered for the following classes:
     * <ul>
     *  <li>{@link java.lang.StringBuffer}</li>
     *  <li>{@link java.lang.StringBuilder}</li>
     * </ul>
     *
     * @param a an object or null
     * @param initialNonZeroOddNumber
     *            a non-zero, odd number used as the initial value
     * @param multiplierNonZeroOddNumber
     *            a non-zero, odd number used as the multiplier
     * @return generated hash code. If same fields in a class are marked
     *     with {@link JAUEquals} and {@link JAUHashCode},
     *     the value returned by this
     *     function and by {@link #equals(java.lang.Object, java.lang.Object)}
     *     are consistent.
     * @throws IllegalArgumentException if <code>initialNonZeroOddNumber</code>
     *     is 0 or
     *     even or <code>multiplierNonZeroOddNumber</code> is 0 or even
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

            Class ct = ca.getComponentType();
            if (ct == byte[].class)
                return Arrays.hashCode((byte[]) a);
            else if (ct == short[].class)
                return Arrays.hashCode((short[]) a);
            else if (ct == int[].class)
                return Arrays.hashCode((int[]) a);
            else if (ct == long[].class)
                return Arrays.hashCode((long[]) a);
            else if (ct == char[].class)
                return Arrays.hashCode((char[]) a);
            else if (ct == float[].class)
                return Arrays.hashCode((float[]) a);
            else if (ct == double[].class)
                return Arrays.hashCode((double[]) a);
            else if (ct == boolean[].class)
                return Arrays.hashCode((boolean[]) a);
            else {
                int result = initialNonZeroOddNumber * multiplierNonZeroOddNumber +
                        ca.hashCode();
                for (int i = 0; i < lengtha; i++) {
                    Object ela = Array.get(a, i);
                    result += hashCode(ela) * multiplierNonZeroOddNumber;
                }
                return result;
            }
        } else if (annotatedForHashCode(ca)) {
            return hashCodeAnnotated(a, ca, 
                    (JAUHashCode) ca.getAnnotation(JAUHashCode.class),
                    initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        } else {
            HashCoder hc = HASH_CODERS.get(ca);
            if (hc != null)
                return hc.hashCode(a);
            else {
                if (Map.class.isAssignableFrom(ca))
                    return mapHashCode((Map) a);
                return a.hashCode();
            }
        }
    }

    /**
     * Computes hash code for an object annotated by {@link JAUEquals}
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
        Field[] fields = getFieldsForHashCode(ca);
        int result = initialNonZeroOddNumber;
        for (Field f: fields) {
            if (Modifier.isPrivate(f.getModifiers()) && !f.isAccessible())
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
     * Compares the specified object with this map for equality.  Returns
     * <tt>true</tt> if the given object is also a map and the two maps
     * represent the same mappings.  More formally, two maps <tt>m1</tt> and
     * <tt>m2</tt> represent the same mappings if
     * <tt>m1.entrySet().equals(m2.entrySet())</tt>.  This ensures that the
     * <tt>equals</tt> method works properly across different implementations
     * of the <tt>Map</tt> interface.
     *
     * <p>This implementation first checks if the specified object is this map;
     * if so it returns <tt>true</tt>.  Then, it checks if the specified
     * object is a map whose size is identical to the size of this map; if
     * not, it returns <tt>false</tt>.  If so, it iterates over this map's
     * <tt>entrySet</tt> collection, and checks that the specified map
     * contains each mapping that this map contains.  If the specified map
     * fails to contain such a mapping, <tt>false</tt> is returned.  If the
     * iteration completes, <tt>true</tt> is returned.
     *
     * @param o object to be compared for equality with this map
     * @return <tt>true</tt> if the specified object is equal to this map
     */
    private static <K, V> boolean mapEquals(Map<K, V> a, Map<K, V> o) {
        Map<K, V> m = (Map<K, V>) o;
        if (m.size() != a.size()) {
            return false;
        }

        try {
            Iterator<Entry<K, V>> i = a.entrySet().iterator();
            while (i.hasNext()) {
                Entry<K, V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key))) {
                        return false;
                    }
                } else {
                    if (!equals(value, m.get(key))) {
                        return false;
                    }
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

    /**
     * Check whether a class is annotated for automatic equals() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic equals()
     */
    private static boolean annotatedForEquals(Class c) {
        Boolean b = ANNOTATED_FOR_EQUALS.get(c);
        if (b == null) {
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

            ANNOTATED_FOR_EQUALS.put(c, Boolean.valueOf(include));
            return include;
        } else {
            return b.booleanValue();
        }
    }

    /**
     * Compares 2 objects {@link Object#equals(java.lang.Object)}. Classes
     * should be annotated using @JAUEquals (directly or through the
     * corresponding package) to be compared using reflection. Another way
     * is to register a user defined object to copy values via
     * {@link #registerComparator(java.lang.Class, java.util.Comparator) }
     * At last .equals(Object) is called.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param a first object or null
     * @param b second object or null
     * @return
     *     <p>true, if (a == null) && (b == null)</p>
     *     <p>false, if (a == null) && (b != null)</p>
     *     <p>false, if (a != null) && (b == null)</p>
     *     <p>true, if (a == b)</p>
     *     <p>false, if (a.getClass() != b.getClass())</p>
     *     <p>arrays are compared deeply using this method for each element
     *     for classes annotated with JAUEquals</p>
     *     <p>If a class is not annotated, but implements java.util.Map,
     *         it is compared so JAU.equals() is called for
     *         each value instead of a.equals(b)</p>
     *     <p>a.equals(b) otherwise</p>
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

            Class ct = ca.getComponentType();
            if (ct == byte[].class)
                return Arrays.equals((byte[]) a, (byte[]) b);
            else if (ct == short[].class)
                return Arrays.equals((short[]) a, (short[]) b);
            else if (ct == int[].class)
                return Arrays.equals((int[]) a, (int[]) b);
            else if (ct == long[].class)
                return Arrays.equals((long[]) a, (long[]) b);
            else if (ct == char[].class)
                return Arrays.equals((char[]) a, (char[]) b);
            else if (ct == float[].class)
                return Arrays.equals((float[]) a, (float[]) b);
            else if (ct == double[].class)
                return Arrays.equals((double[]) a, (double[]) b);
            else if (ct == boolean[].class)
                return Arrays.equals((boolean[]) a, (boolean[]) b);
            else {
                for (int i = 0; i < lengtha; i++) {
                    Object ela = Array.get(a, i);
                    Object elb = Array.get(b, i);
                    if (!equals(ela, elb))
                        return false;
                }
                return true;
            }
        } else if (ca == String.class) {
            return ((String) a).equals(b);
        } else if (annotatedForEquals(ca)) {
            return equalsAnnotated(a, b, ca, 
                    (JAUEquals) ca.getAnnotation(JAUEquals.class));
        } else {
            Comparator comparator = COMPARATORS.get(ca);
            if (comparator != null)
                return comparator.compare(a, b) == 0;
            else {
                if (Map.class.isAssignableFrom(ca))
                    return mapEquals((Map) a, (Map) b);
            }
            return a.equals(b);
        }
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
        Field[] fields = getFieldsForEquals(ca);
        for (Field f: fields) {
            if (Modifier.isPrivate(f.getModifiers()) && !f.isAccessible())
                f.setAccessible(true);
            try {
                if (!fieldEqual(f, a, b))
                    return false;
            } catch (IllegalArgumentException ex) {
                throw (InternalError) new InternalError(
                        ex.getMessage()).initCause(ex);
            } catch (IllegalAccessException ex) {
                throw (InternalError) new InternalError(
                        ex.getMessage()).initCause(ex);
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
     * Optimized version for comparing field values.
     *
     * @param f value of this field will be compared
     * @param a first object
     * @param b second object
     */
    private static boolean fieldEqual(Field f, Object a, Object b)
            throws IllegalArgumentException, IllegalAccessException {
        Class c = f.getType();
        if (c.isPrimitive()) {
            if (c == Byte.TYPE) {
                return f.getByte(a) == f.getByte(b);
            } else if (c == Short.TYPE) {
                return f.getShort(a) == f.getShort(b);
            } else if (c == Integer.TYPE) {
                return f.getInt(a) == f.getInt(b);
            } else if (c == Long.TYPE) {
                return f.getLong(a) == f.getLong(b);
            } else if (c == Float.TYPE) {
                return Float.compare(f.getFloat(a), f.getFloat(b)) == 0;
            } else if (c == Double.TYPE) {
                return Double.compare(f.getDouble(a), f.getDouble(b)) == 0;
            } else if (c == Character.TYPE) {
                return f.getChar(a) == f.getChar(b);
            } else {
                return equals(f.get(a), f.get(b));
            }
        } else {
            return equals(f.get(a), f.get(b));
        }
    }

    /**
     * Optimized version for comparing field values.
     *
     * @param f value of this field will be compared
     * @param a first object
     * @param b second object
     */
    private static int fieldCompare(Field f, Object a, Object b)
            throws IllegalArgumentException, IllegalAccessException {
        Class c = f.getType();
        if (c.isPrimitive()) {
            if (c == Byte.TYPE) {
                return f.getByte(a) - f.getByte(b);
            } else if (c == Short.TYPE) {
                return f.getShort(a) - f.getShort(b);
            } else if (c == Integer.TYPE) {
                return f.getInt(a) - f.getInt(b);
            } else if (c == Long.TYPE) {
                return (int) (f.getLong(a) - f.getLong(b));
            } else if (c == Float.TYPE) {
                return Float.compare(f.getFloat(a), f.getFloat(b));
            } else if (c == Double.TYPE) {
                return Double.compare(f.getDouble(a), f.getDouble(b));
            } else if (c == Character.TYPE) {
                return f.getChar(a) - f.getChar(b);
            } else {
                return compare(f.get(a), f.get(b));
            }
        } else {
            return compare(f.get(a), f.get(b));
        }
    }

    /**
     * Check whether a class is annotated for automatic copy() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic equals()
     */
    private static boolean annotatedForCopy(Class c) {
        Boolean b = ANNOTATED_FOR_COPY.get(c);
        if (b == null) {
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

            ANNOTATED_FOR_COPY.put(c, Boolean.valueOf(include));
            return include;
        } else {
            return b.booleanValue();
        }
    }

    /**
     * Copies all data from one object to another (deep copy)
     * Classes should be annotated using JAUCopy (directly or through the
     * corresponding package) to be compared using reflection. Another way
     * is to register a user defined object to copy values via
     * {@link #registerCopier(java.lang.Class, com.googlecode.jau.Copier)}
     *
     * Static and synthetic fields will be ignored.
     *
     * Default Copiers are registered for the following classes:
     * <ul>
     *  <li>java.lang.StringBuffer</li>
     *  <li>java.lang.StringBuilder</li>
     * </ul>
     *
     * @param a first object
     * @param b second object
     * @throws IllegalArgumentException if 
     *     <p><code>a</code> or <code>b</code> are instances of different classes</p>
     *     <p>class of <code>a</code> and <code>b</code> is immutable {@link #isImmutableClass(java.lang.Class)} i </p>
     *     <p><code>a</code> or <code>b</code> is null or</p>
     *     <p><code>a</code> and <code>b</code> are arrays with different lenghts</p>
     *     <p>class of <code>a</code> and <code>b</code> is not annotated with JAUCopy</p>
     */
    public static void copy(Object a, Object b) {
        if (a == null || b == null)
            throw new IllegalArgumentException("a or b is null");

        if (a == b)
            return;

        Class ca = a.getClass();
        Class cb = b.getClass();
        if (ca != cb)
            throw new IllegalArgumentException("Cannot copy " + ca + " to " + cb);

        if (ca.isEnum())
            throw new IllegalArgumentException(
                    "copy() does not work for enumeration values");

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);
            int lengthb = Array.getLength(b);
            if (lengtha != lengthb)
                throw new IllegalArgumentException(
                        "Cannot copy arrays with different lengths");

            for (int i = 0; i < lengtha; i++) {
                Object ela = Array.get(a, i);
                if (ca.getComponentType().isPrimitive())
                    Array.set(b, i, ela);
                else
                    Array.set(b, i, clone(ela));
            }
            return;
        } else if (annotatedForCopy(ca)) {
            copyAnnotated(a, b, ca,
                    (JAUCopy) ca.getAnnotation(JAUCopy.class));
        } else {
            Copier copier = COPIERS.get(ca);
            if (copier != null)
                copier.copy(a, b);
            else
                throw new IllegalArgumentException(
                        "Class " + ca + " is not annotated for copy");
        }
    }

    /**
     * Firstly, this method creates an object: either by
     * - invoking clone if the class of a implements Cloneable
     * - invoking the default constructor (even if it is private)
     * - invoking the copy constructor if it exists (in this case there will
     *     be no special field copying)
     *
     * Copies of the following classes are not created
     * for enumerations or immutable classes
     * {@link #isImmutableClass(java.lang.Class)}
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
        if (isImmutableClass(ca)) {
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
        Field[] fields = getFieldsForCopy(ca);
        for (Field f: fields) {
            if (Modifier.isPrivate(f.getModifiers()) && !f.isAccessible())
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
        Boolean b = ANNOTATED_FOR_COMPARE.get(c);
        if (b == null) {
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

            ANNOTATED_FOR_COMPARE.put(c, Boolean.valueOf(include));
            return include;
        } else {
            return b.booleanValue();
        }
    }

    /**
     * Compares 2 objects. Classes
     * should be annotated using JAUCompareTo (directly or through the
     * corresponding package) to be compared using reflection. Another way
     * is to register a user defined object to copy values via
     * {@link JAU#registerComparator(java.lang.Class, java.util.Comparator)}.
     * Otherwise if
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
     *     arrays are compared deeply using this method for each element
     *     for classes annotated with JAUCompareTo only fields annotated with
     *         JAUCompareTo will be taken into account and compared
     *     a.compareTo(b) otherwise
     * @throws IllegalArgumentException if a.getClass() != b.getClass() or
     *     the class is not annotated and does not implement Comparable
     */
    public static int compare(Object a, Object b) throws IllegalArgumentException {
        if (a == b)
            return 0;
        if (a == null)
            return -1;
        if (b == null)
            return 1;

        Class ca = a.getClass();
        Class cb = b.getClass();
        if (ca != cb)
            throw new IllegalArgumentException(
                    "Cannot compare instances of different classes");

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
        } else if (annotatedForCompare(ca)) {
            return compareAnnotated(a, b, ca,
                    (JAUCompareTo) ca.getAnnotation(JAUCompareTo.class));
        } else if (a instanceof Comparable) {
            return ((Comparable) a).compareTo(b);
        } else {
            Comparator comparator = COMPARATORS.get(ca);
            if (comparator != null)
                return comparator.compare(a, b);
            else
                throw new java.lang.IllegalArgumentException(
                        "Cannot compare instances of the class " + ca);
        }
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

        Field[] fields = getFieldsForCompare(ca);
        for (Field f: fields) {
            if (Modifier.isPrivate(f.getModifiers()) && !f.isAccessible())
                f.setAccessible(true);
            try {
                int r = fieldCompare(f, a, b);
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
        Boolean a = ANNOTATED_FOR_TO_STRING.get(c);
        if (a == null) {
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

            ANNOTATED_FOR_TO_STRING.put(c, Boolean.valueOf(include));
            return include;
        } else {
            return a.booleanValue();
        }
    }

    /**
     * Generates string representation of an object ({@link Object#toString()}).
     * Classes should be annotated using JAUToString (directly or through the
     * corresponding package) for automatic computation of string
     * representation via reflection.
     *
     * Static and synthetic fields will be ignored.
     * Enums are represented as "com.example.EnumClass.VALUE"
     *
     * @param a object or null
     * @return string representation.
     */
    public static String toString(Object a) {
        StringBuilder sb = new StringBuilder(50);
        toString(sb, a);
        return sb.toString();
    }

    /**
     * Returns all fields necessary to perform equals() computation for the
     * specified class.
     *
     * @param c a class annotated with JAUEquals
     * @return fields
     */
    private static Field[] getFieldsForEquals(Class c) {
        Field[] fields = EQUALS_FIELDS.get(c);
        if (fields == null) {
            fields = c.getDeclaredFields();

            JAUEquals classAnnotation =
                    (JAUEquals) c.getAnnotation(JAUEquals.class);

            boolean defaultInclude;
            if (classAnnotation != null)
                defaultInclude = classAnnotation.allFields();
            else
                defaultInclude = true;

            List<Field> r = new ArrayList<Field>();
            for (Field f: fields) {
                if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                    continue;

                boolean include = defaultInclude;
                JAUEquals an = (JAUEquals) f.getAnnotation(JAUEquals.class);
                if (an != null)
                    include &= an.include();

                if (include)
                    r.add(f);
            }
            fields = r.toArray(new Field[r.size()]);
            EQUALS_FIELDS.put(c, fields);
        }
        return fields;
    }

    /**
     * Returns all fields necessary to perform equals() computation for the
     * specified class.
     *
     * @param c a class annotated with JAUEquals
     * @return fields
     */
    private static Field[] getFieldsForToMap(Class c) {
        Field[] fields = TO_MAP_FIELDS.get(c);
        if (fields == null) {
            fields = c.getDeclaredFields();

            JAUToMap classAnnotation =
                    (JAUToMap) c.getAnnotation(JAUToMap.class);

            boolean defaultInclude;
            if (classAnnotation != null)
                defaultInclude = classAnnotation.allFields();
            else
                defaultInclude = true;

            List<Field> r = new ArrayList<Field>();
            for (Field f: fields) {
                if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                    continue;

                boolean include = defaultInclude;
                JAUToMap an = (JAUToMap) f.getAnnotation(JAUToMap.class);
                if (an != null)
                    include &= an.include();

                if (include)
                    r.add(f);
            }
            fields = r.toArray(new Field[r.size()]);
            TO_MAP_FIELDS.put(c, fields);
        }
        return fields;
    }

    /**
     * Returns all fields necessary to perform compare() computation for the
     * specified class.
     *
     * @param c a class annotated with JAUCompareTo
     * @return fields
     */
    private static Field[] getFieldsForCompare(Class c) {
        Field[] fields = COMPARE_FIELDS.get(c);
        if (fields == null) {
            fields = c.getDeclaredFields();

            JAUCompareTo classAnnotation =
                    (JAUCompareTo) c.getAnnotation(JAUCompareTo.class);

            boolean defaultInclude;
            if (classAnnotation != null)
                defaultInclude = classAnnotation.allFields();
            else
                defaultInclude = true;

            List<Field> r = new ArrayList<Field>();
            for (Field f: fields) {
                if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                    continue;

                boolean include = defaultInclude;
                JAUCompareTo an = (JAUCompareTo) f.getAnnotation(JAUCompareTo.class);
                if (an != null)
                    include &= an.include();

                if (include)
                    r.add(f);
            }
            fields = r.toArray(new Field[r.size()]);
            COMPARE_FIELDS.put(c, fields);
        }
        return fields;
    }

    /**
     * Returns all fields necessary to perform copy() computation for the
     * specified class.
     *
     * @param c a class annotated with JAUCopy
     * @return fields
     */
    private static Field[] getFieldsForCopy(Class c) {
        Field[] fields = COPY_FIELDS.get(c);
        if (fields == null) {
            fields = c.getDeclaredFields();

            JAUCopy classAnnotation =
                    (JAUCopy) c.getAnnotation(JAUCopy.class);

            boolean defaultInclude;
            if (classAnnotation != null)
                defaultInclude = classAnnotation.allFields();
            else
                defaultInclude = true;

            List<Field> r = new ArrayList<Field>();
            for (Field f: fields) {
                if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                    continue;

                boolean include = defaultInclude;
                JAUCopy an = (JAUCopy) f.getAnnotation(JAUCopy.class);
                if (an != null)
                    include &= an.include();

                if (include)
                    r.add(f);
            }
            fields = r.toArray(new Field[r.size()]);
            COPY_FIELDS.put(c, fields);
        }
        return fields;
    }

    /**
     * Returns all fields necessary to perform hashCode() computation for the
     * specified class.
     *
     * @param c a class annotated with JAUHashCode
     * @return fields
     */
    private static Field[] getFieldsForHashCode(Class c) {
        Field[] fields = HASHCODE_FIELDS.get(c);
        if (fields == null) {
            fields = c.getDeclaredFields();

            JAUHashCode classAnnotation =
                    (JAUHashCode) c.getAnnotation(JAUHashCode.class);

            boolean defaultInclude;
            if (classAnnotation != null)
                defaultInclude = classAnnotation.allFields();
            else
                defaultInclude = true;

            List<Field> r = new ArrayList<Field>();
            for (Field f: fields) {
                if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                    continue;

                boolean include = defaultInclude;
                JAUHashCode an = (JAUHashCode) f.getAnnotation(JAUHashCode.class);
                if (an != null)
                    include &= an.include();

                if (include)
                    r.add(f);
            }
            fields = r.toArray(new Field[r.size()]);
            HASHCODE_FIELDS.put(c, fields);
        }
        return fields;
    }

    /**
     * Returns all fields necessary to perform toString() computation for the
     * specified class.
     *
     * @param c a class annotated with JAUToString
     * @return fields
     */
    private static Field[] getFieldsForToString(Class c) {
        Field[] fields = TO_STRING_FIELDS.get(c);
        if (fields == null) {
            fields = c.getDeclaredFields();

            JAUToString classAnnotation =
                    (JAUToString) c.getAnnotation(JAUToString.class);

            boolean defaultInclude;
            if (classAnnotation != null)
                defaultInclude = classAnnotation.allFields();
            else
                defaultInclude = true;

            List<Field> r = new ArrayList<Field>();
            for (Field f: fields) {
                if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                    continue;

                boolean include = defaultInclude;
                JAUToString an = (JAUToString) f.getAnnotation(JAUToString.class);
                if (an != null)
                    include &= an.include();

                if (include)
                    r.add(f);
            }
            fields = r.toArray(new Field[r.size()]);
            TO_STRING_FIELDS.put(c, fields);
        }
        return fields;
    }

    /**
     * Generates string representation of an object ({@link Object#toString()}).
     * Classes should be annotated using @JAUToString (directly or through the
     * corresponding package) for automatic computation of string
     * representation via reflection.
     *
     * Static and synthetic fields will be ignored.
     * Enums are represented as "com.example.EnumClass.VALUE"
     *
     * @param sb string representation will be stored here
     * @param an object or null
     */
    private static void toString(StringBuilder sb, Object a) {
        if (a == null) {
            sb.append("null");
            return;
        }

        Class ca = a.getClass();

        if (ca.isArray()) {
            int lengtha = Array.getLength(a);

            sb.append(ca.getComponentType().getCanonicalName()).append("[");
            for (int i = 0; i < lengtha; i++) {
                if (i != 0)
                    sb.append(", ");
                Object ela = Array.get(a, i);
                sb.append(toString(ela));
            }
            sb.append("]");
        } else if (ca.isEnum()) {
            sb.append(ca.getName()).append(".").append(a.toString());
        } else if (ca == String.class) {
            sb.append('\"').append(a).append('\"');
        } else if (annotatedForToString(ca)) {
            sb.append(ca.getCanonicalName()).append("@").
                    append(Integer.toHexString(
                    System.identityHashCode(a))).append("(");
            try {
                toStringAnnotated(sb, a, ca,
                        (JAUToString) ca.getAnnotation(JAUToString.class));
            } catch (IllegalArgumentException ex) {
                throw (InternalError) new InternalError(
                        ex.getMessage()).initCause(ex);
            } catch (IllegalAccessException ex) {
                throw (InternalError) new InternalError(
                        ex.getMessage()).initCause(ex);
            }
            sb.append(")");
        } else {
            Stringifier s = STRINGIFIERS.get(ca);
            if (s != null)
                sb.append(s.toString(a));
            else
                sb.append(a.toString());
        }
    }

    /**
     * Computes string representation for an object annotated by JAUToString
     *
     * @param sb string representation appended here
     * @param a the object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @return string representation
     */
    private static void toStringAnnotated(StringBuilder sb, Object a,
            Class ca, JAUToString classAnnotation)
            throws IllegalArgumentException, IllegalAccessException {
        boolean first = true;

        ToStringType t;
        if (classAnnotation != null)
            t = classAnnotation.type();
        else
            t = ToStringType.ONE_LINE;

        for (Field f: getFieldsForToString(ca)) {
            if (Modifier.isPrivate(f.getModifiers()) && !f.isAccessible())
                f.setAccessible(true);
            if (t == ToStringType.MANY_LINES) {
                if (!first)
                    sb.append(",\n    ");
                else
                    sb.append("\n    ");
            } else {
                if (!first)
                    sb.append(", ");
            }
            sb.append(f.getName()).append('=');
                fieldToString(sb, f, a);
                first = false;
        }
        if (classAnnotation == null || classAnnotation.inherited()) {
            Class parentClass = ca.getSuperclass();
            if (parentClass == null || parentClass == Object.class) {
                // nothing
            } else {
                if (annotatedForToString(parentClass)) {
                    if (!first)
                        sb.append(", ");
                    toStringAnnotated(sb, a, parentClass,
                            (JAUToString) parentClass.getAnnotation(
                            JAUToString.class));
                }
            }
        }
    }

    /**
     * Optimized version for appending value of a field to string
     * representation.
     * 
     * @param sb output
     * @param f value of this field will be appended
     * @param a value for the field will be read from this object
     */
    private static void fieldToString(StringBuilder sb, Field f, Object a)
            throws IllegalArgumentException, IllegalAccessException {
        Class c = f.getType();
        if (c.isPrimitive()) {
            if (c == Byte.TYPE) {
                sb.append(f.getByte(a));
            } else if (c == Short.TYPE) {
                sb.append(f.getShort(a));
            } else if (c == Integer.TYPE) {
                sb.append(f.getInt(a));
            } else if (c == Long.TYPE) {
                sb.append(f.getLong(a));
            } else if (c == Float.TYPE) {
                sb.append(f.getFloat(a));
            } else if (c == Double.TYPE) {
                sb.append(f.getDouble(a));
            } else if (c == Character.TYPE) {
                sb.append(f.getChar(a));
            } else {
                toString(sb, f.get(a));
            }
        } else {
            toString(sb, f.get(a));
        }
    }

    /**
     * Check whether a class is annotated for automatic toString() (directly
     * or through a package).
     *
     * @param c a class
     * @return true = the class can be used for automatic toString()
     */
    private static boolean annotatedForToMap(Class c) {
        Boolean b = ANNOTATED_FOR_TOMAP.get(c);
        if (b == null) {
            // firstly, check package annotation
            Package p = c.getPackage();
            boolean include = false;
            if (p != null) {
                JAUToMap annotation = p.getAnnotation(JAUToMap.class);
                if (annotation != null && annotation.include())
                    include = true;
            }

            // class annotation is more important if present
            JAUToMap annotation = (JAUToMap) c.getAnnotation(JAUToMap.class);
            if (annotation != null)
                include = annotation.include();

            ANNOTATED_FOR_TOMAP.put(c, Boolean.valueOf(include));
            return include;
        } else {
            return b.booleanValue();
        }
    }

    /**
     * Creates a map filled with properties from an object. Classes
     * should be annotated using JAUToMap (directly or through the
     * corresponding package) for this to work.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param a object or null
     * @return a map filled with the property values from <code>a</code>.
     *     An empty map is returned if <code>a</code> is null. Otherwise
     *     there will be an entry for every property from <code>a</code> with
     *     the value from the object. The returned map is mutable.
     * @throws IllegalArgumentException if <code>a</code> is an
     *     enumeration value or an array or the class of <code>a</code> is not
     *     annotated with JAUToMap
     */
    public static Map<String, Object> toMap(Object a) {
        if (a == null)
            return new HashMap();

        Class ca = a.getClass();

        if (ca.isArray() || ca.isEnum())
            throw new IllegalArgumentException(
                    "toMap() does not work for arrays and enumeration values");

        if (annotatedForToMap(ca)) {
            Map<String, Object> m = new HashMap<String, Object>();
            toMapAnnotated(m, a, ca,
                    (JAUToMap) ca.getAnnotation(JAUToMap.class));
            return m;
        } else
            throw new IllegalArgumentException("Class " + ca +
                    " is not annotated with JAUToMap");
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
        Field[] fields = getFieldsForToMap(ca);
        for (Field f: fields) {
            JAUToMap an = (JAUToMap) f.getAnnotation(JAUToMap.class);
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
     * @param a object or null
     * @param map a map filled with the property values, which will be
     *     stored in <code>a</code>.
     *     Nothing is done if <code>a</code> is null or the class of
     *     <code>a</code> is not annotated or an array. Otherwise
     *     properties stored in <code>a</code>.
     */
    public static void fromMap(Map<String, Object> map, Object a) {
        if (a == null)
            return;

        Class ca = a.getClass();

        if (ca.isArray())
            return;

        if (annotatedForToMap(ca)) {
            fromMapAnnotated(map, a, ca,
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
        Field[] fields = getFieldsForToMap(ca);
        for (Field f: fields) {
            JAUToMap an = (JAUToMap) f.getAnnotation(JAUToMap.class);

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

    /**
     * Tests whether a class is immutable.
     *
     * Following classes are considered immutable:
     * <ul>
     *  <li>String.class</li>
     *  <li>Byte.class</li>
     *  <li>Short.class</li>
     *  <li>Integer.class</li>
     *  <li>Long.class</li>
     *  <li>Float.class</li>
     *  <li>Double.class</li>
     *  <li>Character.class</li>
     *  <li>Class.class</li>
     *  <li>Object.class</li>
     *  <li>BigDecimal.class</li>
     *  <li>BigInteger.class</li>
     *  <li>any enumeration class</li>
     * </ul>
     *
     * @param c a class
     * @return true = instances of the class are not mutable
     */
    static boolean isImmutableClass(Class c) {
        return c == String.class || c == Byte.class || c == Short.class ||
                c == Integer.class || c == Long.class || c == Float.class ||
                c == Double.class || c == Character.class ||
                c == Class.class || c == Object.class ||
                c == BigDecimal.class || c == BigInteger.class || c.isEnum();
    }
}
