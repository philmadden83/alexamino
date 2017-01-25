package com.mymo.flexa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Phil on 1/22/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Slot {
    String value();
    String pattern() default "";
    String format() default "";
    Class<?> typeDeserializer() default Class.class;
}
