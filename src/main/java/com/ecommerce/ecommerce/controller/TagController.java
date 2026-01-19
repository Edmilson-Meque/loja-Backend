package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.TagDTO;
import com.ecommerce.ecommerce.dto.TagRequestDTO;
import com.ecommerce.ecommerce.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // ============ ENDPOINTS PÚBLICOS ============

    @GetMapping
    public ResponseEntity<?> listarTodas() {
        List<TagDTO> tags = tagService.listarTodas();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/populares")
    public ResponseEntity<?> listarMaisPopulares() {
        List<TagDTO> tags = tagService.listarMaisPopulares();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        TagDTO tag = tagService.buscarPorId(id);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> buscarPorSlug(@PathVariable String slug) {
        TagDTO tag = tagService.buscarPorSlug(slug);
        return ResponseEntity.ok(tag);
    }

    // ============ ENDPOINTS ADMIN ============

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarTag(@Valid @RequestBody TagRequestDTO dto) {
        TagDTO tag = tagService.criar(dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tag criada com sucesso");
        response.put("tag", tag);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizarTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequestDTO dto) {

        TagDTO tag = tagService.atualizar(id, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tag atualizada com sucesso");
        response.put("tag", tag);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarTag(@PathVariable Long id) {
        tagService.deletar(id);
    }
}