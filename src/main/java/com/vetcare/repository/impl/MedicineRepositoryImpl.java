package com.vetcare.repository.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.model.Medicine;
import com.vetcare.model.enums.EstadoGeneral;
import com.vetcare.repository.MedicineRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicineRepositoryImpl implements MedicineRepository {

    @Override
    public Medicine save(Medicine medicine) throws SQLException {
        String sql = "INSERT INTO medicines (codigo, nombre, presentacion, laboratorio, " +
                "cantidad_disponible, cantidad_minima, precio_unitario, estado, fecha_registro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, medicine.getCodigo());
            statement.setString(2, medicine.getNombre());
            statement.setString(3, medicine.getPresentacion());
            statement.setString(4, medicine.getLaboratorio());
            statement.setInt(5, medicine.getCantidadDisponible());
            statement.setInt(6, medicine.getCantidadMinima());
            statement.setDouble(7, medicine.getPrecioUnitario());
            statement.setString(8, medicine.getEstado().name());
            statement.setDate(9, Date.valueOf(medicine.getFechaRegistro()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medicine.setId(generatedKeys.getLong(1));
                }
            }
        }

        return medicine;
    }

    @Override
    public Optional<Medicine> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM medicines WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToMedicine(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Medicine> findAll() throws SQLException {
        String sql = "SELECT * FROM medicines ORDER BY id";
        List<Medicine> medicines = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                medicines.add(mapRowToMedicine(resultSet));
            }
        }

        return medicines;
    }

    @Override
    public List<Medicine> findConStockBajo() throws SQLException {
        String sql = "SELECT * FROM medicines WHERE cantidad_disponible <= cantidad_minima " +
                "AND estado = 'ACTIVO' ORDER BY cantidad_disponible";
        List<Medicine> medicines = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                medicines.add(mapRowToMedicine(resultSet));
            }
        }

        return medicines;
    }

    @Override
    public void update(Medicine medicine) throws SQLException {
        String sql = "UPDATE medicines SET codigo = ?, nombre = ?, presentacion = ?, laboratorio = ?, " +
                "cantidad_disponible = ?, cantidad_minima = ?, precio_unitario = ?, estado = ? " +
                "WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, medicine.getCodigo());
            statement.setString(2, medicine.getNombre());
            statement.setString(3, medicine.getPresentacion());
            statement.setString(4, medicine.getLaboratorio());
            statement.setInt(5, medicine.getCantidadDisponible());
            statement.setInt(6, medicine.getCantidadMinima());
            statement.setDouble(7, medicine.getPrecioUnitario());
            statement.setString(8, medicine.getEstado().name());
            statement.setLong(9, medicine.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE medicines SET estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, EstadoGeneral.INACTIVO.name());
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    @Override
    public boolean existsByCodigo(String codigo) throws SQLException {
        String sql = "SELECT 1 FROM medicines WHERE codigo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, codigo);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public void descontarStock(Long medicineId, int cantidad, Connection connection) throws SQLException {
        String sql = "UPDATE medicines SET cantidad_disponible = cantidad_disponible - ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cantidad);
            statement.setLong(2, medicineId);

            statement.executeUpdate();
        }
    }

    private Medicine mapRowToMedicine(ResultSet resultSet) throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setId(resultSet.getLong("id"));
        medicine.setCodigo(resultSet.getString("codigo"));
        medicine.setNombre(resultSet.getString("nombre"));
        medicine.setPresentacion(resultSet.getString("presentacion"));
        medicine.setLaboratorio(resultSet.getString("laboratorio"));
        medicine.setCantidadDisponible(resultSet.getInt("cantidad_disponible"));
        medicine.setCantidadMinima(resultSet.getInt("cantidad_minima"));
        medicine.setPrecioUnitario(resultSet.getDouble("precio_unitario"));
        medicine.setEstado(EstadoGeneral.valueOf(resultSet.getString("estado")));
        medicine.setFechaRegistro(resultSet.getDate("fecha_registro").toLocalDate());
        return medicine;
    }
}