package com.vetcare.repository.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.model.Owner;
import com.vetcare.model.enums.EstadoGeneral;
import com.vetcare.repository.OwnerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerRepositoryImpl implements OwnerRepository {

    @Override
    public Owner save(Owner owner) throws SQLException {
        String sql = "INSERT INTO owners (tipo_identificacion, numero_identificacion, nombre_completo, " +
                "telefono, correo, direccion, estado, fecha_registro) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, owner.getTipoIdentificacion());
            statement.setString(2, owner.getNumeroIdentificacion());
            statement.setString(3, owner.getNombreCompleto());
            statement.setString(4, owner.getTelefono());
            statement.setString(5, owner.getCorreo());
            statement.setString(6, owner.getDireccion());
            statement.setString(7, owner.getEstado().name());
            statement.setDate(8, Date.valueOf(owner.getFechaRegistro()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    owner.setId(generatedKeys.getLong(1));
                }
            }
        }

        return owner;
    }

    @Override
    public Optional<Owner> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM owners WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToOwner(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Owner> findAll() throws SQLException {
        String sql = "SELECT * FROM owners ORDER BY id";
        List<Owner> owners = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                owners.add(mapRowToOwner(resultSet));
            }
        }

        return owners;
    }

    @Override
    public void update(Owner owner) throws SQLException {
        String sql = "UPDATE owners SET tipo_identificacion = ?, numero_identificacion = ?, " +
                "nombre_completo = ?, telefono = ?, correo = ?, direccion = ?, estado = ? " +
                "WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, owner.getTipoIdentificacion());
            statement.setString(2, owner.getNumeroIdentificacion());
            statement.setString(3, owner.getNombreCompleto());
            statement.setString(4, owner.getTelefono());
            statement.setString(5, owner.getCorreo());
            statement.setString(6, owner.getDireccion());
            statement.setString(7, owner.getEstado().name());
            statement.setLong(8, owner.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE owners SET estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, EstadoGeneral.INACTIVO.name());
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    @Override
    public boolean existsByNumeroIdentificacion(String numeroIdentificacion) throws SQLException {
        String sql = "SELECT 1 FROM owners WHERE numero_identificacion = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroIdentificacion);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean existsByCorreo(String correo) throws SQLException {
        String sql = "SELECT 1 FROM owners WHERE correo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, correo);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private Owner mapRowToOwner(ResultSet resultSet) throws SQLException {
        Owner owner = new Owner();
        owner.setId(resultSet.getLong("id"));
        owner.setTipoIdentificacion(resultSet.getString("tipo_identificacion"));
        owner.setNumeroIdentificacion(resultSet.getString("numero_identificacion"));
        owner.setNombreCompleto(resultSet.getString("nombre_completo"));
        owner.setTelefono(resultSet.getString("telefono"));
        owner.setCorreo(resultSet.getString("correo"));
        owner.setDireccion(resultSet.getString("direccion"));
        owner.setEstado(EstadoGeneral.valueOf(resultSet.getString("estado")));
        owner.setFechaRegistro(resultSet.getDate("fecha_registro").toLocalDate());
        return owner;
    }
}