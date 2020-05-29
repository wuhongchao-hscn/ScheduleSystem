package com.hitachi.schedule.config.validator;

import com.hitachi.schedule.config.common.StringUtil;
import com.hitachi.schedule.config.constraints.FullCharSpaceOK;
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

