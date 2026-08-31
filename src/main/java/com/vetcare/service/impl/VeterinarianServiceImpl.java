package com.vetcare.service.impl;

import com.vetcare.exception.DuplicateDataException;
import com.vetcare.exception.EntityNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Veterinarian;
import com.vetcare.repository.VeterinarianRepository;
import com.vetcare.repository.impl.VeterinarianRepositoryImpl;
import com.vetcare.service.VeterinarianService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class VeterinarianServiceImpl implements VeterinarianService {

    private final VeterinarianRepository veterinarianRepository;

    public VeterinarianServiceImpl() {
        this.veterinarianRepository = new VeterinarianRepositoryImpl();
    }

    @Override
    public Veterinarian registrar(Veterinarian veterinarian) throws SQLException {
        validarDatosBasicos(veterinarian);

        if (veterinarianRepository.existsByNumeroDocumento(veterinarian.getNumeroDocumento())) {
            throw new DuplicateDataException(
                    "Ya existe un veterinario con el documento: " + veterinarian.getNumeroDocumento());
        }

        if (veterinarianRepository.existsByTarjetaProfesional(veterinarian.getTarjetaProfesional())) {
            throw new DuplicateDataException(
                    "Ya existe un veterinario con la tarjeta profesional: " + veterinarian.getTarjetaProfesional());
        }

        if (veterinarian.getCorreo() != null && veterinarianRepository.existsByCorreo(veterinarian.getCorreo())) {
            throw new DuplicateDataException(
                    "Ya existe un veterinario con el correo: " + veterinarian.getCorreo());
        }

        return veterinarianRepository.save(veterinarian);
    }

    @Override
    public Veterinarian buscarPorId(Long id) throws SQLException {
        Optional<Veterinarian> resultado = veterinarianRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new EntityNotFoundException("Veterinarian", id);
        }

        return resultado.get();
    }

    @Override
    public List<Veterinarian> listarTodos() throws SQLException {
        return veterinarianRepository.findAll();
    }

    @Override
    public void actualizar(Veterinarian veterinarian) throws SQLException {
        buscarPorId(veterinarian.getId());

        validarDatosBasicos(veterinarian);

        veterinarianRepository.update(veterinarian);
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        buscarPorId(id);

        veterinarianRepository.delete(id);
    }

    private void validarDatosBasicos(Veterinarian veterinarian) {
        if (veterinarian.getNumeroDocumento() == null || veterinarian.getNumeroDocumento().isBlank()) {
            throw new ValidationException("El número de documento es obligatorio");
        }

        if (veterinarian.getNombre() == null || veterinarian.getNombre().isBlank()) {
            throw new ValidationException("El nombre es obligatorio");
        }

        if (veterinarian.getTarjetaProfesional() == null || veterinarian.getTarjetaProfesional().isBlank()) {
            throw new ValidationException("La tarjeta profesional es obligatoria");
        }

        if (veterinarian.getEspecialidad() == null) {
            throw new ValidationException("La especialidad es obligatoria");
        }
    }

}
