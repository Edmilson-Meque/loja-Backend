package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.*;
import com.ecommerce.ecommerce.entity.TipoConteudo;
import com.ecommerce.ecommerce.service.ConteudoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conteudos")
public class ConteudoController {

    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService) {
        this.conteudoService = conteudoService;
    }

    // ============ ENDPOINTS PÚBLICOS ============

    @GetMapping
    public ResponseEntity<?> listarConteudosPublicos(
            @RequestParam(required = false) TipoConteudo tipo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {

        BuscaConteudoDTO filtros = new BuscaConteudoDTO();
        filtros.setTipo(tipo);
        filtros.setAtivo(true); // Apenas conteúdos ativos
        filtros.setPagina(pagina);
        filtros.setTamanho(tamanho);

        PaginatedResponseDTO<ConteudoSimplificadoDTO> conteudos =
                conteudoService.buscarConteudos(filtros);

        return ResponseEntity.ok(conteudos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ConteudoResponseDTO conteudo = conteudoService.buscarPorId(id);
        return ResponseEntity.ok(conteudo);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> buscarPorSlug(@PathVariable String slug) {
        ConteudoResponseDTO conteudo = conteudoService.buscarPorSlug(slug);
        return ResponseEntity.ok(conteudo);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> listarPorTipo(@PathVariable TipoConteudo tipo) {
        List<ConteudoSimplificadoDTO> conteudos = conteudoService.listarPorTipo(tipo);
        return ResponseEntity.ok(conteudos);
    }

    @GetMapping("/destaques")
    public ResponseEntity<?> listarDestaques() {
        List<ConteudoSimplificadoDTO> destaques = conteudoService.listarDestaques();
        return ResponseEntity.ok(destaques);
    }

    @GetMapping("/recentes")
    public ResponseEntity<?> listarRecentes(
            @RequestParam(defaultValue = "5") int limite) {
        List<ConteudoSimplificadoDTO> recentes = conteudoService.listarRecentes(limite);
        return ResponseEntity.ok(recentes);
    }

    @GetMapping("/mais-vistos")
    public ResponseEntity<?> listarMaisVistos(
            @RequestParam(defaultValue = "5") int limite) {
        List<ConteudoSimplificadoDTO> maisVistos = conteudoService.listarMaisVistos(limite);
        return ResponseEntity.ok(maisVistos);
    }

    @GetMapping("/validar-slug")
    public ResponseEntity<?> validarSlug(@RequestParam String slug) {
        SlugValidationDTO validacao = conteudoService.validarSlug(slug);
        return ResponseEntity.ok(validacao);
    }

    // ============ ENDPOINTS ADMIN ============

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarConteudo(@Valid @RequestBody ConteudoRequestDTO dto) {
        ConteudoResponseDTO conteudo = conteudoService.criar(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Conteúdo criado com sucesso");
        response.put("conteudo", conteudo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarConteudo(
            @PathVariable Long id,
            @Valid @RequestBody ConteudoRequestDTO dto) {

        ConteudoResponseDTO conteudo = conteudoService.atualizar(id, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Conteúdo atualizado com sucesso");
        response.put("conteudo", conteudo);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarConteudo(@PathVariable Long id) {
        conteudoService.deletar(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusDTO dto) {

        ConteudoResponseDTO conteudo = conteudoService.atualizarStatus(id, dto.isAtivo());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Status atualizado com sucesso");
        response.put("conteudo", conteudo);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/destaque")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarDestaque(
            @PathVariable Long id,
            @RequestBody AtualizarDestaqueDTO dto) {

        ConteudoResponseDTO conteudo = conteudoService.atualizarDestaque(id, dto.isDestaque());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Destaque atualizado com sucesso");
        response.put("conteudo", conteudo);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarConteudosAdmin(@RequestBody BuscaConteudoDTO filtros) {
        PaginatedResponseDTO<ConteudoSimplificadoDTO> resultado =
                conteudoService.buscarConteudos(filtros);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/admin/estatisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEstatisticas() {
        EstatisticasCMSDTO estatisticas = conteudoService.getEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }
}
