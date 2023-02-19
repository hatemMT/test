package com.clinics.core.control;

import com.clinics.core.control.exceptions.AppointmentNotFound;
import com.clinics.core.control.validation.ValidateModel;
import com.clinics.core.entity.Appointment;
import com.clinics.core.entity.CancellationReason;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.clinics.core.entity.Status.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicsService {

    private final AppointmentRepository repository;

    public List<Appointment> listAllAppointmentsToday() {
        return repository.findByDateTimeBetween(LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(23, 59, 59));
    }

    @ValidateModel
    public Appointment addNewAppointment(Appointment appointment) {
        appointment.setStatus(ACTIVE);
        return repository.save(appointment);
    }

    @Transactional
    public Appointment cancelAppointment(String appointmentId, CancellationReason cancellationReason) {
        repository.cancelAppointment(appointmentId, cancellationReason);
        return repository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFound(appointmentId));
    }

    public List<Appointment> filterAppointmentsByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return repository.findByDateTimeBetween(fromDate, toDate);
    }

    public List<Appointment> filterAppointmentsByPatientName(String patientName) {
        return repository.findByPatientName(patientName);
    }

    public List<Appointment> allAppointmentsByPatientName(String patientName) {
        return repository.findHistoryByPatientName(patientName);
    }
}
