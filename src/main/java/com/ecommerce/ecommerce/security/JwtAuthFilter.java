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

    /**
     * ✅ Rotas que NÃO devem passar por validação JWT.
     * Isso evita 403 em recursos estáticos como /products/** e /uploads/**.
     */
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();

        // ✅ Preflight (CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // ✅ Arquivos estáticos (imagens)
        if (path.startsWith("/products/")) return true;
        if (path.startsWith("/uploads/")) return true;

        // ✅ Rotas públicas do teu sistema
        if (path.startsWith("/api/auth")) return true;
        if (path.startsWith("/api/produtos")) return true;
        if (path.startsWith("/api/categorias")) return true;

        // ✅ Imagens públicas (endpoints)
        if (path.startsWith("/api/imagens/health")) return true;
        if (path.startsWith("/api/imagens/ping")) return true;
        if (path.startsWith("/api/imagens/produto/")) return true;
        // Se você quiser deixar /api/imagens/{id} público também:
        if (path.startsWith("/api/imagens/")) return true;

        // ✅ Outros comuns (se tiver swagger)
        if (path.startsWith("/swagger-ui")) return true;
        if (path.startsWith("/v3/api-docs")) return true;

        // ✅ Erro padrão do Spring
        if (path.equals("/error")) return true;

        return false;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // ✅ Sem token: não bloqueia, só segue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            String userEmail = jwtService.extractEmail(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtService.isTokenValid(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = jwtService.getAuthentication(jwt);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // ✅ Importante: NÃO retornar 403 aqui.
            // Apenas não autentica e deixa seguir para o Spring Security decidir.
            logger.error("Erro ao processar JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
