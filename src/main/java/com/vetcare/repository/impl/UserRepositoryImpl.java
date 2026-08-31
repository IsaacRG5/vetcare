package com.vetcare.repository.impl;

import com.vetcare.config.DatabaseConnection;
import com.vetcare.model.User;
import com.vetcare.model.enums.EstadoGeneral;
import com.vetcare.model.enums.Rol;
import com.vetcare.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, rol, estado) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRol().name());
            statement.setString(4, user.getEstado().name());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToUser(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToUser(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY id";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        }

        return users;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, rol = ?, estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRol().name());
            statement.setString(4, user.getEstado().name());
            statement.setLong(5, user.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "UPDATE users SET estado = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, EstadoGeneral.INACTIVO.name());
            statement.setLong(2, id);

            statement.executeUpdate();
        }
    }

    @Override
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setRol(Rol.valueOf(resultSet.getString("rol")));
        user.setEstado(EstadoGeneral.valueOf(resultSet.getString("estado")));
        return user;
    }
}