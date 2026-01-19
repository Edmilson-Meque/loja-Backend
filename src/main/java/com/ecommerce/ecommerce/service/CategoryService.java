package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CategoryRequestDTO;
import com.ecommerce.ecommerce.dto.CategoryResponseDTO;
import com.ecommerce.ecommerce.entity.Category;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponseDTO criar(CategoryRequestDTO dto) {
        if (categoryRepository.existsByNome(dto.getNome())) {
            throw new BadRequestException("Categoria com este nome já existe");
        }

        Category categoria = new Category();
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setImagemUrl(dto.getImagemUrl());

        Category salva = categoryRepository.save(categoria);

        return new CategoryResponseDTO(
                salva.getId(),
                salva.getNome(),
                salva.getDescricao(),
                salva.getImagemUrl(),
                0 // No products yet
        );
    }

    public List<CategoryResponseDTO> listarTodas() {
        System.out.println("[CategoryService] Chamado listarTodas()");
        long count = categoryRepository.count();
        System.out.println("[CategoryService] Total de categorias no banco: " + count);
        
        List<Category> categorias = categoryRepository.findAll();
        System.out.println("[CategoryService] Categorias recuperadas: " + categorias.size());
        
        return categorias.stream()
                .map(categoria -> {
                    System.out.println("[CategoryService] Mapeando categoria: " + categoria.getNome());
                    return new CategoryResponseDTO(
                        categoria.getId(),
                        categoria.getNome(),
                        categoria.getDescricao(),
                        categoria.getImagemUrl(),
                        categoria.getProdutos() != null ? categoria.getProdutos().size() : 0
                    );
                })
                .toList();
    }

    public CategoryResponseDTO buscarPorId(Long id) {
        Category categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        return new CategoryResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getImagemUrl(),
                categoria.getProdutos() != null ? categoria.getProdutos().size() : 0
        );
    }

    @Transactional
    public CategoryResponseDTO atualizar(Long id, CategoryRequestDTO dto) {
        Category categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        if (!categoria.getNome().equals(dto.getNome()) &&
                categoryRepository.existsByNome(dto.getNome())) {
            throw new BadRequestException("Outra categoria já possui este nome");
        }

        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setImagemUrl(dto.getImagemUrl());

        Category atualizada = categoryRepository.save(categoria);

        return new CategoryResponseDTO(
                atualizada.getId(),
                atualizada.getNome(),
                atualizada.getDescricao(),
                atualizada.getImagemUrl(),
                atualizada.getProdutos() != null ? atualizada.getProdutos().size() : 0
        );
    }

    @Transactional
    public void deletar(Long id) {
        Category categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        if (categoria.getProdutos() != null && !categoria.getProdutos().isEmpty()) {
            throw new BadRequestException("Não é possível deletar categoria com produtos associados");
        }

        categoryRepository.delete(categoria);
    }
}