package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CategoriaConteudoDTO;
import com.ecommerce.ecommerce.dto.CategoriaConteudoRequestDTO;
import com.ecommerce.ecommerce.dto.SlugValidationDTO;
import com.ecommerce.ecommerce.entity.CategoriaConteudo;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.CategoriaConteudoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaConteudoService {

    private final CategoriaConteudoRepository categoriaConteudoRepository;

    public CategoriaConteudoService(CategoriaConteudoRepository categoriaConteudoRepository) {
        this.categoriaConteudoRepository = categoriaConteudoRepository;
    }

    @Transactional
    public CategoriaConteudoDTO criar(CategoriaConteudoRequestDTO dto) {
        // Validar nome único
        if (categoriaConteudoRepository.existsByNome(dto.getNome())) {
            throw new BadRequestException("Já existe uma categoria com este nome");
        }

        // Validar slug único
        if (categoriaConteudoRepository.existsBySlug(dto.getSlug())) {
            throw new BadRequestException("Slug já está em uso: " + dto.getSlug());
        }

        CategoriaConteudo categoria = new CategoriaConteudo();
        categoria.setNome(dto.getNome());
        categoria.setSlug(dto.getSlug());
        categoria.setDescricao(dto.getDescricao());
        categoria.setIcone(dto.getIcone());
        categoria.setCor(dto.getCor());
        categoria.setOrdem(dto.getOrdem());
        categoria.setAtivo(dto.isAtivo());

        CategoriaConteudo salva = categoriaConteudoRepository.save(categoria);
        return convertToDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaConteudoDTO> listarTodas() {
        return categoriaConteudoRepository.findByAtivoTrueOrderByOrdemAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaConteudoDTO> listarComConteudo() {
        return categoriaConteudoRepository.findCategoriasComConteudo().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaConteudoDTO buscarPorId(Long id) {
        CategoriaConteudo categoria = categoriaConteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));
        return convertToDTO(categoria);
    }

    @Transactional(readOnly = true)
    public CategoriaConteudoDTO buscarPorSlug(String slug) {
        CategoriaConteudo categoria = categoriaConteudoRepository.findBySlug(slug)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        if (!categoria.isAtivo()) {
            throw new BadRequestException("Categoria não está disponível");
        }

        return convertToDTO(categoria);
    }

    @Transactional
    public CategoriaConteudoDTO atualizar(Long id, CategoriaConteudoRequestDTO dto) {
        CategoriaConteudo categoria = categoriaConteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        // Verificar se nome foi alterado e se é único
        if (!categoria.getNome().equals(dto.getNome()) &&
                categoriaConteudoRepository.existsByNome(dto.getNome())) {
            throw new BadRequestException("Já existe uma categoria com este nome");
        }

        // Verificar se slug foi alterado e se é único
        if (!categoria.getSlug().equals(dto.getSlug()) &&
                categoriaConteudoRepository.existsBySlug(dto.getSlug())) {
            throw new BadRequestException("Slug já está em uso: " + dto.getSlug());
        }

        categoria.setNome(dto.getNome());
        categoria.setSlug(dto.getSlug());
        categoria.setDescricao(dto.getDescricao());
        categoria.setIcone(dto.getIcone());
        categoria.setCor(dto.getCor());
        categoria.setOrdem(dto.getOrdem());
        categoria.setAtivo(dto.isAtivo());
        categoria.atualizarData();

        CategoriaConteudo atualizada = categoriaConteudoRepository.save(categoria);
        return convertToDTO(atualizada);
    }

    @Transactional
    public void deletar(Long id) {
        CategoriaConteudo categoria = categoriaConteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        // Verificar se tem conteúdos associados
        if (!categoria.getConteudos().isEmpty()) {
            throw new BadRequestException("Não é possível deletar categoria com conteúdos associados");
        }

        categoriaConteudoRepository.delete(categoria);
    }

    @Transactional
    public CategoriaConteudoDTO atualizarStatus(Long id, boolean ativo) {
        CategoriaConteudo categoria = categoriaConteudoRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        categoria.setAtivo(ativo);
        categoria.atualizarData();

        CategoriaConteudo atualizada = categoriaConteudoRepository.save(categoria);
        return convertToDTO(atualizada);
    }

    @Transactional(readOnly = true)
    public SlugValidationDTO validarSlug(String slug) {
        boolean disponivel = !categoriaConteudoRepository.existsBySlug(slug);
        return new SlugValidationDTO(slug, disponivel);
    }

    private CategoriaConteudoDTO convertToDTO(CategoriaConteudo categoria) {
        CategoriaConteudoDTO dto = new CategoriaConteudoDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setSlug(categoria.getSlug());
        dto.setDescricao(categoria.getDescricao());
        dto.setIcone(categoria.getIcone());
        dto.setCor(categoria.getCor());
        dto.setOrdem(categoria.getOrdem());
        dto.setAtivo(categoria.isAtivo());
        dto.setDataCriacao(categoria.getDataCriacao());
        dto.setDataAtualizacao(categoria.getDataAtualizacao());
        dto.setTotalConteudos(categoria.getConteudos() != null ? categoria.getConteudos().size() : 0);
        return dto;
    }
}