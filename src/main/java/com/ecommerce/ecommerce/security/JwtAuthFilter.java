package com.ecommerce.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
        String path = request.getRequestURI();

// ✅ pula recursos públicos
if (path.startsWith("/uploads/")
        || path.startsWith("/products/")
        || path.startsWith("/api/produtos")
        || path.startsWith("/api/categorias")
        || path.startsWith("/api/imagens/health")
        || path.startsWith("/api/imagens/ping")) {
    filterChain.doFilter(request, response);
    return;
}

// Se não tem Authorization, não bloqueia: só segue
String authHeader = request.getHeader("Authorization");
if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    filterChain.doFilter(request, response);
    return;
}

    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Se não tem token, continua
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractEmail(jwt);

            // Se tem email e não está autenticado
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Valida o token
                if (jwtService.isTokenValid(jwt)) {
                    // Cria autenticação
                    UsernamePasswordAuthenticationToken authToken = jwtService.getAuthentication(jwt);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao processar JWT: " + e.getMessage());
            // Não limpa o contexto de segurança
        }

        filterChain.doFilter(request, response);
    }
}
