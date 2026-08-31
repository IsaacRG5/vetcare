package com.vetcare.controller;

import com.vetcare.model.Owner;
import com.vetcare.service.OwnerService;
import com.vetcare.service.impl.OwnerServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController() {
        this.ownerService = new OwnerServiceImpl();
    }

    public Owner registrarOwner(Owner owner) throws SQLException {
        return ownerService.registrar(owner);
    }

    public Owner buscarOwnerPorId(Long id) throws SQLException {
        return ownerService.buscarPorId(id);
    }

    public List<Owner> listarOwners() throws SQLException {
        return ownerService.listarTodos();
    }

    public void actualizarOwner(Owner owner) throws SQLException {
        ownerService.actualizar(owner);
    }

    public void eliminarOwner(Long id) throws SQLException {
        ownerService.eliminar(id);
    }
}
