package com.vetcare.repository;

import com.vetcare.model.MedicalRecord;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository {

    MedicalRecord save(MedicalRecord medicalRecord) throws SQLException;

    MedicalRecord save(MedicalRecord medicalRecord, Connection connection) throws SQLException;

    Optional<MedicalRecord> findById(Long id) throws SQLException;

    Optional<MedicalRecord> findByAppointmentId(Long appointmentId) throws SQLException;

    List<MedicalRecord> findByPetId(Long petId) throws SQLException;

    void finalizar(MedicalRecord medicalRecord, Connection connection) throws SQLException;

}
