package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CategoryRequestDTO;
import com.ecommerce.ecommerce.dto.CategoryResponseDTO;
import com.ecommerce.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // PUBLIC ENDPOINTS
    @GetMapping
    public List<CategoryResponseDTO> listarTodas() {
        System.out.println("[CategoryController] GET /api/categorias - Iniciando");
        try {
            List<CategoryResponseDTO> resultado = categoryService.listarTodas();
            System.out.println("[CategoryController] Retornando " + resultado.size() + " categorias");
            return resultado;
        } catch (Exception e) {
            System.err.println("[CategoryController] ERRO: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/listar-simples")
    public List<CategoryResponseDTO> listarSimples() {
        System.out.println("[CategoryController] GET /api/categorias/listar-simples - Iniciando (SEM ACESSAR PRODUTOS)");
        try {
            List<CategoryResponseDTO> resultado = categoryService.listarSimples();
            System.out.println("[CategoryController] Retornando " + resultado.size() + " categorias (simples)");
            return resultado;
        } catch (Exception e) {
            System.err.println("[CategoryController] ERRO no listarSimples: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO buscarPorId(@PathVariable Long id) {
        return categoryService.buscarPorId(id);
    }

    // ADMIN ENDPOINTS
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDTO criar(@Valid @RequestBody CategoryRequestDTO dto) {
        return categoryService.criar(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO dto) {
        return categoryService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        categoryService.deletar(id);
    }
}
