package com.clinics.core.control.exceptions;

public class AppointmentNotFound extends RuntimeException {
    public AppointmentNotFound(String appointmentId) {
        super("Appointment that needs cancellation is not found with id " + appointmentId);
    }
}
