package com.appweb.backend.controllers.auth;

import com.appweb.backend.dto.LoginRequest;
import com.appweb.backend.dto.RegisterRequest;
import com.appweb.backend.models.User;
import com.appweb.backend.models.Role;
import com.appweb.backend.security.JwtService;
import com.appweb.backend.services.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // password encriptado
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER);

        return userService.save(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        User user = userService.findByUsername(request.getUsername());

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password incorrecto");
        }

        String token = jwtService.generateToken(user.getUsername());

        return Map.of("token", token);
    }
}