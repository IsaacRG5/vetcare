package com.vetcare.repository.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.model.MedicalRecord;
import com.vetcare.model.enums.EstadoAtencion;
import com.vetcare.repository.MedicalRecordRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return save(medicalRecord, connection);
        }
    }

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord, Connection connection) throws SQLException {
        String sql = "INSERT INTO medical_records (appointment_id, pet_id, veterinarian_id, sintomas, " +
                "diagnostico, tratamiento, observaciones, fecha_atencion, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, medicalRecord.getAppointmentId());
            statement.setLong(2, medicalRecord.getPetId());
            statement.setLong(3, medicalRecord.getVeterinarianId());
            statement.setString(4, medicalRecord.getSintomas());
            statement.setString(5, medicalRecord.getDiagnostico());
            statement.setString(6, medicalRecord.getTratamiento());
            statement.setString(7, medicalRecord.getObservaciones());
            statement.setTimestamp(8, Timestamp.valueOf(medicalRecord.getFechaAtencion()));
            statement.setString(9, medicalRecord.getEstado().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medicalRecord.setId(generatedKeys.getLong(1));
                }
            }
        }

        return medicalRecord;
    }

    @Override
    public Optional<MedicalRecord> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM medical_records WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToMedicalRecord(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<MedicalRecord> findByAppointmentId(Long appointmentId) throws SQLException {
        String sql = "SELECT * FROM medical_records WHERE appointment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, appointmentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToMedicalRecord(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<MedicalRecord> findByPetId(Long petId) throws SQLException {
        String sql = "SELECT * FROM medical_records WHERE pet_id = ? ORDER BY fecha_atencion";
        List<MedicalRecord> records = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, petId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    records.add(mapRowToMedicalRecord(resultSet));
                }
            }
        }

        return records;
    }

    @Override
    public void finalizar(MedicalRecord medicalRecord, Connection connection) throws SQLException {
        String sql = "UPDATE medical_records SET diagnostico = ?, tratamiento = ?, observaciones = ?, " +
                "estado = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, medicalRecord.getDiagnostico());
            statement.setString(2, medicalRecord.getTratamiento());
            statement.setString(3, medicalRecord.getObservaciones());
            statement.setString(4, EstadoAtencion.FINALIZADA.name());
            statement.setLong(5, medicalRecord.getId());

            statement.executeUpdate();
        }
    }

    private MedicalRecord mapRowToMedicalRecord(ResultSet resultSet) throws SQLException {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(resultSet.getLong("id"));
        medicalRecord.setAppointmentId(resultSet.getLong("appointment_id"));
        medicalRecord.setPetId(resultSet.getLong("pet_id"));
        medicalRecord.setVeterinarianId(resultSet.getLong("veterinarian_id"));
        medicalRecord.setSintomas(resultSet.getString("sintomas"));
        medicalRecord.setDiagnostico(resultSet.getString("diagnostico"));
        medicalRecord.setTratamiento(resultSet.getString("tratamiento"));
        medicalRecord.setObservaciones(resultSet.getString("observaciones"));
        medicalRecord.setFechaAtencion(resultSet.getTimestamp("fecha_atencion").toLocalDateTime());
        medicalRecord.setEstado(EstadoAtencion.valueOf(resultSet.getString("estado")));
        return medicalRecord;
    }
}