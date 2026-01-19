package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CategoryRequestDTO;
import com.ecommerce.ecommerce.dto.CategoryResponseDTO;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import com.ecommerce.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    // PUBLIC ENDPOINTS
    @GetMapping
    public List<CategoryResponseDTO> listarTodas() {
        try {
            System.out.println("[CategoryController] GET /api/categorias chamado");
            List<CategoryResponseDTO> resultado = categoryService.listarTodas();
            System.out.println("[CategoryController] Retornando " + resultado.size() + " categorias");
            return resultado;
        } catch (Exception e) {
            System.err.println("[CategoryController] ERRO em listarTodas: " + e.getMessage());
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

    // DEBUG ENDPOINT
    @GetMapping("/debug/status")
    public Map<String, Object> debugStatus() {
        Map<String, Object> status = new HashMap<>();
        long totalCategorias = categoryRepository.count();
        status.put("total_categorias", totalCategorias);
        status.put("timestamp", System.currentTimeMillis());
        status.put("status", "OK");
        System.out.println("[DEBUG] Status check - Total categorias: " + totalCategorias);
        return status;
    }
}