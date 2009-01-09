package com.googlecode.jau;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class with automatic .hashCode()
 */
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(ElementType.TYPE)
public @interface HashCodeClass {
    /**
     * Should the parent .hashCode() be taken into account
     */
    boolean inherited() default false;

    /**
     * Should all fields be included by default?
     */
    boolean allFields() default false;
}
