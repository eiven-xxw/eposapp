package com.eposapp.common.util;

import com.eposapp.common.constant.ResponseCodeConstans;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by 42548 on 2018/3/18.
 */
public class ValidationUtils {

    private  static final ValidatorFactory VF = Validation.buildDefaultValidatorFactory();

    public static String validation(Object object){
        Validator validator = VF.getValidator();
        Set<ConstraintViolation<Object>> set = validator.validate(object);
        for (ConstraintViolation constraintViolation : set) {
            return constraintViolation.getMessage();
        }
        return ResponseCodeConstans.SUCCESS;
    }
}
