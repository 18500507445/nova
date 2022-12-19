package com.nova.common.utils.common;


import com.nova.common.exception.base.ParamException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator 自定义校验工具类
 *
 * @author tojson
 * @date 2022/6/22 21:13
 */
public class ValidatorUtil {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void validate(Object object, Class<?>... groups) throws ParamException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage()).append(";");
            }
            throw new ParamException(1000, msg.toString());
        }
    }


}