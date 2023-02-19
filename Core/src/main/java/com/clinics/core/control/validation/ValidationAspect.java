package com.clinics.core.control.validation;


import com.clinics.core.control.exceptions.AppValidationException;
import com.clinics.core.control.exceptions.ErrorModel;
import com.clinics.core.entity.BaseModel;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidationAspect {

    private final List<BusinessValidator> validators;

    @Before("@annotation(ValidateModel)")
    public void validateModel(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            List<ErrorModel> errs = new ArrayList<>();
            validators.forEach(validator -> {
                        if (validator.canValidate(args[0]))
                            validator.validate((BaseModel) args[0])
                                     .ifPresent(errs::add);
                    }
            );
            if (errs.size() > 0) {
                throw new AppValidationException("Validation error", errs);
            }
        }
    }
}
