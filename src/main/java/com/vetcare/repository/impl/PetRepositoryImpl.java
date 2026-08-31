package com.vetcare.repository.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.model.Pet;
import com.vetcare.model.enums.EstadoGeneral;
import com.vetcare.model.enums.SexoMascota;
import com.vetcare.repository.PetRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetRepositoryImpl implements PetRepository {

    @Override
    public Pet save(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (nombre, especie, raza, sexo, fecha_nacimiento, peso, " +
                "owner_id, estado, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, pet.getNombre());
            statement.setString(2, pet.getEspecie());
            statement.setString(3, pet.getRaza());
            statement.setString(4, pet.getSexo().name().equals("MACHO") ? "M" : "H");
            statement.setDate(5, Date.valueOf(pet.getFechaNacimiento()));
            statement.setDouble(6, pet.getPeso());
            statement.setLong(7, pet.getOwnerId());
            statement.setString(8, pet.getEstado().name());
            statement.setDate(9, Date.valueOf(pet.getFechaRegistro()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pet.setId(generatedKeys.getLong(1));
                }
            }
        }

        return pet;
    }

    @Override
    public Optional<Pet> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM pets WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToPet(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Pet> findAll() throws SQLException {
        String sql = "SELECT * FROM pets ORDER BY id";
        List<Pet> pets = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                pets.add(mapRowToPet(resultSet));
            }
        }

        return pets;
    }

    @Override
    public List<Pet> findByOwnerId(Long ownerId) throws SQLException {
        String sql = "SELECT * FROM pets WHERE owner_id = ? ORDER BY id";
        List<Pet> pets = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, ownerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pets.add(mapRowToPet(resultSet));
                }
            }
        }

        return pets;
    }

    @Override
    public void update(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET nombre = ?, especie = ?, raza = ?, sexo = ?, " +
                "fecha_nacimiento = ?, peso = ?, owner_id = ?, estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, pet.getNombre());
            statement.setString(2, pet.getEspecie());
            statement.setString(3, pet.getRaza());
            statement.setString(4, pet.getSexo().name().equals("MACHO") ? "M" : "H");
            statement.setDate(5, Date.valueOf(pet.getFechaNacimiento()));
            statement.setDouble(6, pet.getPeso());
            statement.setLong(7, pet.getOwnerId());
            statement.setString(8, pet.getEstado().name());
            statement.setLong(9, pet.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE pets SET estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, EstadoGeneral.INACTIVO.name());
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    private Pet mapRowToPet(ResultSet resultSet) throws SQLException {
        Pet pet = new Pet();
        pet.setId(resultSet.getLong("id"));
        pet.setNombre(resultSet.getString("nombre"));
        pet.setEspecie(resultSet.getString("especie"));
        pet.setRaza(resultSet.getString("raza"));
        pet.setSexo(resultSet.getString("sexo").equals("M") ? SexoMascota.MACHO : SexoMascota.HEMBRA);
        pet.setFechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate());
        pet.setPeso(resultSet.getDouble("peso"));
        pet.setOwnerId(resultSet.getLong("owner_id"));
        pet.setEstado(EstadoGeneral.valueOf(resultSet.getString("estado")));
        pet.setFechaRegistro(resultSet.getDate("fecha_registro").toLocalDate());
        return pet;
    }
}