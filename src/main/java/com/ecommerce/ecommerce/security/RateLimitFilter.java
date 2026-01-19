package com.ecommerce.ecommerce.security;

import com.ecommerce.ecommerce.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets;
    private final JwtService jwtService;

    public RateLimitFilter(Map<String, Bucket> buckets, JwtService jwtService) {
        this.buckets = buckets;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String bucketType = determineBucketType(request);
        Bucket bucket = buckets.get(bucketType);

        if (bucket == null) {
            bucket = buckets.get(RateLimitConfig.BucketType.PUBLIC.name());
        }

        // Verifica se há tokens disponíveis
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    "error": "Rate limit exceeded",
                    "message": "Too many requests. Please try again later.",
                    "retryAfter": "60"
                }
                """);
        }
    }

    private String determineBucketType(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated();
        boolean isAdmin = false;

        if (isAuthenticated) {
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }

        // Determinar tipo de bucket baseado no endpoint
        if (path.contains("/api/imagens/upload") ||
                path.contains("/api/imagens/upload-multiplas")) {
            return RateLimitConfig.BucketType.UPLOAD.name();
        }

        if (isAdmin) {
            return RateLimitConfig.BucketType.ADMIN.name();
        }

        if (isAuthenticated) {
            return RateLimitConfig.BucketType.AUTHENTICATED.name();
        }

        return RateLimitConfig.BucketType.PUBLIC.name();
    }
}