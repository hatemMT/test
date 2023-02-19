package com.clinics.core.control.validation;

import com.clinics.core.control.AppointmentRepository;
import com.clinics.core.control.exceptions.ErrorModel;
import com.clinics.core.entity.Appointment;
import com.clinics.core.entity.BaseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentConflictValidator implements BusinessValidator {

    private final AppointmentRepository repository;

    @Override
    public Optional<ErrorModel> validate(BaseModel model) {
        log.debug("validating appointment regarding conflicts : {}", model);
        Appointment appointment = (Appointment) model;
        if (hasConflict(appointment)) {
            return Optional.of(new ErrorModel("Appointment Conflict", " Conflict  at time %s ".formatted((appointment).getDateTime())));
        }
        return Optional.empty();
    }

    public boolean canValidate(Object entity) {
        return entity instanceof Appointment;
    }

    private boolean hasConflict(Appointment appointment) {
        return repository.hasConflict(
                appointment.getDoctor().getId(),
                appointment.getPatient().getId(), appointment.getDateTime(),
                appointment.getDateTime().plusMinutes(30)
        );
    }
}
