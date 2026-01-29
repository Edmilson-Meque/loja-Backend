package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.CategoryRequestDTO;
import com.ecommerce.ecommerce.dto.CategoryResponseDTO;
import com.ecommerce.ecommerce.entity.Category;
import com.ecommerce.ecommerce.exception.BadRequestException;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import com.ecommerce.ecommerce.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private  final ProdutoRepository produtoRepository;

    public CategoryService(CategoryRepository categoryRepository, ProdutoRepository produtoRepository) {
        this.categoryRepository = categoryRepository;
        this.produtoRepository=produtoRepository;
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


    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> listarTodas() {
        System.out.println("[CategoryService] Iniciando listarTodas()");
        List<Category> categorias = categoryRepository.findAll();
        System.out.println("[CategoryService] Total de categorias encontradas: " + categorias.size());

        List<CategoryResponseDTO> resultado = categorias.stream()
                .map(categoria -> {
                    System.out.println("[CategoryService] Mapeando categoria: " + categoria.getNome());

                    int quantidadeProdutos = (int) produtoRepository.countByCategoriaId(categoria.getId());

                    System.out.println("[CategoryService]   Produtos: " + quantidadeProdutos);

                    return new CategoryResponseDTO(
                            categoria.getId(),
                            categoria.getNome(),
                            categoria.getDescricao(),
                            categoria.getImagemUrl(),
                            quantidadeProdutos
                    );
                })
                .toList();

        System.out.println("[CategoryService] Mapeamento concluído com sucesso");
        return resultado;
    }

    public List<CategoryResponseDTO> listarSimples() {
        try {
            System.out.println("[CategoryService] Iniciando listarSimples() - SEM ACESSAR PRODUTOS");
            List<Category> categorias = categoryRepository.findAll();
            System.out.println("[CategoryService] Total de categorias encontradas: " + categorias.size());
            
            List<CategoryResponseDTO> resultado = categorias.stream()
                    .map(categoria -> {
                        System.out.println("[CategoryService] Mapeando categoria simples: " + categoria.getId() + " - " + categoria.getNome());
                        return new CategoryResponseDTO(
                                categoria.getId(),
                                categoria.getNome(),
                                categoria.getDescricao(),
                                categoria.getImagemUrl(),
                                0 // Não acessa getProdutos() para evitar lazy loading
                        );
                    })
                    .toList();
            
            System.out.println("[CategoryService] Mapeamento simples concluído com sucesso: " + resultado.size() + " categorias");
            return resultado;
        } catch (Exception e) {
            System.err.println("[CategoryService] ERRO em listarSimples(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public CategoryResponseDTO buscarPorId(Long id) {
        Category categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        return new CategoryResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getImagemUrl(),
                categoria.getProdutos().size()
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
                atualizada.getProdutos().size()
        );
    }

    @Transactional
    public void deletar(Long id) {
        Category categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Categoria não encontrada"));

        if (!categoria.getProdutos().isEmpty()) {
            throw new BadRequestException("Não é possível deletar categoria com produtos associados");
        }

        categoryRepository.delete(categoria);
    }
}
