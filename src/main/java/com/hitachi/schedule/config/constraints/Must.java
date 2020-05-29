package com.hitachi.schedule.config.constraints;

import com.hitachi.schedule.config.validator.MustValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MustValidator.class})
public @interface Must {

    String message() default "{key}には値を入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String key() default "";

    /**
     * Defines several {@link javax.validation.constraints.NotNull} annotations on the same element.
     *
     * @see javax.validation.constraints.NotNull
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        javax.validation.constraints.NotNull[] value();
    }
}