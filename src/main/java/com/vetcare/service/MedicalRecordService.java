package com.vetcare.service;

import com.vetcare.model.MedicalRecord;
import com.vetcare.model.MedicalRecordMedicine;
import java.sql.SQLException;
import java.util.List;

public interface MedicalRecordService {

    MedicalRecord abrirAtencion(Long appointmentId, String sintomas) throws SQLException;

    void finalizarAtencion(Long medicalRecordId, String diagnostico, String tratamiento,
                           String observaciones, List<MedicalRecordMedicine> medicamentosUtilizados) throws SQLException;

    MedicalRecord buscarPorId(Long id) throws SQLException;

    List<MedicalRecord> listarPorMascota(Long petId) throws SQLException;

}