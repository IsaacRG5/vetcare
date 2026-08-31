package com.vetcare.controller;

import com.vetcare.model.User;
import com.vetcare.service.UserService;
import com.vetcare.service.impl.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class UserController {

    private final UserService userService;

    public UserController() {
        this.userService = new UserServiceImpl();
    }

    public User registrarUser(User user) throws SQLException {
        return userService.registrar(user);
    }

    public User login(String username, String password) throws SQLException {
        return userService.autenticar(username, password);
    }

    public User buscarUserPorId(Long id) throws SQLException {
        return userService.buscarPorId(id);
    }

    public List<User> listarUsers() throws SQLException {
        return userService.listarTodos();
    }

    public void eliminarUser(Long id) throws SQLException {
        userService.eliminar(id);
    }
}