package com.vetcare.controller;

import com.vetcare.model.Veterinarian;
import com.vetcare.service.VeterinarianService;
import com.vetcare.service.impl.VeterinarianServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class VeterinarianController {


    private final VeterinarianService veterinarianService;

    public VeterinarianController() {
        this.veterinarianService = new VeterinarianServiceImpl();
    }

    public Veterinarian registrarVeterinarian(Veterinarian veterinarian) throws SQLException {
        return veterinarianService.registrar(veterinarian);
    }

    public Veterinarian buscarVeterinarianPorId(Long id) throws SQLException {
        return veterinarianService.buscarPorId(id);
    }

    public List<Veterinarian> listarVeterinarians() throws SQLException {
        return veterinarianService.listarTodos();
    }

    public void actualizarVeterinarian(Veterinarian veterinarian) throws SQLException {
        veterinarianService.actualizar(veterinarian);
    }

    public void eliminarVeterinarian(Long id) throws SQLException {
        veterinarianService.eliminar(id);
    }
}
