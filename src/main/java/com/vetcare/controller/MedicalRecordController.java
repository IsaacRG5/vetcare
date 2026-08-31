
package com.vetcare.controller;

import com.vetcare.model.MedicalRecord;
import com.vetcare.model.MedicalRecordMedicine;
import com.vetcare.service.MedicalRecordService;
import com.vetcare.service.impl.MedicalRecordServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController() {
        this.medicalRecordService = new MedicalRecordServiceImpl();
    }

    public MedicalRecord abrirAtencion(Long appointmentId, String sintomas) throws SQLException {
        return medicalRecordService.abrirAtencion(appointmentId, sintomas);
    }

    public void finalizarAtencion(Long medicalRecordId, String diagnostico, String tratamiento,
                                  String observaciones, Map<Long, Integer> medicamentosYCantidades) throws SQLException {

        List<MedicalRecordMedicine> medicamentosUtilizados = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : medicamentosYCantidades.entrySet()) {
            Long medicineId = entry.getKey();
            Integer cantidad = entry.getValue();
            medicamentosUtilizados.add(new MedicalRecordMedicine(null, medicineId, cantidad));
        }

        medicalRecordService.finalizarAtencion(
                medicalRecordId, diagnostico, tratamiento, observaciones, medicamentosUtilizados);
    }

    public MedicalRecord buscarMedicalRecordPorId(Long id) throws SQLException {
        return medicalRecordService.buscarPorId(id);
    }

    public List<MedicalRecord> listarMedicalRecordsPorMascota(Long petId) throws SQLException {
        return medicalRecordService.listarPorMascota(petId);
    }
}