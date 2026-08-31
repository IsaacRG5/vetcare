package com.vetcare.service;

import com.vetcare.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    User registrar(User user) throws SQLException;

    User autenticar(String username, String password) throws SQLException;

    User buscarPorId(Long id) throws SQLException;

    List<User> listarTodos() throws SQLException;

    void eliminar(Long id) throws SQLException;

}