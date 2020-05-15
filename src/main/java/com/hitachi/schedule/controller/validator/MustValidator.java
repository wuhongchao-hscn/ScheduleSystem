package com.hitachi.schedule.controller.validator;

import com.hitachi.schedule.controller.constraints.Must;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MustValidator implements ConstraintValidator<Must, String> {

    @Override
    public void initialize(Must constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return true;
    }
}

