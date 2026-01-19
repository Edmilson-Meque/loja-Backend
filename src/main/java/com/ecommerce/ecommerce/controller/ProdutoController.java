package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.ProdutoRequestDTO;
import com.ecommerce.ecommerce.dto.ProdutoResponseDTO;
import com.ecommerce.ecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // ENDPOINTS PÚBLICOS
    @GetMapping
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<ProdutoResponseDTO> buscarPorCategoria(@PathVariable Long categoriaId) {
        return produtoService.buscarPorCategoria(categoriaId);
    }

    @GetMapping("/promocoes")
    public List<ProdutoResponseDTO> buscarEmPromocao() {
        return produtoService.buscarEmPromocao();
    }

    @GetMapping("/ofertas-do-dia")
    public List<ProdutoResponseDTO> buscarOfertasDoDia() {
        return produtoService.buscarOfertasDoDia();
    }

    @GetMapping("/destaques")
    public List<ProdutoResponseDTO> buscarEmDestaque() {
        return produtoService.buscarEmDestaque();
    }

    // ENDPOINTS ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return produtoService.criar(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProdutoResponseDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequestDTO dto) {
        return produtoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }
}