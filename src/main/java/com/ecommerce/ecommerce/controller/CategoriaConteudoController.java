package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CategoriaConteudoDTO;
import com.ecommerce.ecommerce.dto.CategoriaConteudoRequestDTO;
import com.ecommerce.ecommerce.dto.SlugValidationDTO;
import com.ecommerce.ecommerce.service.CategoriaConteudoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias-conteudo")
public class CategoriaConteudoController {

    private final CategoriaConteudoService categoriaService;

    public CategoriaConteudoController(CategoriaConteudoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // ============ ENDPOINTS PÚBLICOS ============

    @GetMapping
    public ResponseEntity<?> listarCategoriasAtivas() {
        List<CategoriaConteudoDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/com-conteudo")
    public ResponseEntity<?> listarCategoriasComConteudo() {
        List<CategoriaConteudoDTO> categorias = categoriaService.listarComConteudo();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        CategoriaConteudoDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> buscarPorSlug(@PathVariable String slug) {
        CategoriaConteudoDTO categoria = categoriaService.buscarPorSlug(slug);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/validar-slug")
    public ResponseEntity<?> validarSlug(@RequestParam String slug) {
        SlugValidationDTO validacao = categoriaService.validarSlug(slug);
        return ResponseEntity.ok(validacao);
    }

    // ============ ENDPOINTS ADMIN ============

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarCategoria(@Valid @RequestBody CategoriaConteudoRequestDTO dto) {
        CategoriaConteudoDTO categoria = categoriaService.criar(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Categoria criada com sucesso");
        response.put("categoria", categoria);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaConteudoRequestDTO dto) {

        CategoriaConteudoDTO categoria = categoriaService.atualizar(id, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Categoria atualizada com sucesso");
        response.put("categoria", categoria);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCategoria(@PathVariable Long id) {
        categoriaService.deletar(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestParam boolean ativo) {

        CategoriaConteudoDTO categoria = categoriaService.atualizarStatus(id, ativo);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Status da categoria atualizado com sucesso");
        response.put("categoria", categoria);

        return ResponseEntity.ok(response);
    }
}