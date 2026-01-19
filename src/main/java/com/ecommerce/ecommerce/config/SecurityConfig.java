package com.ecommerce.ecommerce.config;

import com.ecommerce.ecommerce.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ← ADICIONE ESTA LINHA
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/test").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/categorias").permitAll()
                        .requestMatchers("/api/categorias/{id}").permitAll()
                        .requestMatchers("/api/produtos").permitAll()
                        .requestMatchers("/api/produtos/{id}").permitAll()
                        .requestMatchers("/api/produtos/categoria/**").permitAll()
                        .requestMatchers("/api/produtos/promocoes").permitAll()
                        .requestMatchers("/api/produtos/ofertas-do-dia").permitAll()
                        .requestMatchers("/api/produtos/destaques").permitAll()

                        // Imagens - endpoints públicos para visualização
                        .requestMatchers("/api/imagens/health").permitAll()
                        .requestMatchers("/api/imagens/ping").permitAll()
                        .requestMatchers("/api/imagens/produto/**").permitAll()
                        .requestMatchers("/api/imagens/{imageId}").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Rotas que exigem autenticação (qualquer role)
                        .requestMatchers("/api/favoritos/**").authenticated()
                        .requestMatchers("/api/carrinho/**").authenticated()
                        .requestMatchers("/api/pedidos/**").authenticated()

                        // Rotas de upload - apenas ADMIN
                        .requestMatchers("/api/imagens/upload**").hasRole("ADMIN")
                        .requestMatchers("/api/imagens/upload-multiplas/**").hasRole("ADMIN")
                        .requestMatchers("/api/imagens/{imageId}/**").hasRole("ADMIN")
                        .requestMatchers("/api/imagens/produto/{produtoId}/todas").hasRole("ADMIN")

                        .requestMatchers("/api/conteudos/**").permitAll()
                        .requestMatchers("/api/categorias-conteudo/**").permitAll()
                        .requestMatchers("/api/tags/**").permitAll()
                        .requestMatchers("/api/contatos").permitAll() //

                        .requestMatchers("/api/conteudos/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/categorias-conteudo/**").hasRole("ADMIN") // Exceto GETs públicos
                        .requestMatchers("/api/tags/**").hasRole("ADMIN") // Exceto GETs públicos
                        .requestMatchers("/api/contatos/**").hasRole("ADMIN") // Exceto POST
                        // Demais rotas exigem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    // ← ADICIONE ESTE MÉTODO PARA CORS ↓
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir origens para testes locais
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost",
                "http://127.0.0.1:5500",  // Live Server do VS Code
                "http://localhost:5500",   // Live Server
                "http://127.0.0.1:8080",   // Backend
                "http://localhost:8080",   // Backend
                "file://",                  // Arquivos locais
                "null"                      // Arquivos abertos diretamente
        ));

        // Permitir métodos HTTP
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Permitir todos os headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Expor headers específicos
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        // Permitir credenciais (cookies, tokens)
        configuration.setAllowCredentials(true);

        // Tempo de cache para preflight requests
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}