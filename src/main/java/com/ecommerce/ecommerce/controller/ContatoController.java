package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.ContatoRequestDTO;
import com.ecommerce.ecommerce.dto.ContatoResponseDTO;
import com.ecommerce.ecommerce.dto.RespostaContatoDTO;
import com.ecommerce.ecommerce.service.ContatoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contatos")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    // ============ ENDPOINT PÚBLICO ============

    @PostMapping
    public ResponseEntity<?> enviarContato(
            @Valid @RequestBody ContatoRequestDTO dto,
            HttpServletRequest request) {

        ContatoResponseDTO contato = contatoService.criar(dto, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mensagem enviada com sucesso! Entraremos em contato em breve.");
        response.put("contato", contato);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============ ENDPOINTS ADMIN ============

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarContatos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {

        Page<ContatoResponseDTO> contatos = contatoService.listarTodosPaginado(pagina, tamanho);

        Map<String, Object> response = new HashMap<>();
        response.put("contatos", contatos.getContent());
        response.put("paginaAtual", contatos.getNumber());
        response.put("totalPaginas", contatos.getTotalPages());
        response.put("totalElementos", contatos.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/nao-lidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarNaoLidos() {
        return ResponseEntity.ok(contatoService.listarNaoLidos());
    }

    @GetMapping("/nao-respondidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarNaoRespondidos() {
        return ResponseEntity.ok(contatoService.listarNaoRespondidos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ContatoResponseDTO contato = contatoService.buscarPorId(id);
        return ResponseEntity.ok(contato);
    }

    @PutMapping("/{id}/ler")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> marcarComoLido(@PathVariable Long id) {
        ContatoResponseDTO contato = contatoService.marcarComoLido(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Contato marcado como lido");
        response.put("contato", contato);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/responder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> responderContato(
            @PathVariable Long id,
            @Valid @RequestBody RespostaContatoDTO dto) {

        ContatoResponseDTO contato = contatoService.responder(id, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Resposta enviada com sucesso");
        response.put("contato", contato);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarContato(@PathVariable Long id) {
        contatoService.deletar(id);
    }

    @GetMapping("/estatisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        estatisticas.put("naoLidos", contatoService.contarNaoLidos());
        estatisticas.put("naoRespondidos", contatoService.contarNaoRespondidos());

        return ResponseEntity.ok(estatisticas);
    }
}