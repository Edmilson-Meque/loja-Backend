package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.CategoryResponseDTO;
import com.ecommerce.ecommerce.entity.Category;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test/categorias")
public class TestCategoriesController {

    private final CategoryRepository categoryRepository;

    public TestCategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/listar-raw")
    public List<Category> listarRaw() {
        System.out.println("[TEST] GET /api/test/categorias/listar-raw");
        try {
            List<Category> categorias = categoryRepository.findAll();
            System.out.println("[TEST] Categorias encontradas: " + categorias.size());
            categorias.forEach(c -> {
                System.out.println("[TEST]   - " + c.getId() + ": " + c.getNome() + 
                    " (Produtos: " + (c.getProdutos() != null ? c.getProdutos().size() : "null") + ")");
            });
            return categorias;
        } catch (Exception e) {
            System.err.println("[TEST] ERRO: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/count")
    public Map<String, Object> contarCategorias() {
        long total = categoryRepository.count();
        List<Category> categorias = categoryRepository.findAll();
        
        Map<String, Object> response = new HashMap<>();
        response.put("totalCategorias", total);
        response.put("categorias", categorias.stream()
                .map(c -> new HashMap<String, Object>() {{
                    put("id", c.getId());
                    put("nome", c.getNome());
                    put("descricao", c.getDescricao());
                }})
                .toList());
        
        System.out.println("[TEST] Total de categorias no banco: " + total);
        
        return response;
    }

    @PostMapping("/criar-teste")
    public Map<String, String> criarCategoriaTestePrimeira() {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Verifica se já existe
            if (categoryRepository.count() > 0) {
                response.put("mensagem", "Categoria teste já existe");
                return response;
            }
            
            Category categoria = new Category();
            categoria.setNome("Eletrônicos");
            categoria.setDescricao("Produtos eletrônicos em geral");
            categoria.setImagemUrl("https://via.placeholder.com/200?text=Eletrônicos");
            
            categoryRepository.save(categoria);
            
            response.put("mensagem", "Categoria teste criada com sucesso");
            response.put("id", categoria.getId().toString());
            response.put("nome", categoria.getNome());
            
            System.out.println("[TEST] Categoria teste criada: " + categoria.getNome());
            
            return response;
        } catch (Exception e) {
            System.err.println("[TEST] Erro ao criar categoria teste: " + e.getMessage());
            response.put("erro", e.getMessage());
            return response;
        }
    }
}
