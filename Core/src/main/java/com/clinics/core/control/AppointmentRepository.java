package com.clinics.core.control;

import com.clinics.core.entity.Appointment;
import com.clinics.core.entity.CancellationReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate);

    @Query("select a from Appointment a " +
            "where a.patient.firstName like '%:patientName%' " +
            "    or a.patient.lastName like '%:patientName%'" +
            " and a.dateTime >= CURRENT_DATE and a.status = com.clinics.core.entity.Status.ACTIVE")
    List<Appointment> findByPatientName(String patientName);

    @Query("select a from Appointment a " +
            "where a.patient.firstName like '%:patientName%' " +
            "    or a.patient.lastName like '%:patientName%'")
    List<Appointment> findHistoryByPatientName(String patientName);

    @Query(// language=jpaql
            """
            select count(a.id)>0 from Appointment a
            where  ( a.doctor.id= :doctorId or a.patient.id = :patientId)
                    and a.dateTime between :fromTime and  :toTime
            """)
    boolean hasConflict(String doctorId, String patientId, LocalDateTime fromTime, LocalDateTime toTime);

    @Modifying
    @Query("update Appointment a " +
            "set a.status = com.clinics.core.entity.Status.CANCELED , " +
            "a.cancellationReason = :cancellationReason " +
            "where a.id = :appointmentId")
    void cancelAppointment(String appointmentId, CancellationReason cancellationReason);
}
