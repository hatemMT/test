package com.clinics.core.boundary.dto;

import java.time.LocalDateTime;

public record CreateAppointmentCommand(
        String id, String doctorId, String patientId, LocalDateTime dateTime
) {}
