
package com.vetcare.repository.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.model.Veterinarian;
import com.vetcare.model.enums.EstadoGeneral;
import com.vetcare.model.enums.Especialidad;
import com.vetcare.repository.VeterinarianRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VeterinarianRepositoryImpl implements VeterinarianRepository {

    @Override
    public Veterinarian save(Veterinarian veterinarian) throws SQLException {
        String sql = "INSERT INTO veterinarians (numero_documento, nombre, tarjeta_profesional, " +
                "especialidad, telefono, correo, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, veterinarian.getNumeroDocumento());
            statement.setString(2, veterinarian.getNombre());
            statement.setString(3, veterinarian.getTarjetaProfesional());
            statement.setString(4, veterinarian.getEspecialidad().name());
            statement.setString(5, veterinarian.getTelefono());
            statement.setString(6, veterinarian.getCorreo());
            statement.setString(7, veterinarian.getEstado().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    veterinarian.setId(generatedKeys.getLong(1));
                }
            }
        }

        return veterinarian;
    }

    @Override
    public Optional<Veterinarian> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM veterinarians WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToVeterinarian(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Veterinarian> findAll() throws SQLException {
        String sql = "SELECT * FROM veterinarians ORDER BY id";
        List<Veterinarian> veterinarians = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                veterinarians.add(mapRowToVeterinarian(resultSet));
            }
        }

        return veterinarians;
    }

    @Override
    public void update(Veterinarian veterinarian) throws SQLException {
        String sql = "UPDATE veterinarians SET numero_documento = ?, nombre = ?, " +
                "tarjeta_profesional = ?, especialidad = ?, telefono = ?, correo = ?, estado = ? " +
                "WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, veterinarian.getNumeroDocumento());
            statement.setString(2, veterinarian.getNombre());
            statement.setString(3, veterinarian.getTarjetaProfesional());
            statement.setString(4, veterinarian.getEspecialidad().name());
            statement.setString(5, veterinarian.getTelefono());
            statement.setString(6, veterinarian.getCorreo());
            statement.setString(7, veterinarian.getEstado().name());
            statement.setLong(8, veterinarian.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE veterinarians SET estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, EstadoGeneral.INACTIVO.name());
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    @Override
    public boolean existsByNumeroDocumento(String numeroDocumento) throws SQLException {
        String sql = "SELECT 1 FROM veterinarians WHERE numero_documento = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroDocumento);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean existsByTarjetaProfesional(String tarjetaProfesional) throws SQLException {
        String sql = "SELECT 1 FROM veterinarians WHERE tarjeta_profesional = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tarjetaProfesional);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean existsByCorreo(String correo) throws SQLException {
        String sql = "SELECT 1 FROM veterinarians WHERE correo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, correo);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private Veterinarian mapRowToVeterinarian(ResultSet resultSet) throws SQLException {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(resultSet.getLong("id"));
        veterinarian.setNumeroDocumento(resultSet.getString("numero_documento"));
        veterinarian.setNombre(resultSet.getString("nombre"));
        veterinarian.setTarjetaProfesional(resultSet.getString("tarjeta_profesional"));
        veterinarian.setEspecialidad(Especialidad.valueOf(resultSet.getString("especialidad")));
        veterinarian.setTelefono(resultSet.getString("telefono"));
        veterinarian.setCorreo(resultSet.getString("correo"));
        veterinarian.setEstado(EstadoGeneral.valueOf(resultSet.getString("estado")));
        return veterinarian;
    }
}