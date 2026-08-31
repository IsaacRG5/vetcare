package com.vetcare.service.impl;

import com.vetcare.exception.DuplicateDataException;
import com.vetcare.exception.EntityNotFoundException;
import com.vetcare.exception.ValidationException;
import com.vetcare.model.User;
import com.vetcare.repository.UserRepository;
import com.vetcare.repository.impl.UserRepositoryImpl;
import com.vetcare.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public User registrar(User user) throws SQLException {
        validarDatosBasicos(user);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateDataException(
                    "Ya existe un usuario con el nombre de usuario: " + user.getUsername());
        }

        String passwordHasheado = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(passwordHasheado);

        return userRepository.save(user);
    }

    @Override
    public User autenticar(String username, String password) throws SQLException {
        Optional<User> resultado = userRepository.findByUsername(username);

        if (resultado.isEmpty()) {
            throw new ValidationException("Usuario o contraseña incorrectos");
        }

        User user = resultado.get();

        boolean passwordCorrecto = BCrypt.checkpw(password, user.getPassword());

        if (!passwordCorrecto) {
            throw new ValidationException("Usuario o contraseña incorrectos");
        }

        return user;
    }

    @Override
    public User buscarPorId(Long id) throws SQLException {
        Optional<User> resultado = userRepository.findById(id);

        if (resultado.isEmpty()) {
            throw new EntityNotFoundException("User", id);
        }

        return resultado.get();
    }

    @Override
    public List<User> listarTodos() throws SQLException {
        return userRepository.findAll();
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        buscarPorId(id);

        userRepository.delete(id);
    }

    private void validarDatosBasicos(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ValidationException("El nombre de usuario es obligatorio");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ValidationException("La contraseña es obligatoria");
        }

        if (user.getPassword().length() < 6) {
            throw new ValidationException("La contraseña debe tener al menos 6 caracteres");
        }

        if (user.getRol() == null) {
            throw new ValidationException("El rol del usuario es obligatorio");
        }
    }
}