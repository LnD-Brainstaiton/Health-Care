package com.health.care.appointment.service.repository;

import com.health.care.appointment.service.domain.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment  a WHERE (:doctorId IS NULL OR a.doctorId LIKE %:doctorId%) and " +
            "(:patientId IS NULL OR a.patientId LIKE %:patientId%) and " +
            "(:date IS NULL OR a.appointmentDate >= :date)" +
            "ORDER BY a.appointmentDate ASC ")
        Page<Appointment> findByParam(
                Pageable pageable,
                String doctorId,
                String patientId,
                LocalDate date
        );
}
