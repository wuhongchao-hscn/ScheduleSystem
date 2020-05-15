package com.hitachi.schedule.controller.validator;

import com.hitachi.schedule.controller.common.StringUtil;
import com.hitachi.schedule.controller.constraints.FullCharSpaceOK;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FullCharSpaceOkValidator implements ConstraintValidator<FullCharSpaceOK, String> {

    @Override
    public void initialize(FullCharSpaceOK constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        return StringUtil.fullCharCheck(value);
    }
}

