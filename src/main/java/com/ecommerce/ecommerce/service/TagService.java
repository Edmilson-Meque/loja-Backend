package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.TagDTO;
import com.ecommerce.ecommerce.dto.TagRequestDTO;
import com.ecommerce.ecommerce.entity.Tag;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional
    public TagDTO criar(TagRequestDTO dto) {
        // Validar nome único
        if (tagRepository.existsByNome(dto.getNome())) {
            throw new BadRequestException("Já existe uma tag com este nome");
        }

        // Validar slug único
        if (tagRepository.existsBySlug(dto.getSlug())) {
            throw new BadRequestException("Slug já está em uso: " + dto.getSlug());
        }

        Tag tag = new Tag();
        tag.setNome(dto.getNome());
        tag.setSlug(dto.getSlug());
        tag.setDescricao(dto.getDescricao());
        tag.setCor(dto.getCor());

        Tag salva = tagRepository.save(tag);
        return convertToDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<TagDTO> listarTodas() {
        return tagRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TagDTO> listarMaisPopulares() {
        return tagRepository.findMaisPopulares().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TagDTO buscarPorId(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tag não encontrada"));
        return convertToDTO(tag);
    }

    @Transactional(readOnly = true)
    public TagDTO buscarPorSlug(String slug) {
        Tag tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new BadRequestException("Tag não encontrada"));
        return convertToDTO(tag);
    }

    @Transactional
    public TagDTO atualizar(Long id, TagRequestDTO dto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tag não encontrada"));

        // Verificar se nome foi alterado e se é único
        if (!tag.getNome().equals(dto.getNome()) &&
                tagRepository.existsByNome(dto.getNome())) {
            throw new BadRequestException("Já existe uma tag com este nome");
        }

        // Verificar se slug foi alterado e se é único
        if (!tag.getSlug().equals(dto.getSlug()) &&
                tagRepository.existsBySlug(dto.getSlug())) {
            throw new BadRequestException("Slug já está em uso: " + dto.getSlug());
        }

        tag.setNome(dto.getNome());
        tag.setSlug(dto.getSlug());
        tag.setDescricao(dto.getDescricao());
        tag.setCor(dto.getCor());

        Tag atualizada = tagRepository.save(tag);
        return convertToDTO(atualizada);
    }

    @Transactional
    public void deletar(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tag não encontrada"));

        tagRepository.delete(tag);
    }

    private TagDTO convertToDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setNome(tag.getNome());
        dto.setSlug(tag.getSlug());
        dto.setDescricao(tag.getDescricao());
        dto.setCor(tag.getCor());
        dto.setDataCriacao(tag.getDataCriacao());
        dto.setTotalConteudos(tag.getConteudos() != null ? tag.getConteudos().size() : 0);
        return dto;
    }
}