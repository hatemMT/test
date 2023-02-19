package com.clinics.core.boundary;

import com.clinics.core.BaseIntTest;
import com.clinics.core.control.ClinicsService;
import com.clinics.core.control.validation.AppointmentConflictValidator;
import com.clinics.core.control.validation.ValidationAspect;
import com.clinics.core.entity.Appointment;
import com.clinics.core.entity.Status;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.LocalDateTime;

import static com.clinics.core.entity.CancellationReason.PATIENT_REQUEST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Import({ClinicsService.class,
        ValidationAspect.class, AppointmentConflictValidator.class})
@AutoConfigureDataJpa
@TestMethodOrder(OrderAnnotation.class)
public class ClinicControllerIntTest extends BaseIntTest {

    @Test
    @Order(1)
    public void testListAppointmentsTodayIsEmpty() {
        graphQlTester.document(queryAllAppointments()).execute().path("listAllAppointmentsToday")
                     .entityList(Appointment.class).hasSize(0);
    }

    @Test
    @Order(2)
    public void testListAppointmentsTodayAfterAddingOneToday() {
        //Given an appointment today is added
        LocalDateTime dateTime = LocalDateTime.now().plusHours(2);
        String patientId = "1";
        String doctorId = "1";
        graphQlTester.document(
                addNewAppointment(doctorId, patientId, dateTime)
        ).execute();

        // When fetching today's list
        graphQlTester
                .document(queryAllAppointments()).execute()
                .path("listAllAppointmentsToday")
                .entityList(Appointment.class)
                .hasSize(1)
                .satisfies(list -> {
                    assertThat(list.get(0).getDoctor().getId(), is(doctorId));
                    assertThat(list.get(0).getPatient().getId(), is(patientId));
                });

    }

    @Test
    @Order(3)
    public void testAddingConflictingAppointment() {
        //Given an appointment that is not conflicting
        LocalDateTime dateTime = LocalDateTime.now().plusHours(4);
        String patientId = "1";
        String doctorId = "1";
        graphQlTester.document(
                addNewAppointment(doctorId, patientId, dateTime)
        ).execute();

        //When Adding a conflicting one
        String anotherPatient = "2";
        graphQlTester.document(
                             addNewAppointment(doctorId, anotherPatient, dateTime)
                     ).execute().errors()
                     .satisfy(errs -> {
                         assertThat(errs.size(), is(1));
                         assertThat(errs.get(0).getErrorType().toString(), is("BAD_REQUEST"));
                     });
    }

    @Test
    @Order(4)
    public void testCancellingAppointment() {
        //Given a new appointment
        LocalDateTime dateTime = LocalDateTime.now().plusHours(4);
        String patientId = "3";
        String doctorId = "2";
        GraphQlTester.Entity<Appointment, ?> newAppointment = graphQlTester.document(
                addNewAppointment(doctorId, patientId, dateTime)
        ).execute().path("addNewAppointment").entity(Appointment.class);

        String appointmentId = newAppointment.get().getId();


        GraphQlTester.Response executionResponse = graphQlTester.document(cancelAppointmentByPatientRequest(appointmentId))
                                                                .execute();
        executionResponse.path("cancelAppointment")
                         .entity(Appointment.class)
                         .satisfies(appointment -> {
                             assertThat(appointment.getCancellationReason(), is(PATIENT_REQUEST));
                             assertThat(appointment.getStatus(), is(Status.CANCELED));
                         });
    }

    private String cancelAppointmentByPatientRequest(String appointmentId) {
        return
                // language=graphql
                """
                mutation {
                cancelAppointment(id: "%s", reason: PATIENT_REQUEST){
                        id
                        doctor { id firstName lastName phone }
                        patient { id firstName lastName phone }
                        dateTime
                        status
                        cancellationReason
                    }
                }
                """.formatted(appointmentId);
    }


    private String addNewAppointment(String doctorId, String patientId, LocalDateTime dateTime) {
        return
                // language=graphql
                """
                mutation {
                addNewAppointment(newAppointment: {doctorId: "%s", patientId: "%s", dateTime:"%s"}){
                        id
                        doctor { id firstName lastName phone }
                        patient { id firstName lastName phone }
                        dateTime
                        status
                    }
                }
                """.formatted(doctorId, patientId, dateTime);
    }

    @NotNull
    private static String queryAllAppointments() {
        // language=graphql
        var document = """
                       query {
                            listAllAppointmentsToday {
                                id
                                dateTime
                                doctor { id firstName lastName phone}     
                                patient { id firstName lastName phone }           
                                status      
                            }
                       }
                       """;
        return document;
    }

}

