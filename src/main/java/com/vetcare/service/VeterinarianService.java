package com.vetcare.service;


import com.vetcare.model.Veterinarian;
import java.sql.SQLException;
import java.util.List;


public interface VeterinarianService {

    Veterinarian registrar(Veterinarian veterinarian) throws SQLException;

    Veterinarian buscarPorId(Long id) throws SQLException;

    List<Veterinarian> listarTodos() throws SQLException;

    void actualizar(Veterinarian veterinarian) throws SQLException;

    void eliminar(Long id) throws SQLException;
}
