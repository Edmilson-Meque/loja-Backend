package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.FavoriteResponseDTO;
import com.ecommerce.ecommerce.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/produto/{produtoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteResponseDTO adicionarFavorito(@PathVariable Long produtoId) {
        return favoriteService.adicionarFavorito(produtoId);
    }

    @GetMapping
    public List<FavoriteResponseDTO> listarFavoritos() {
        return favoriteService.listarFavoritos();
    }

    @DeleteMapping("/produto/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerFavorito(@PathVariable Long produtoId) {
        favoriteService.removerFavorito(produtoId);
    }

    @GetMapping("/produto/{produtoId}/check")
    public boolean isFavorito(@PathVariable Long produtoId) {
        return favoriteService.isFavorito(produtoId);
    }

    @GetMapping("/count")
    public long contarFavoritos() {
        return favoriteService.contarFavoritos();
    }

    @GetMapping("/usuario/{usuarioId}/produto/{produtoId}/check")
    public boolean isFavoritoPorUsuario(@PathVariable Long usuarioId, @PathVariable Long produtoId) {
        return favoriteService.isFavoritoByUsuarioId(usuarioId, produtoId);
    }
}