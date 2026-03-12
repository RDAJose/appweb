package com.appweb.backend.controllers;

import com.appweb.backend.models.User;
import com.appweb.backend.services.UserService;
import com.appweb.backend.dto.UserDTO;
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

    // Obtener todos los usuarios
    @GetMapping
public Page<User> getUsers(Pageable pageable) {
    return userService.getUsers(pageable);
}

    // Crear usuario
    @PostMapping
    public User createUser(@RequestBody @Valid UserDTO userDTO) {

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        return userService.createUser(user);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}