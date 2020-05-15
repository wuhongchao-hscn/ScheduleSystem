package com.hitachi.schedule.controller.constraints;

import com.hitachi.schedule.controller.validator.FullCharSpaceOkValidator;

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
@Constraint(validatedBy = {FullCharSpaceOkValidator.class})
public @interface FullCharSpaceOK {

    String message() default "{key}には全角文字(全角空白を含む)のみを入力してください。";

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