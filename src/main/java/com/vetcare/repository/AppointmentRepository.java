package com.vetcare.repository;

import com.vetcare.model.Appointment;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {

    Appointment save(Appointment appointment) throws SQLException;

    Optional<Appointment> findById(Long id) throws SQLException;

    List<Appointment> findAll() throws SQLException;

    List<Appointment> findByPetId(Long petId) throws SQLException;

    List<Appointment> findByVeterinarianId(Long veterinarianId) throws SQLException;

    void updateEstado(Long id, String estado) throws SQLException;

    void delete(Long id) throws SQLException;

    boolean existsByVeterinarianAndFechaHora(Long veterinarianId, LocalDate fecha, LocalTime hora) throws SQLException;

    boolean existsByPetAndFechaHora(Long petId, LocalDate fecha, LocalTime hora) throws SQLException;

    void updateEstado(Long appointmentId, String name, Connection connection);
}