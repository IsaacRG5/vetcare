package com.vetcare.controller;

import com.vetcare.model.Medicine;
import com.vetcare.service.MedicineService;
import com.vetcare.service.impl.MedicineServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController() {
        this.medicineService = new MedicineServiceImpl();
    }

    public Medicine registrarMedicine(Medicine medicine) throws SQLException {
        return medicineService.registrar(medicine);
    }

    public Medicine buscarMedicinePorId(Long id) throws SQLException {
        return medicineService.buscarPorId(id);
    }

    public List<Medicine> listarMedicines() throws SQLException {
        return medicineService.listarTodos();
    }

    public List<Medicine> listarMedicinesConStockBajo() throws SQLException {
        return medicineService.listarConStockBajo();
    }

    public void actualizarMedicine(Medicine medicine) throws SQLException {
        medicineService.actualizar(medicine);
    }

    public void eliminarMedicine(Long id) throws SQLException {
        medicineService.eliminar(id);
    }
}