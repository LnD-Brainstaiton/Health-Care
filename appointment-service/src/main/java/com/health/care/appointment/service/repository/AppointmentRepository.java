package com.health.care.appointment.service.repository;

import com.health.care.appointment.service.domain.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment  a WHERE (:doctorId IS NULL OR a.doctorId LIKE %:doctorId%) and " +
            "(:patientId IS NULL OR a.patientId LIKE %:patientId%) and " +
            "(:appointmentId IS NULL OR a.appointmentNo LIKE %:appointmentId%) and " +
            "(a.appointmentDate >= :date) and " +
            "(a.appointmentTime >= :time)" +
            "ORDER BY a.appointmentDate, a.appointmentTime ASC ")
        Page<Appointment> findByParam(
                @Param("doctorId") String doctorId,
                @Param("patientId") String patientId,
                @Param("appointmentId") String appointmentId,
                @Param("date") LocalDate date,
                @Param("time") LocalTime time,
                Pageable pageable
        );

    @Query("SELECT a FROM Appointment  a WHERE (:doctorId IS NULL OR a.doctorId LIKE %:doctorId%) and " +
            "(a.appointmentDate >= :date) and " +
            "(a.appointmentTime >= :time)" +
            "ORDER BY a.appointmentDate, a.appointmentTime ASC ")
    List<Appointment> findByParamCount(
            @Param("doctorId") String doctorId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );

    @Query(value = "SELECT * FROM appointment a WHERE " +
            "(:doctorId IS NULL OR a.doctor_id LIKE CONCAT('%', :doctorId, '%')) AND " +
            "(:patientId IS NULL OR a.patient_id LIKE CONCAT('%', :patientId, '%')) AND " +
            "(:date IS NULL OR a.appointment_date >= :date) AND " +
            "(:time IS NULL OR a.appointment_time >= :time) " +
            "ORDER BY a.appointment_date ASC",
            nativeQuery = true)
    Page<Appointment> findByParamNative(
            @Param("doctorId") String doctorId,
            @Param("patientId") String patientId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            Pageable pageable
    );
   @Query("SELECT a.appointmentTime FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDate = :date")
    List<LocalTime> getAppointedTime(@Param("doctorId") String doctorId,
                                     @Param("date") LocalDate date);
}
