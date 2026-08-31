package com.vetcare.repository;

import com.vetcare.model.Pet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PetRepository {

    Pet save(Pet pet) throws SQLException;

    Optional<Pet> findById(Long id) throws SQLException;

    List<Pet> findAll() throws SQLException;

    List<Pet> findByOwnerId(Long ownerId) throws SQLException;

    void update(Pet pet) throws SQLException;

    void delete(Long id) throws SQLException;

}