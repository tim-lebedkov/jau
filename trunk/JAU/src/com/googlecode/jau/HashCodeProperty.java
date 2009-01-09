package com.googlecode.jau;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker for properties used in .hashCode().
 */
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target(ElementType.FIELD)
public @interface HashCodeProperty {
}
