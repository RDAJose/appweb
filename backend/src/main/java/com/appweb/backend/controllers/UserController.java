package com.appweb.backend.controllers;

import com.appweb.backend.models.User;
import com.appweb.backend.services.UserService;
import com.appweb.backend.dto.UserDTO;
import com.appweb.backend.dto.UserResponseDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Obtener usuarios (sin mostrar password)
    @GetMapping
    public Page<UserResponseDTO> getUsers(Pageable pageable) {

        Page<User> users = userService.getUsers(pageable);

        return users.map(user -> new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        ));
    }

    // Crear usuario
    @PostMapping
    public User createUser(@RequestBody @Valid UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        return userService.createUser(user);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}