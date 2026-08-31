package com.vetcare.repository.impl;

import com.vetcare.model.MedicalRecordMedicine;
import com.vetcare.repository.MedicalRecordMedicineRepository;
import com.vetcare.config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordMedicineRepositoryImpl implements MedicalRecordMedicineRepository {

    @Override
    public void save(MedicalRecordMedicine medicalRecordMedicine, Connection connection) throws SQLException {
        String sql = "INSERT INTO medical_record_medicines (medical_record_id, medicine_id, " +
                "cantidad_utilizada) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, medicalRecordMedicine.getMedicalRecordId());
            statement.setLong(2, medicalRecordMedicine.getMedicineId());
            statement.setInt(3, medicalRecordMedicine.getCantidadUtilizada());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medicalRecordMedicine.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    @Override
    public List<MedicalRecordMedicine> findByMedicalRecordId(Long medicalRecordId) throws SQLException {
        String sql = "SELECT * FROM medical_record_medicines WHERE medical_record_id = ?";
        List<MedicalRecordMedicine> list = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, medicalRecordId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    MedicalRecordMedicine mrm = new MedicalRecordMedicine();
                    mrm.setId(resultSet.getLong("id"));
                    mrm.setMedicalRecordId(resultSet.getLong("medical_record_id"));
                    mrm.setMedicineId(resultSet.getLong("medicine_id"));
                    mrm.setCantidadUtilizada(resultSet.getInt("cantidad_utilizada"));
                    list.add(mrm);
                }
            }
        }

        return list;
    }
}