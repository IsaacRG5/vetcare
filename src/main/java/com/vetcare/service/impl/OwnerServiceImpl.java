package com.vetcare.service.impl;

import com.vetcare.exception.DuplicateDataException;
import com.vetcare.exception.EntityNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.Owner;
import com.vetcare.repository.OwnerRepository;
import com.vetcare.repository.impl.OwnerRepositoryImpl;
import com.vetcare.service.OwnerService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl() {
        this.ownerRepository = new OwnerRepositoryImpl();
    }

    @Override
    public Owner registrar(Owner owner) throws SQLException {
        validarDatosBasicos(owner);

        if (ownerRepository.existsByNumeroIdentificacion(owner.getNumeroIdentificacion())) {
            throw new DuplicateDataException(
                    "Ya existe un propietario con la identificación: " + owner.getNumeroIdentificacion());
        }

        if (owner.getCorreo() != null && ownerRepository.existsByCorreo(owner.getCorreo())) {
            throw new DuplicateDataException(
                    "Ya existe un propietario con el correo: " + owner.getCorreo());
        }

        return ownerRepository.save(owner);
    }

    @Override
    public Owner buscarPorId(Long id) throws SQLException {
        Optional<Owner> resultado = ownerRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new EntityNotFoundException("Owner", id);
        }

        return resultado.get();
    }

    @Override
    public List<Owner> listarTodos() throws SQLException {
        return ownerRepository.findAll();
    }

    @Override
    public void actualizar(Owner owner) throws SQLException {
        buscarPorId(owner.getId());

        validarDatosBasicos(owner);

        ownerRepository.update(owner);
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        buscarPorId(id);

        ownerRepository.delete(id);
    }

    private void validarDatosBasicos(Owner owner) {
        if (owner.getTipoIdentificacion() == null || owner.getTipoIdentificacion().isBlank()) {
            throw new ValidationException("El tipo de identificación es obligatorio");
        }

        if (owner.getNumeroIdentificacion() == null || owner.getNumeroIdentificacion().isBlank()) {
            throw new ValidationException("El número de identificación es obligatorio");
        }

        if (owner.getNombreCompleto() == null || owner.getNombreCompleto().isBlank()) {
            throw new ValidationException("El nombre completo es obligatorio");
        }

        if (owner.getTelefono() == null || owner.getTelefono().isBlank()) {
            throw new ValidationException("El teléfono es obligatorio");
        }
    }
}