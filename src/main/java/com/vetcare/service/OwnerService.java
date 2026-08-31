package com.vetcare.service;

import com.vetcare.model.Owner;
import java.sql.SQLException;
import java.util.List;

public interface OwnerService {

    Owner registrar(Owner owner) throws SQLException;

    Owner buscarPorId(Long id) throws SQLException;

    List<Owner> listarTodos() throws SQLException;

    void actualizar(Owner owner) throws SQLException;

    void eliminar(Long id) throws SQLException;
}
