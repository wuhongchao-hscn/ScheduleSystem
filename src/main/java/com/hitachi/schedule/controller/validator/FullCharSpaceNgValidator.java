package com.hitachi.schedule.controller.validator;

import com.hitachi.schedule.controller.common.StringUtil;
import com.hitachi.schedule.controller.constraints.FullCharSpaceNg;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FullCharSpaceNgValidator implements ConstraintValidator<FullCharSpaceNg, String> {

    @Override
    public void initialize(FullCharSpaceNg constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        if (value.contains("　")) {
            return false;
        }

        return StringUtil.fullCharCheck(value);
    }
}

