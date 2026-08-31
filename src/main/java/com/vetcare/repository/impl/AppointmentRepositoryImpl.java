package com.vetcare.repository.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.model.Appointment;
import com.vetcare.model.enums.EstadoCita;
import com.vetcare.repository.AppointmentRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    @Override
    public Appointment save(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (pet_id, veterinarian_id, fecha, hora, motivo, " +
                "estado, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, appointment.getPetId());
            statement.setLong(2, appointment.getVeterinarianId());
            statement.setDate(3, Date.valueOf(appointment.getFecha()));
            statement.setTime(4, Time.valueOf(appointment.getHora()));
            statement.setString(5, appointment.getMotivo());
            statement.setString(6, appointment.getEstado().name());
            statement.setTimestamp(7, Timestamp.valueOf(appointment.getFechaCreacion()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getLong(1));
                }
            }
        }

        return appointment;
    }

    @Override
    public Optional<Appointment> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToAppointment(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Appointment> findAll() throws SQLException {
        String sql = "SELECT * FROM appointments ORDER BY fecha, hora";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                appointments.add(mapRowToAppointment(resultSet));
            }
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByPetId(Long petId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE pet_id = ? ORDER BY fecha, hora";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, petId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(mapRowToAppointment(resultSet));
                }
            }
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByVeterinarianId(Long veterinarianId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE veterinarian_id = ? ORDER BY fecha, hora";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, veterinarianId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    appointments.add(mapRowToAppointment(resultSet));
                }
            }
        }

        return appointments;
    }

    @Override
    public void updateEstado(Long id, String estado) throws SQLException {
        String sql = "UPDATE appointments SET estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, estado);
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE appointments SET estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, EstadoCita.CANCELADA.name());
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    @Override
    public boolean existsByVeterinarianAndFechaHora(Long veterinarianId, LocalDate fecha, LocalTime hora) throws SQLException {
        String sql = "SELECT 1 FROM appointments WHERE veterinarian_id = ? AND fecha = ? AND hora = ? " +
                "AND estado != 'CANCELADA'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, veterinarianId);
            statement.setDate(2, Date.valueOf(fecha));
            statement.setTime(3, Time.valueOf(hora));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean existsByPetAndFechaHora(Long petId, LocalDate fecha, LocalTime hora) throws SQLException {
        String sql = "SELECT 1 FROM appointments WHERE pet_id = ? AND fecha = ? AND hora = ? " +
                "AND estado != 'CANCELADA'";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, petId);
            statement.setDate(2, Date.valueOf(fecha));
            statement.setTime(3, Time.valueOf(hora));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public void updateEstado(Long appointmentId, String name, Connection connection) {

    }

    private Appointment mapRowToAppointment(ResultSet resultSet) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(resultSet.getLong("id"));
        appointment.setPetId(resultSet.getLong("pet_id"));
        appointment.setVeterinarianId(resultSet.getLong("veterinarian_id"));
        appointment.setFecha(resultSet.getDate("fecha").toLocalDate());
        appointment.setHora(resultSet.getTime("hora").toLocalTime());
        appointment.setMotivo(resultSet.getString("motivo"));
        appointment.setEstado(EstadoCita.valueOf(resultSet.getString("estado")));
        appointment.setFechaCreacion(resultSet.getTimestamp("fecha_creacion").toLocalDateTime());
        return appointment;
    }
}