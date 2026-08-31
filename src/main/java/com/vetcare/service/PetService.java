package com.vetcare.service;

import com.vetcare.model.Pet;
import java.sql.SQLException;
import java.util.List;

public interface PetService {

    Pet registrar(Pet pet) throws SQLException;

    Pet buscarPorId(Long id) throws SQLException;

    List<Pet> listarTodos() throws SQLException;

    List<Pet> listarPorPropietario(Long ownerId) throws SQLException;

    void actualizar(Pet pet) throws SQLException;

    void eliminar(Long id) throws SQLException;

}
