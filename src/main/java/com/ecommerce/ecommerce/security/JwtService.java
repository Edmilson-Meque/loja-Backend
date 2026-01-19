package com.ecommerce.ecommerce.security;

import com.ecommerce.ecommerce.entity.Role;
import com.ecommerce.ecommerce.entity.User;
import com.ecommerce.ecommerce.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // ✅ CHAVE SECRETA VÁLIDA PARA BASE64 (sem caracteres especiais)
    private final String SECRET_STRING = "MinhaChaveSuperSecretaParaJWTEcommerce2024Segura1234567890";

    // ✅ Cria uma chave válida para HS256
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    private final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 horas

    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(User user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getNome)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY) // ✅ Usa SecretKey ao invés de String
                .compact();
    }

    public String generateToken(String email) {
        // Buscar usuário para pegar roles
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            return generateToken(userOpt.get());
        }

        // Se não encontrar usuário, cria token sem roles
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<?> roles = claims.get("roles", List.class);

        if (roles == null) {
            return List.of("ROLE_CLIENTE");
        }

        return roles.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public boolean isTokenValid(String token) {
        try {
            return !extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && isTokenValid(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String email = extractEmail(token);
        List<String> roles = extractRoles(token);

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}