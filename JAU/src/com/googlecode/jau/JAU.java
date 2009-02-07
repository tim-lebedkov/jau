package com.googlecode.jau;

import java.lang.annotation.Annotation;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Annotation based implementation of common methods.
 * All methods in this class are thread-safe.
 */
public class JAU {
    /**
     * Boolean function with 1 argument
     * 
     * @param <T> argument type
     */
    private static interface BooleanFunc<T> {
        public boolean perform(T t);
    }

    private static final BooleanFunc<JAUEquals> JAU_EQUALS_INCLUDE =
            new BooleanFunc<JAUEquals>() {
        public boolean perform(JAUEquals a) {
            return a.include();
        }
    };

    private static final BooleanFunc<JAUToString> JAU_TOSTRING_INCLUDE =
            new BooleanFunc<JAUToString>() {
        public boolean perform(JAUToString a) {
            return a.include();
        }
    };

    private static final BooleanFunc<JAUHashCode> JAU_HASHCODE_INCLUDE =
            new BooleanFunc<JAUHashCode>() {
        public boolean perform(JAUHashCode a) {
            return a.include();
        }
    };

    private static final BooleanFunc<JAUCopy> JAU_COPY_INCLUDE =
            new BooleanFunc<JAUCopy>() {
        public boolean perform(JAUCopy a) {
            return a.include();
        }
    };

    private static final BooleanFunc<JAUCompareTo> JAU_COMPARETO_INCLUDE =
            new BooleanFunc<JAUCompareTo>() {
        public boolean perform(JAUCompareTo a) {
            return a.include();
        }
    };

    private static final BooleanFunc<JAUToMap> JAU_TOMAP_INCLUDE =
            new BooleanFunc<JAUToMap>() {
        public boolean perform(JAUToMap a) {
            return a.include();
        }
    };

    private static final BooleanFunc<JAUEquals> JAU_EQUALS_ALLFIELDS =
            new BooleanFunc<JAUEquals>() {
        public boolean perform(JAUEquals a) {
            return a.allFields();
        }
    };

    private static final BooleanFunc<JAUToString> JAU_TOSTRING_ALLFIELDS =
            new BooleanFunc<JAUToString>() {
        public boolean perform(JAUToString a) {
            return a.allFields();
        }
    };

    private static final BooleanFunc<JAUHashCode> JAU_HASHCODE_ALLFIELDS =
            new BooleanFunc<JAUHashCode>() {
        public boolean perform(JAUHashCode a) {
            return a.allFields();
        }
    };

    private static final BooleanFunc<JAUCopy> JAU_COPY_ALLFIELDS =
            new BooleanFunc<JAUCopy>() {
        public boolean perform(JAUCopy a) {
            return a.allFields();
        }
    };

    private static final BooleanFunc<JAUCompareTo> JAU_COMPARETO_ALLFIELDS =
            new BooleanFunc<JAUCompareTo>() {
        public boolean perform(JAUCompareTo a) {
            return a.allFields();
        }
    };

    private static final BooleanFunc<JAUToMap> JAU_TOMAP_ALLFIELDS =
            new BooleanFunc<JAUToMap>() {
        public boolean perform(JAUToMap a) {
            return a.allFields();
        }
    };

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

    private static JAUInterface IMPLEMENTATION;

    static {
        try {
            IMPLEMENTATION = (JAUInterface) Class.forName(
                    "com.googlecode.jau.JAUUnsafe").
                    newInstance();
        } catch (Exception e) {
            // ignore
        }
    }

    private static final Map<Class, Copier> COPIERS =
            new ConcurrentHashMap<Class, Copier>();
    private static final Map<Class, Comparator> COMPARATORS_COMPARETO =
            new ConcurrentHashMap<Class, Comparator>();
    private static final Map<Class, Comparator> COMPARATORS_EQUALS =
            new ConcurrentHashMap<Class, Comparator>();
    private static final Map<Class, HashCoder> HASH_CODERS =
            new ConcurrentHashMap<Class, HashCoder>();
    private static final Map<Class, Stringifier> STRINGIFIERS =
            new ConcurrentHashMap<Class, Stringifier>();

    private static final Map<Class, ClassInfo> ANNOTATED_FOR_TO_STRING =
            new ConcurrentHashMap<Class, ClassInfo>();
    private static final Map<Class, ClassInfo> ANNOTATED_FOR_HASHCODE =
            new ConcurrentHashMap<Class, ClassInfo>();
    private static final Map<Class, ClassInfo> ANNOTATED_FOR_EQUALS =
            new ConcurrentHashMap<Class, ClassInfo>();
    private static final Map<Class, ClassInfo> ANNOTATED_FOR_COPY =
            new ConcurrentHashMap<Class, ClassInfo>();
    private static final Map<Class, ClassInfo> ANNOTATED_FOR_COMPARE =
            new ConcurrentHashMap<Class, ClassInfo>();
    private static final Map<Class, ClassInfo> ANNOTATED_FOR_TOMAP =
            new ConcurrentHashMap<Class, ClassInfo>();

    static {
        COPIERS.put(StringBuffer.class, new StringBufferCopier());
        COPIERS.put(StringBuilder.class, new StringBuilderCopier());
        COMPARATORS_COMPARETO.put(StringBuffer.class, new StringBufferComparator());
        COMPARATORS_COMPARETO.put(StringBuilder.class, new StringBuilderComparator());
        COMPARATORS_EQUALS.put(StringBuffer.class, new StringBufferComparator());
        COMPARATORS_EQUALS.put(StringBuilder.class, new StringBuilderComparator());
        COMPARATORS_EQUALS.put(Hashtable.class, MapComparator.INSTANCE);
        COMPARATORS_EQUALS.put(HashMap.class, MapComparator.INSTANCE);
        HASH_CODERS.put(StringBuffer.class, new StringBufferHashCoder());
        HASH_CODERS.put(StringBuilder.class, new StringBuilderHashCoder());
        HASH_CODERS.put(Hashtable.class, MapHashCoder.INSTANCE);
        HASH_CODERS.put(HashMap.class, MapHashCoder.INSTANCE);
        STRINGIFIERS.put(Hashtable.class, MapStringifier.INSTANCE);
        STRINGIFIERS.put(HashMap.class, MapStringifier.INSTANCE);
    }

    /**
     * Registers a user defined Copier for a class.
     * A copier cannot be registered for an array, an interface
     * or a primitive type.
     * 
     * Copiers for the following classes are defined by default:
     *     java.lang.StringBuffer
     *     java.lang.StringBuilder
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
        if (c.isInterface())
            throw new IllegalArgumentException(
                    "Cannot register a copier for an interface");
        COPIERS.put(c, copier);
    }

    /**
     * Registers a user defined Stringifier for a class.
     * An implementation for computing string representation
     * cannot be registered for an array, an enum, interface
     * or a primitive type.
     *
     * Stringifiers for the following classes are defined by default:
     *     java.lang.StringBuffer
     *     java.lang.StringBuilder
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
        if (c.isInterface())
            throw new IllegalArgumentException(
                    "Cannot register a Stringifier for an interface");
        STRINGIFIERS.put(c, stringifier);
    }

    /**
     * Registers a user defined Comparator for a class
     * A comparator cannot be registered for an array, a primitive type, an
     * interface or a type that implements {@link Comparable}
     *
     * Comparators for the following classes are defined by default:
     *     java.lang.StringBuffer
     *     java.lang.StringBuilder
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
        if (c.isInterface())
            throw new IllegalArgumentException(
                    "Cannot register a comparator for an interface");
        if (Comparable.class.isAssignableFrom(c))
            throw new IllegalArgumentException(
                "Cannot register a comparator for a class that implements " +
                "java.lang.Comparable");
        COMPARATORS_COMPARETO.put(c, copier);
    }

    /**
     * Registers a user defined Comparator for a class that is used in
     * JAU.equals.
     * A comparator cannot be registered for an array, a primitive type,
     * an interface or a type that implements {@link Comparable}
     *
     * Comparators for the following classes are defined by default:
     *     java.lang.StringBuffer
     *     java.lang.StringBuilder
     *     java.util.Hashtable
     *     java.util.HashMap
     *
     * @param c a class
     * @param copier a copier for the class
     */
    public static <T> void registerEqualsComparator(Class<T> c, Comparator<T> copier) {
        if (c.isArray())
            throw new IllegalArgumentException(
                    "Cannot register a comparator for an array");
        if (c.isPrimitive())
            throw new IllegalArgumentException(
                    "Cannot register a comparator for a primitive type");
        if (c.isInterface())
            throw new IllegalArgumentException(
                    "Cannot register a comparator for an interface");
        if (Comparable.class.isAssignableFrom(c))
            throw new IllegalArgumentException(
                "Cannot register a comparator for a class that implements " +
                "java.lang.Comparable");
        COMPARATORS_EQUALS.put(c, copier);
    }

    /**
     * Registers a user defined hash code algorithm for a class.
     * A coder cannot be registered for an array, an interface
     * or a primitive type.
     *
     * HashCoders for the following classes are defined by default:
     *     java.lang.StringBuffer
     *     java.lang.StringBuilder
     *     java.util.Hashtable
     *     java.util.HashMap
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
        if (c.isInterface())
            throw new IllegalArgumentException(
                    "Cannot register a coder for an interface");
        HASH_CODERS.put(c, hc);
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
        } else {
            ClassInfo ci = annotatedFor(ANNOTATED_FOR_HASHCODE, ca,
                    JAUHashCode.class, JAU_HASHCODE_INCLUDE,
                    JAU_HASHCODE_ALLFIELDS);
            if (ci.annotated) {
                return hashCodeAnnotated(a, ci, ca,
                        (JAUHashCode) ci.annotation,
                        initialNonZeroOddNumber, multiplierNonZeroOddNumber);
            } else {
                HashCoder hc = HASH_CODERS.get(ca);
                if (hc != null)
                    return hc.hashCode(a);
                else 
                    return a.hashCode();
            }
        }
    }

    /**
     * Computes hash code for an object annotated by {@link JAUEquals}
     *
     * @param a the object
     * @param ci class information
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @param initialNonZeroOddNumber
     *            a non-zero, odd number used as the initial value
     * @param multiplierNonZeroOddNumber
     *            a non-zero, odd number used as the multiplier
     * @return hash code
     */
    private static int hashCodeAnnotated(Object a, ClassInfo ci,
            Class ca, JAUHashCode classAnnotation, int initialNonZeroOddNumber,
            int multiplierNonZeroOddNumber) {
        Field[] fields = getFieldsFor(ca, JAUHashCode.class,
                JAU_HASHCODE_INCLUDE, JAU_HASHCODE_ALLFIELDS);
        int result = initialNonZeroOddNumber;
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            try {
                switch (ci.types[i]) {
                    case ClassInfo.INTEGER_TYPE:
                        result += multiplierNonZeroOddNumber * f.getInt(a);
                        break;
                    case ClassInfo.BYTE_TYPE:
                        result += multiplierNonZeroOddNumber * f.getByte(a);
                        break;
                    case ClassInfo.SHORT_TYPE:
                        result += multiplierNonZeroOddNumber * f.getShort(a);
                        break;
                    case ClassInfo.LONG_TYPE:
                        long value = f.getLong(a);
                        result += multiplierNonZeroOddNumber * 
                                (int)(value ^ (value >>> 32));
                        break;
                    case ClassInfo.FLOAT_TYPE:
                        result += multiplierNonZeroOddNumber *
                                Float.floatToIntBits(f.getFloat(a));
                        break;
                    case ClassInfo.DOUBLE_TYPE:
                        long bits = Double.doubleToLongBits(f.getDouble(a));
                        result += multiplierNonZeroOddNumber *
                                (int)(bits ^ (bits >>> 32));
                        break;
                    case ClassInfo.CHARACTER_TYPE:
                        result += multiplierNonZeroOddNumber *
                                (int) (f.getChar(a));
                        break;
                    default:
                        result += multiplierNonZeroOddNumber * 
                                hashCode(f.get(a));
                }
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

            ClassInfo parentci = annotatedFor(ANNOTATED_FOR_HASHCODE,
                    parentClass, JAUHashCode.class, JAU_HASHCODE_INCLUDE,
                    JAU_HASHCODE_ALLFIELDS);
            if (parentci.annotated)
                return result + hashCodeAnnotated(a, parentci, parentClass,
                        (JAUHashCode) parentci.annotation,
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
     * @param annotationClass class of the annotation
     * @param includeFunc calls .include() on the annotation
     * @param allFields calls .allFields() on the annotation
     * @return information about annotation
     */
    private static ClassInfo annotatedFor(
            Map<Class, ClassInfo> storedInfos,
            Class c, Class annotationClass,
            BooleanFunc includeFunc, BooleanFunc allFieldsFunc) {
        ClassInfo b = storedInfos.get(c);
        if (b == null) {
            // firstly, check package annotation
            Package p = c.getPackage();
            boolean include = false;
            if (p != null) {
                Annotation annotation = p.getAnnotation(annotationClass);
                if (annotation != null && includeFunc.perform(annotation))
                    include = true;
            }

            // class annotation is more important if present
            Annotation annotation = c.getAnnotation(annotationClass);
            if (annotation != null)
                include = includeFunc.perform(annotation);

            ClassInfo ci = new ClassInfo();
            ci.annotated = include;
            ci.annotation = annotation;
            ci.fields = getFieldsFor(c, annotationClass, includeFunc,
                    allFieldsFunc);
            ci.offsets = new long[ci.fields.length];
            ci.types = new int[ci.fields.length];
            for (int i = 0; i < ci.fields.length; i++) {
                if (IMPLEMENTATION != null)
                    IMPLEMENTATION.update(ci);
                Class fc = ci.fields[i].getType();
                if (fc == Integer.TYPE) {
                    ci.types[i] = ClassInfo.INTEGER_TYPE;
                } else if (fc == Byte.TYPE) {
                    ci.types[i] = ClassInfo.BYTE_TYPE;
                } else if (fc == Short.TYPE) {
                    ci.types[i] = ClassInfo.SHORT_TYPE;
                } else if (fc == Long.TYPE) {
                    ci.types[i] = ClassInfo.LONG_TYPE;
                } else if (fc == Float.TYPE) {
                    ci.types[i] = ClassInfo.FLOAT_TYPE;
                } else if (fc == Double.TYPE) {
                    ci.types[i] = ClassInfo.DOUBLE_TYPE;
                } else if (fc == Character.TYPE) {
                    ci.types[i] = ClassInfo.CHARACTER_TYPE;
                } else {
                    ci.types[i] = ClassInfo.OTHER_TYPE;
                }
            }
            storedInfos.put(c, ci);
            return ci;
        } else {
            return b;
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

        ClassInfo ci = annotatedFor(
                ANNOTATED_FOR_EQUALS,
                ca, JAUEquals.class, JAU_EQUALS_INCLUDE,
                JAU_EQUALS_ALLFIELDS);
        if (ci.annotated) {
            try {
                return equalsAnnotated(a, b, ca, ci);
            } catch (IllegalArgumentException ex) {
                throw (InternalError) new InternalError(
                        ex.getMessage()).initCause(ex);
            } catch (IllegalAccessException ex) {
                throw (InternalError) new InternalError(
                        ex.getMessage()).initCause(ex);
            }
        } else if (ca.isArray()) {
            int lengtha = Array.getLength(a);
            int lengthb = Array.getLength(b);
            if (lengtha != lengthb)
                return false;

            if (ca == byte[].class)
                return Arrays.equals((byte[]) a, (byte[]) b);
            else if (ca == short[].class)
                return Arrays.equals((short[]) a, (short[]) b);
            else if (ca == int[].class)
                return Arrays.equals((int[]) a, (int[]) b);
            else if (ca == long[].class)
                return Arrays.equals((long[]) a, (long[]) b);
            else if (ca == char[].class)
                return Arrays.equals((char[]) a, (char[]) b);
            else if (ca == float[].class)
                return Arrays.equals((float[]) a, (float[]) b);
            else if (ca == double[].class)
                return Arrays.equals((double[]) a, (double[]) b);
            else if (ca == boolean[].class)
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
        } else {
            Comparator comparator = COMPARATORS_EQUALS.get(ca);
            if (comparator != null)
                return comparator.compare(a, b) == 0;
            else
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
     * @param ci information about the class
     * @return true = equals
     */
    private static boolean equalsAnnotated(Object a, Object b,
            Class ca, ClassInfo ci) throws IllegalArgumentException,
            IllegalAccessException {
        if (IMPLEMENTATION == null) {
            for (Field f: ci.fields) {
                Class c = f.getType();
                if (c == Integer.TYPE) {
                    if (f.getInt(a) != f.getInt(b))
                        return false;
                } else if (c == Byte.TYPE) {
                    if (f.getByte(a) != f.getByte(b))
                        return false;
                } else if (c == Short.TYPE) {
                    if (f.getShort(a) != f.getShort(b))
                        return false;
                } else if (c == Long.TYPE) {
                    if (f.getLong(a) != f.getLong(b))
                        return false;
                } else if (c == Float.TYPE) {
                    if (Float.floatToIntBits(f.getFloat(a)) !=
                            Float.floatToIntBits(f.getFloat(b)))
                        return false;
                } else if (c == Double.TYPE) {
                    if (Double.doubleToLongBits(f.getDouble(a)) !=
                            Double.doubleToLongBits(f.getDouble(b)))
                        return false;
                } else if (c == Character.TYPE) {
                    if (f.getChar(a) != f.getChar(b))
                        return false;
                } else {
                    if (!equals(f.get(a), f.get(b)))
                        return false;
                }
            }
        } else {
            if (!IMPLEMENTATION.equals(ci, a, b))
                return false;
        }
        if (ci.annotation == null || ((JAUEquals) ci.annotation).inherited()) {
            Class parentClass = ca.getSuperclass();
            if (parentClass == null || parentClass == Object.class)
                return true;

            ClassInfo cip = annotatedFor(
                    ANNOTATED_FOR_EQUALS,
                    parentClass,
                    JAUEquals.class, JAU_EQUALS_INCLUDE, JAU_EQUALS_ALLFIELDS);
            if (cip.annotated)
                return equalsAnnotated(a, b, parentClass, cip);
            else
                return false;
        }

        return true;
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

            System.arraycopy(a, 0, b, 0, lengtha);
        } else {
            ClassInfo ci = annotatedFor(ANNOTATED_FOR_COPY, ca, JAUCopy.class, 
                    JAU_COPY_INCLUDE, JAU_COPY_ALLFIELDS);
            if (ci.annotated) {
                copyAnnotated(a, b, ca, (JAUCopy) ci.annotation, ci);
            } else {
                Copier copier = COPIERS.get(ca);
                if (copier != null)
                    copier.copy(a, b);
                else
                    throw new IllegalArgumentException(
                            "Class " + ca + " is not annotated for copy");
            }
        }
    }

    /**
     * Firstly, this method creates an object: either by
     * <ul>
     *  <li>- invoking clone if the class of a implements Cloneable</li>
     *  <li>- invoking the default constructor (even if it is private)</li>
     *  <li>- invoking the copy constructor if it exists (in this case there will
     *     be no special field copying)</li>
     * </ul>
     *
     * Copies of the following classes are not created
     * for enumerations or immutable classes:
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
     * @param a an object or null
     * @return null, if a == null, copy of a otherwise
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
     * @param ci info about the class
     * @return true = equals
     */
    private static void copyAnnotated(Object a, Object b,
            Class ca, JAUCopy classAnnotation, ClassInfo ci) {
        for (Field f: ci.fields) {
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

            ClassInfo parentci = annotatedFor(ANNOTATED_FOR_COPY,
                    parentClass, JAUCopy.class, JAU_COPY_INCLUDE,
                    JAU_COMPARETO_ALLFIELDS);
            if (parentci.annotated)
                copyAnnotated(a, b, parentClass,
                        (JAUCopy) parentClass.getAnnotation(JAUCopy.class),
                        parentci);
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
        } else {
            ClassInfo ci = annotatedFor(ANNOTATED_FOR_COMPARE, ca,
                    JAUCompareTo.class, JAU_COMPARETO_INCLUDE,
                    JAU_COMPARETO_ALLFIELDS);
            if (ci.annotated) {
                return compareAnnotated(a, b, ca,
                        (JAUCompareTo) ci.annotation);
            } else if (a instanceof Comparable) {
                return ((Comparable) a).compareTo(b);
            } else {
                Comparator comparator = COMPARATORS_COMPARETO.get(ca);
                if (comparator != null)
                    return comparator.compare(a, b);
                else
                    throw new java.lang.IllegalArgumentException(
                            "Cannot compare instances of the class " + ca);
            }
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
                ClassInfo parentci = annotatedFor(ANNOTATED_FOR_COMPARE,
                        parentClass, JAUCompareTo.class, JAU_COMPARETO_INCLUDE,
                        JAU_COMPARETO_ALLFIELDS);
                if (parentci.annotated)
                    inheritedCompare = compareAnnotated(a, b, parentClass,
                            (JAUCompareTo) parentci.annotation);
            }
        }
        if (inheritedCompare != 0)
            return inheritedCompare;

        Field[] fields = getFieldsFor(ca, JAUCompareTo.class,
                JAU_COMPARETO_INCLUDE, JAU_COMPARETO_ALLFIELDS);
        for (Field f: fields) {
            try {
                Class c = f.getType();
                int r;
                if (c == Byte.TYPE) {
                    r = f.getByte(a) - f.getByte(b);
                } else if (c == Short.TYPE) {
                    r = f.getShort(a) - f.getShort(b);
                } else if (c == Integer.TYPE) {
                    r = f.getInt(a) - f.getInt(b);
                } else if (c == Long.TYPE) {
                    r = (int) (f.getLong(a) - f.getLong(b));
                } else if (c == Float.TYPE) {
                    r = Float.compare(f.getFloat(a), f.getFloat(b));
                } else if (c == Double.TYPE) {
                    r = Double.compare(f.getDouble(a), f.getDouble(b));
                } else if (c == Character.TYPE) {
                    r = f.getChar(a) - f.getChar(b);
                } else {
                    r = compare(f.get(a), f.get(b));
                }
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
        toString(sb, a, false);
        return sb.toString();
    }

    /**
     * Returns all fields necessary to perform equals() computation for the
     * specified class.
     *
     * @param c a class annotated with JAUEquals
     * @param annotationClass annotation class like JAUEquals
     * @return fields
     */
    private static Field[] getFieldsFor(Class c, Class annotationClass,
            BooleanFunc includef, BooleanFunc allFields) {
        Field[] fields = c.getDeclaredFields();
        Annotation classAnnotation =
                c.getAnnotation(annotationClass);

        boolean defaultInclude;
        if (classAnnotation != null)
            defaultInclude = allFields.perform(classAnnotation);
        else
            defaultInclude = true;

        List<Field> r = new ArrayList<Field>();
        for (Field f: fields) {
            if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic())
                continue;

            boolean include = defaultInclude;
            Annotation an = f.getAnnotation(annotationClass);
            if (an != null)
                include &= includef.perform(an);

            if (include) {
                r.add(f);
                if (Modifier.isPrivate(f.getModifiers()) && !f.isAccessible())
                    f.setAccessible(true);
            }
        }
        fields = r.toArray(new Field[r.size()]);
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
     * @param manyLines true = spread string representatio over many lines
     */
    public static void toString(StringBuilder sb, Object a, boolean manyLines) {
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
        } else {
            ClassInfo ci = annotatedFor(ANNOTATED_FOR_TO_STRING,
                    ca, JAUToString.class, JAU_TOSTRING_INCLUDE,
                    JAU_TOSTRING_ALLFIELDS);
            if (ci.annotated) {
                sb.append(ca.getCanonicalName()).append("@").
                        append(Integer.toHexString(
                        System.identityHashCode(a))).append("(");
                try {
                    toStringAnnotated(sb, a, ca,
                            (JAUToString) ci.annotation, manyLines);
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
    }

    /**
     * Computes string representation for an object annotated by JAUToString
     *
     * @param sb string representation appended here
     * @param a the object
     * @param ca only fields from this class (and superclasses of it)
     *     are considered
     * @param classAnnotation annotation of the class or null
     * @param manyLines spreads the string representation over many lines
     * @return string representation
     */
    private static void toStringAnnotated(StringBuilder sb, Object a,
            Class ca, JAUToString classAnnotation, boolean manyLines)
            throws IllegalArgumentException, IllegalAccessException {
        boolean first = true;

        for (Field f: getFieldsFor(ca, JAUToString.class, JAU_TOSTRING_INCLUDE,
                JAU_TOSTRING_ALLFIELDS)) {
            if (manyLines) {
                if (!first)
                    sb.append(",\n    ");
                else
                    sb.append("\n    ");
            } else {
                if (!first)
                    sb.append(", ");
            }
            sb.append(f.getName()).append('=');
                fieldToString(sb, f, a, manyLines);
                first = false;
        }
        if (classAnnotation == null || classAnnotation.inherited()) {
            Class parentClass = ca.getSuperclass();
            if (parentClass == null || parentClass == Object.class) {
                // nothing
            } else {
                ClassInfo parentci = annotatedFor(ANNOTATED_FOR_TO_STRING,
                        parentClass, JAUToString.class, JAU_TOSTRING_INCLUDE,
                        JAU_TOSTRING_ALLFIELDS);
                if (parentci.annotated) {
                    if (!first)
                        sb.append(", ");
                    toStringAnnotated(sb, a, parentClass,
                            (JAUToString) parentci.annotation, manyLines);
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
     * @param manyLines true = spread string representation over many lines
     */
    private static void fieldToString(StringBuilder sb, Field f, Object a,
            boolean manyLines)
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
                toString(sb, f.get(a), manyLines);
            }
        } else {
            toString(sb, f.get(a), manyLines);
        }
    }

    /**
     * Transfers field values from one object to another. Classes
     * should be annotated using JAUToMap (directly or through the
     * corresponding package) for this to work.
     *
     * <code>a</code> and <code>b</code> may be instances of *different*
     * classes.
     *
     * Static and synthetic fields will be ignored.
     *
     * @param a source
     * @param b destination
     * @param clone if true, each field value will be cloned using JAU.clone
     * @throws IllegalArgumentException if <code>a</code> or <code>b</code> is an
     *     enumeration value or an array or the class of <code>a</code> or
     *     <code>b</code> is not annotated with JAUToMap
     */
    public static void transferFields(Object a, Object b, boolean clone) {
        Map<String, Object> m = toMap(a);
        if (clone) {
            Map<String, Object> copy = new HashMap<String, Object>();
            for (Map.Entry<String, Object> e: m.entrySet()) {
                copy.put(e.getKey(), clone(e.getValue()));
            }
            m = copy;
        }
        fromMap(m, b);
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

        ClassInfo ci = annotatedFor(ANNOTATED_FOR_TOMAP, ca, JAUToMap.class,
                JAU_TOMAP_INCLUDE, JAU_TOMAP_ALLFIELDS);
        if (ci.annotated) {
            Map<String, Object> m = new HashMap<String, Object>();
            toMapAnnotated(m, a, ca, (JAUToMap) ci.annotation);
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
        Field[] fields = getFieldsFor(ca, JAUToMap.class, JAU_TOMAP_INCLUDE,
                JAU_TOMAP_ALLFIELDS);
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
                ClassInfo parentci = annotatedFor(ANNOTATED_FOR_TOMAP,
                        parentClass, JAUToMap.class, JAU_TOMAP_INCLUDE,
                        JAU_TOMAP_ALLFIELDS);
                if (parentci.annotated) {
                    toMapAnnotated(map, a, parentClass,
                            (JAUToMap) parentci.annotation);
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

        ClassInfo ci = annotatedFor(ANNOTATED_FOR_TOMAP, ca, JAUToMap.class,
                JAU_TOMAP_INCLUDE, JAU_TOMAP_ALLFIELDS);
        if (ci.annotated) {
            fromMapAnnotated(map, a, ca, (JAUToMap) ci.annotation);
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
        Field[] fields = getFieldsFor(ca, JAUToMap.class, JAU_TOMAP_INCLUDE,
                JAU_TOMAP_ALLFIELDS);
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
                ClassInfo parentci = annotatedFor(ANNOTATED_FOR_TOMAP,
                        parentClass, JAUToMap.class, JAU_TOMAP_INCLUDE,
                        JAU_TOMAP_ALLFIELDS);
                if (parentci.annotated) {
                    fromMapAnnotated(map, a, parentClass,
                            (JAUToMap) parentci.annotation);
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
