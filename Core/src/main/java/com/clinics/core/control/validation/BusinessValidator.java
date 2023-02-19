package com.clinics.core.control.validation;

import com.clinics.core.control.exceptions.ErrorModel;
import com.clinics.core.entity.BaseModel;

import java.util.Optional;

public interface BusinessValidator {
    Optional<ErrorModel> validate(BaseModel entity);

    boolean canValidate(Object entity);

}
