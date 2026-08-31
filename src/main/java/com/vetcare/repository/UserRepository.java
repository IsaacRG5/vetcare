
package com.vetcare.repository;

import com.vetcare.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user) throws SQLException;

    Optional<User> findById(Long id) throws SQLException;

    Optional<User> findByUsername(String username) throws SQLException;

    List<User> findAll() throws SQLException;

    void update(User user) throws SQLException;

    void delete(Long id) throws SQLException;

    boolean existsByUsername(String username) throws SQLException;

}