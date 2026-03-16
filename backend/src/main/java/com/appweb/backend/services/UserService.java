package com.appweb.backend.services;

import com.appweb.backend.models.User;
import com.appweb.backend.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Crear usuario
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Guardar usuario (usado por register)
    public User save(User user) {
        return userRepository.save(user);
    }

    // Buscar por username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Obtener usuarios paginados
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    // Obtener usuario por id
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Eliminar usuario
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}