package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.LoginRequestDTO;
import com.ecommerce.ecommerce.dto.LoginResponseDTO;
import com.ecommerce.ecommerce.dto.RegisterRequestDTO;
import com.ecommerce.ecommerce.dto.RegisterResponseDTO;
import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.exception.UnauthorizedException;
import com.ecommerce.ecommerce.repository.RoleRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenha())) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }

        // ✅ Usa o método que recebe User (não String)
        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(token);
    }

    public RegisterResponseDTO register(RegisterRequestDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL_ALREADY_USED");
        }
        Role roleCliente = roleRepository.findByNome("ROLE_CLIENTE")
                .orElseGet(() -> {
                    Role novaRole = new Role("ROLE_CLIENTE");
                    return roleRepository.save(novaRole);
                });

        // Cria também a role ADMIN se não existir
        roleRepository.findByNome("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role novaRole = new Role("ROLE_ADMIN");
                    return roleRepository.save(novaRole);
                });

        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setRoles(Set.of(roleCliente));

        User salvo = userRepository.save(user);

        return new RegisterResponseDTO(
                salvo.getId(),
                salvo.getEmail()
        );
    }
}
