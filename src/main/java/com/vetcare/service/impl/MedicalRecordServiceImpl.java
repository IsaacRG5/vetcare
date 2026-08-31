package com.vetcare.service.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.exception.EntityNotFoundException;
import com.vetcare.exception.InsufficientStockException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Appointment;
import com.vetcare.model.MedicalRecord;
import com.vetcare.model.MedicalRecordMedicine;
import com.vetcare.model.Medicine;
import com.vetcare.model.enums.EstadoCita;
import com.vetcare.repository.*;
import com.vetcare.repository.impl.*;
import com.vetcare.service.MedicalRecordService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMedicineRepository medicalRecordMedicineRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicineRepository medicineRepository;

    public MedicalRecordServiceImpl() {
        this.medicalRecordRepository = new MedicalRecordRepositoryImpl();
        this.medicalRecordMedicineRepository = new MedicalRecordMedicineRepositoryImpl();
        this.appointmentRepository = new AppointmentRepositoryImpl();
        this.medicineRepository = new MedicineRepositoryImpl();
    }

    @Override
    public MedicalRecord abrirAtencion(Long appointmentId, String sintomas) throws SQLException {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new EntityNotFoundException("Appointment", appointmentId);
        }

        Appointment appointment = appointmentOpt.get();

        if (sintomas == null || sintomas.isBlank()) {
            throw new ValidationException("Los síntomas son obligatorios para abrir la atención");
        }

        MedicalRecord medicalRecord = new MedicalRecord(
                appointment.getId(), appointment.getPetId(), appointment.getVeterinarianId(), sintomas);

        MedicalRecord guardado = medicalRecordRepository.save(medicalRecord);

        appointmentRepository.updateEstado(appointmentId, EstadoCita.EN_ATENCION.name());

        return guardado;
    }

    @Override
    public void finalizarAtencion(Long medicalRecordId, String diagnostico, String tratamiento,
                                  String observaciones, List<MedicalRecordMedicine> medicamentosUtilizados) throws SQLException {

        if (diagnostico == null || diagnostico.isBlank()) {
            throw new ValidationException("El diagnóstico es obligatorio para finalizar la atención");
        }

        Optional<MedicalRecord> medicalRecordOpt = medicalRecordRepository.findById(medicalRecordId);
        if (medicalRecordOpt.isEmpty()) {
            throw new EntityNotFoundException("MedicalRecord", medicalRecordId);
        }
        MedicalRecord medicalRecord = medicalRecordOpt.get();

        for (MedicalRecordMedicine mrm : medicamentosUtilizados) {
            Optional<Medicine> medicineOpt = medicineRepository.findById(mrm.getMedicineId());
            if (medicineOpt.isEmpty()) {
                throw new EntityNotFoundException("Medicine", mrm.getMedicineId());
            }
            Medicine medicine = medicineOpt.get();

            if (medicine.getCantidadDisponible() < mrm.getCantidadUtilizada()) {
                throw new InsufficientStockException(
                        medicine.getNombre(), medicine.getCantidadDisponible(), mrm.getCantidadUtilizada());
            }
        }

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            medicalRecord.setDiagnostico(diagnostico);
            medicalRecord.setTratamiento(tratamiento);
            medicalRecord.setObservaciones(observaciones);
            medicalRecordRepository.finalizar(medicalRecord, connection);

            for (MedicalRecordMedicine mrm : medicamentosUtilizados) {
                mrm.setMedicalRecordId(medicalRecord.getId());
                medicalRecordMedicineRepository.save(mrm, connection);
                medicineRepository.descontarStock(mrm.getMedicineId(), mrm.getCantidadUtilizada(), connection);
            }

            appointmentRepository.updateEstado(
                    medicalRecord.getAppointmentId(), EstadoCita.FINALIZADA.name(), connection);

            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;

        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    @Override
    public MedicalRecord buscarPorId(Long id) throws SQLException {
        Optional<MedicalRecord> resultado = medicalRecordRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new EntityNotFoundException("MedicalRecord", id);
        }

        return resultado.get();
    }

    @Override
    public List<MedicalRecord> listarPorMascota(Long petId) throws SQLException {
        return medicalRecordRepository.findByPetId(petId);
    }
}