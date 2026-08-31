package com.vetcare.repository;

import com.vetcare.model.Medicine;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MedicineRepository {

    Medicine save(Medicine medicine) throws SQLException;

    Optional<Medicine> findById(Long id) throws SQLException;

    List<Medicine> findAll() throws SQLException;

    List<Medicine> findConStockBajo() throws SQLException;

    void update(Medicine medicine) throws SQLException;

    void delete(Long id) throws SQLException;

    boolean existsByCodigo(String codigo) throws SQLException;

    void descontarStock(Long medicineId, int cantidad, Connection connection) throws SQLException;

}