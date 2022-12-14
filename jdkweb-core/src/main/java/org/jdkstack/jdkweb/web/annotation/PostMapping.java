package org.jdkstack.jdkweb.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Http Post
 *
 * @author admin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostMapping {

  String name() default "";

  String[] value() default {};

  String[] path() default {};

  String[] params() default {};

  String[] headers() default {};

  String[] consumes() default {};

  String[] produces() default {};
}
