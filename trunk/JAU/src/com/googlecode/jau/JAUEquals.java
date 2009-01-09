package com.googlecode.jau;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class with automatic .equals()
 */
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.PACKAGE, ElementType.TYPE,
        ElementType.FIELD})
public @interface JAUEquals {
    /**
     * Should the parent .equals() be taken into account. This is only
     * relevant for classes.
     */
    boolean inherited() default true;

    /**
     * Should all fields be included by default? This is only relevant for
     * classes.
     */
    boolean allFields() default true;

    /**
     * Should this package/class/field be considered for automatic equals()?
     */
    boolean include() default true;
}
