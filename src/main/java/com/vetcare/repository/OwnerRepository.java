package com.vetcare.repository;

import com.vetcare.model.Owner;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OwnerRepository {

    Owner save(Owner owner) throws SQLException;

    Optional<Owner> findById(Long id) throws SQLException;

    List<Owner> findAll() throws SQLException;

    void update(Owner owner) throws SQLException;

    void delete(Long id) throws SQLException;

    boolean existsByNumeroIdentificacion(String numeroIdentificacion) throws SQLException;

    boolean existsByCorreo(String correo) throws SQLException;
}
