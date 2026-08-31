package com.vetcare.repository;

import com.vetcare.model.MedicalRecordMedicine;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface MedicalRecordMedicineRepository {

    void save(MedicalRecordMedicine medicalRecordMedicine, Connection connection) throws SQLException;

    List<MedicalRecordMedicine> findByMedicalRecordId(Long medicalRecordId) throws SQLException;

}