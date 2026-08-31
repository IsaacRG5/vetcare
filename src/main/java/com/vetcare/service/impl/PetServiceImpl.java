package com.vetcare.service.impl;

import com.vetcare.exception.EntityNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Pet;
import com.vetcare.repository.OwnerRepository;
import com.vetcare.repository.PetRepository;
import com.vetcare.repository.impl.OwnerRepositoryImpl;
import com.vetcare.repository.impl.PetRepositoryImpl;
import com.vetcare.service.PetService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    public PetServiceImpl() {
        this.petRepository = new PetRepositoryImpl();
        this.ownerRepository = new OwnerRepositoryImpl();
    }

    @Override
    public Pet registrar(Pet pet) throws SQLException {
        validarDatosBasicos(pet);

        Optional<com.vetcare.model.Owner> owner = ownerRepository.findById(pet.getOwnerId());
        if (owner.isEmpty()) {
            throw new EntityNotFoundException("Owner", pet.getOwnerId());
        }

        return petRepository.save(pet);
    }
    private void validarDatosBasicos(Pet pet) {
        if (pet.getNombre() == null || pet.getNombre().isBlank()) {
            throw new ValidationException("El nombre de la mascota es obligatorio");
        }

        if (pet.getFechaNacimiento() == null) {
            throw new ValidationException("La fecha de nacimiento es obligatoria");
        }

        if (pet.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new ValidationException("La fecha de nacimiento no puede ser una fecha futura");
        }

        if (pet.getPeso() == null || pet.getPeso() <= 0) {
            throw new ValidationException("El peso debe ser mayor a 0");
        }

        if (pet.getOwnerId() == null) {
            throw new ValidationException("La mascota debe tener un propietario asignado");
        }
    }


    @Override
    public Pet buscarPorId(Long id) throws SQLException {
        Optional<Pet> resultado = petRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new EntityNotFoundException("Pet", id);
        }

        return resultado.get();
    }

    @Override
    public List<Pet> listarTodos() throws SQLException {
        return petRepository.findAll();
    }

    @Override
    public List<Pet> listarPorPropietario(Long ownerId) throws SQLException {
        return petRepository.findByOwnerId(ownerId);
    }

    @Override
    public void actualizar(Pet pet) throws SQLException {
        buscarPorId(pet.getId());

        validarDatosBasicos(pet);

        petRepository.update(pet);
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        buscarPorId(id);

        petRepository.delete(id);
    }

}
