package com.hitachi.schedule.controller.validator;

import com.hitachi.schedule.controller.constraints.NoSpace;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoSpaceValidator implements ConstraintValidator<NoSpace, String> {

    @Override
    public void initialize(NoSpace constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            // 空OK
            return true;
        }

        if (value.contains("　") || value.contains(" ")) {
            // space Ng
            return false;
        }
        return true;
    }
}

