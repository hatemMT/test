package com.clinics.core.boundary;

import com.clinics.core.boundary.dto.CreateAppointmentCommand;
import com.clinics.core.control.ClinicsService;
import com.clinics.core.entity.Appointment;
import com.clinics.core.entity.CancellationReason;
import com.clinics.core.entity.Doctor;
import com.clinics.core.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@Slf4j
public class ClinicController {

    private ClinicsService clinicsService;

    @QueryMapping
    public List<Appointment> listAllAppointmentsToday() {
        return clinicsService.listAllAppointmentsToday();
    }

    @QueryMapping
    public List<Appointment> filterAppointmentsByDate(@Argument LocalDateTime fromDate, LocalDateTime toDate) {
        return clinicsService.filterAppointmentsByDateRange(fromDate, toDate);
    }

    @QueryMapping
    public List<Appointment> filterAppointmentsByPatientName(String patientName) {
        return clinicsService.filterAppointmentsByPatientName(patientName);
    }

    @QueryMapping
    public List<Appointment> allAppointmentsByPatientName(String patientName) {
        return clinicsService.allAppointmentsByPatientName(patientName);
    }


    @MutationMapping
    public Appointment addNewAppointment(@Argument CreateAppointmentCommand newAppointment) {
        return clinicsService.addNewAppointment(Appointment.builder()
                                                           .id(UUID.randomUUID().toString())
                                                           .dateTime(newAppointment.dateTime())
                                                           .doctor(Doctor.builder()
                                                                         .id(newAppointment.doctorId())
                                                                         .build())
                                                           .patient(Patient.builder()
                                                                           .id(newAppointment.patientId())
                                                                           .build())
                                                           .build());
    }

    @MutationMapping
    public Appointment cancelAppointment(@Argument String id, @Argument CancellationReason reason) {
        log.debug("Cancelling an appointment : {}", id);
        return clinicsService.cancelAppointment(id, reason);
    }
}
