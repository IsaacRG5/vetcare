
package com.vetcare.repository;

import com.vetcare.model.Veterinarian;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface VeterinarianRepository {

    Veterinarian save(Veterinarian veterinarian) throws SQLException;

    Optional<Veterinarian> findById(Long id) throws SQLException;

    List<Veterinarian> findAll() throws SQLException;

    void update(Veterinarian veterinarian) throws SQLException;

    void delete(Long id) throws SQLException;

    boolean existsByNumeroDocumento(String numeroDocumento) throws SQLException;

    boolean existsByTarjetaProfesional(String tarjetaProfesional) throws SQLException;

    boolean existsByCorreo(String correo) throws SQLException;

}