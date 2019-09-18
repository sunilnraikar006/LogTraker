package com.sgs.mylibrary.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SugarRecord column interface for table with two constraints
 * boolean unique
 * boolean notNull
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();

    /**
     * @return
     */
    boolean unique() default false;

    /**
     * @return
     */
    boolean notNull() default false;
}
