package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.ProdutoRequestDTO;
import com.ecommerce.ecommerce.dto.ProdutoResponseDTO;
import com.ecommerce.ecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

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
    logger.info("[ProdutoController] Recebendo request de criar produto");
    logger.info("Nome: {}", dto.getNome());
    logger.info("Preço: {}", dto.getPreco());
    logger.info("Categoria ID: {}", dto.getCategoriaId());
    logger.info("Estoque: {}", dto.getQuantidadeEstoque());
    logger.info("DTO completo: {}", dto.toString());
    
    try {
        ProdutoResponseDTO resposta = produtoService.criar(dto);
        logger.info("[ProdutoController] Produto criado com id={}", resposta.getId());
        return resposta;
    } catch (Exception e) {
        logger.error("Erro ao criar produto", e);
        throw e;
    }
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
