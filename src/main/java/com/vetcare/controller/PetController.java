package com.vetcare.controller;
import com.vetcare.model.Pet;
import com.vetcare.service.PetService;
import com.vetcare.service.impl.PetServiceImpl;

import java.sql.SQLException;
import java.util.List;


public class PetController {

    private final PetService petService;

    public PetController() {
        this.petService = new PetServiceImpl();
    }

    public Pet registrarPet(Pet pet) throws SQLException {
        return petService.registrar(pet);
    }

    public Pet buscarPetPorId(Long id) throws SQLException {
        return petService.buscarPorId(id);
    }

    public List<Pet> listarPets() throws SQLException {
        return petService.listarTodos();
    }

    public List<Pet> listarPetsPorPropietario(Long ownerId) throws SQLException {
        return petService.listarPorPropietario(ownerId);
    }

    public void actualizarPet(Pet pet) throws SQLException {
        petService.actualizar(pet);
    }

    public void eliminarPet(Long id) throws SQLException {
        petService.eliminar(id);
    }
}
