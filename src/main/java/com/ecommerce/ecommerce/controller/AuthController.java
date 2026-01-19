package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.LoginRequestDTO;
import com.ecommerce.ecommerce.dto.LoginResponseDTO;
import com.ecommerce.ecommerce.service.AuthService;
import com.ecommerce.ecommerce.dto.RegisterRequestDTO;
import com.ecommerce.ecommerce.dto.RegisterResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }
    @PostMapping("/register")
    public RegisterResponseDTO register(@Valid @RequestBody RegisterRequestDTO dto) {
        return authService.register(dto);
    }
}