package com.vetcare.service.impl;

import com.vetcare.exception.DuplicateDataException;
import com.vetcare.exception.EntityNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Medicine;
import com.vetcare.repository.MedicineRepository;
import com.vetcare.repository.impl.MedicineRepositoryImpl;
import com.vetcare.service.MedicineService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineServiceImpl() {
        this.medicineRepository = new MedicineRepositoryImpl();
    }

    @Override
    public Medicine registrar(Medicine medicine) throws SQLException {
        validarDatosBasicos(medicine);

        if (medicineRepository.existsByCodigo(medicine.getCodigo())) {
            throw new DuplicateDataException(
                    "Ya existe un medicamento con el código: " + medicine.getCodigo());
        }

        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine buscarPorId(Long id) throws SQLException {
        Optional<Medicine> resultado = medicineRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new EntityNotFoundException("Medicine", id);
        }

        return resultado.get();
    }

    @Override
    public List<Medicine> listarTodos() throws SQLException {
        return medicineRepository.findAll();
    }

    @Override
    public List<Medicine> listarConStockBajo() throws SQLException {
        return medicineRepository.findConStockBajo();
    }

    @Override
    public void actualizar(Medicine medicine) throws SQLException {
        buscarPorId(medicine.getId());

        validarDatosBasicos(medicine);

        medicineRepository.update(medicine);
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        buscarPorId(id);

        medicineRepository.delete(id);
    }

    private void validarDatosBasicos(Medicine medicine) {
        if (medicine.getCodigo() == null || medicine.getCodigo().isBlank()) {
            throw new ValidationException("El código del medicamento es obligatorio");
        }

        if (medicine.getNombre() == null || medicine.getNombre().isBlank()) {
            throw new ValidationException("El nombre del medicamento es obligatorio");
        }

        if (medicine.getCantidadDisponible() == null || medicine.getCantidadDisponible() < 0) {
            throw new ValidationException("La cantidad disponible no puede ser negativa");
        }

        if (medicine.getPrecioUnitario() == null || medicine.getPrecioUnitario() <= 0) {
            throw new ValidationException("El precio unitario debe ser mayor a 0");
        }
    }
}