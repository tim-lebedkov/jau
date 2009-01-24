package com.googlecode.jau;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Package/class/field used in JAU.toMap()
 */
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.PACKAGE, ElementType.TYPE,
        ElementType.FIELD})
public @interface JAUToMap {
    /**
     * Should the parent .toString() be taken into account. This is only
     * relevant for classes.
     */
    boolean inherited() default true;

    /**
     * Should all fields be included by default? This is only relevant for
     * classes.
     */
    boolean allFields() default true;

    /**
     * Should this package/class/field be considered for automatic toString()?
     */
    boolean include() default true;

    /**
     * Name of the attribute in the map. By default the name of the field
     * is used. This is only valid for class fields.
     *
     * @return Name of the field in the target map.
     */
    String name() default "";
}
