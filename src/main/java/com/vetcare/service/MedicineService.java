package com.vetcare.service;
import com.vetcare.model.Medicine;
import java.sql.SQLException;
import java.util.List;



public interface MedicineService {
    Medicine registrar(Medicine medicine) throws SQLException;

    Medicine buscarPorId(Long id) throws SQLException;

    List<Medicine> listarTodos() throws SQLException;

    List<Medicine> listarConStockBajo() throws SQLException;

    void actualizar(Medicine medicine) throws SQLException;

    void eliminar(Long id) throws SQLException;

}