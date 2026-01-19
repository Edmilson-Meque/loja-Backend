package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SafeProdutoRepository extends ProdutoRepository {

    // Usar parâmetros nomeados para prevenir SQL injection
    @Query("SELECT p FROM Produto p WHERE " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:categoriaId IS NULL OR p.categoria.id = :categoriaId) AND " +
            "(:minPreco IS NULL OR p.preco >= :minPreco) AND " +
            "(:maxPreco IS NULL OR p.preco <= :maxPreco)")
    List<Produto> buscarComFiltrosSeguros(
            @Param("nome") String nome,
            @Param("categoriaId") Long categoriaId,
            @Param("minPreco") BigDecimal minPreco,
            @Param("maxPreco") BigDecimal maxPreco);
}